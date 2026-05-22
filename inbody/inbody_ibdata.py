"""Parser and logger for InBody QR-code result payloads (``IBData``).

Background
----------
InBody body-composition analyzers (InBody 270, 370, 570, 770, ...) print a
QR code on the result sheet. Scanning the QR opens a URL shaped like::

    https://qrcode.inbody.com/?IBData=<payload>

``<payload>`` is one long string with almost no separators. It contains:

* a **primary data block** introduced by a model/equation code such as
  ``270-53`` (``270`` -> InBody 270, ``53`` -> data-format revision);
* zero or more **secondary data blocks** introduced by codes such as
  ``270-2DM-0484`` (extra segmental / research data);
* ``!`` characters, which InBody uses to *fill empty text fields*
  (blank name, member id, ...);
* a ``PASS`` / ``FAIL`` token carrying the device self-test result.

What this module does
---------------------
It decodes the fields that can be identified **unambiguously** -- model
code, device model, serial, gender, test timestamp, PASS/FAIL status --
and exposes the numeric measurement payload as raw, ``!``-delimited
segments. Turning those raw segments into named metrics (weight, PBF,
SMM, ...) requires InBody's proprietary IBData field spec; see
``FIELD_MAP`` near the bottom of this file.
"""

from __future__ import annotations

import json
import logging
import re
from dataclasses import dataclass
from datetime import datetime
from typing import Optional

logger = logging.getLogger("inbody.ibdata")

# --- constants -------------------------------------------------------------

INBODY_QR_HOST = "qrcode.inbody.com"
EMPTY_FIELD_FILLER = "!"

DEVICE_MODELS = {
    "230": "InBody 230",
    "270": "InBody 270",
    "370": "InBody 370",
    "380": "InBody 380",
    "570": "InBody 570",
    "770": "InBody 770",
    "970": "InBody 970",
}

# Primary block code, e.g. "270-53": 3-digit model + "-" + revision digits.
_PRIMARY_CODE_RE = re.compile(r"^(\d{3})-(\d+)")

# Secondary block code, e.g. "270-2DM-0484".
_SECONDARY_CODE_RE = re.compile(r"\d{3}-\d[A-Z]+-\d+")

# Test timestamp: YYYYMMDDhhmmss with a plausible 20xx year.
_DATETIME_RE = re.compile(
    r"20\d{2}(?:0[1-9]|1[0-2])(?:0[1-9]|[12]\d|3[01])"
    r"(?:[01]\d|2[0-3])[0-5]\d[0-5]\d"
)

# Leading alphanumeric serial of the primary block.
_SERIAL_RE = re.compile(r"^[0-9A-Za-z]+")

# Device self-test result token.
_STATUS_RE = re.compile(r"PASS|FAIL")


class IBDataError(ValueError):
    """Raised when an IBData payload cannot be parsed."""


# --- data model ------------------------------------------------------------


@dataclass
class SecondaryBlock:
    code: str
    raw: str
    segments: list[str]


@dataclass
class IBDataResult:
    model_code: str
    device_model: str
    serial: str
    # Digits between the serial and the gender marker. Meaning is not yet
    # verified against the InBody spec (likely height/age or a member id).
    header_numeric: str
    gender: Optional[str]
    test_datetime: Optional[datetime]
    status: Optional[str]
    raw_measurement: str
    measurement_segments: list[str]
    trailing: str
    secondary_blocks: list[SecondaryBlock]
    payload: str

    def to_dict(self) -> dict:
        return {
            "model_code": self.model_code,
            "device_model": self.device_model,
            "serial": self.serial,
            "header_numeric": self.header_numeric,
            "gender": self.gender,
            "test_datetime": (
                self.test_datetime.isoformat() if self.test_datetime else None
            ),
            "status": self.status,
            "measurement_field_count": sum(
                len(s) // 4 for s in self.measurement_segments
            ),
            "measurement_segments": self.measurement_segments,
            "trailing": self.trailing,
            "secondary_blocks": [
                {"code": b.code, "segments": b.segments}
                for b in self.secondary_blocks
            ],
        }


# --- parsing ---------------------------------------------------------------


def extract_ibdata(scanned: str) -> str:
    """Return the raw IBData payload from a scanned InBody QR value.

    ``scanned`` may be the full URL or the bare payload. The payload is
    taken *verbatim* after ``IBData=``; it is deliberately NOT run through
    URL query-string parsing, because an IBData payload can legitimately
    contain ``&``, ``=`` and ``+``, which ``urllib.parse`` would corrupt.
    """
    if not scanned or not scanned.strip():
        raise IBDataError("empty QR value")
    text = scanned.strip()
    lowered = text.lower()
    idx = lowered.find("ibdata=")
    if idx != -1:
        return text[idx + len("ibdata="):]
    if lowered.startswith("http"):
        raise IBDataError(f"URL has no IBData parameter: {scanned!r}")
    return text


def parse_ibdata(scanned: str) -> IBDataResult:
    """Parse a scanned InBody QR value (full URL or bare payload)."""
    payload = extract_ibdata(scanned)

    code_match = _PRIMARY_CODE_RE.match(payload)
    if not code_match:
        raise IBDataError(
            "payload does not start with a model code (e.g. '270-53'): "
            f"{payload[:32]!r}"
        )
    model_number, revision = code_match.group(1), code_match.group(2)
    model_code = f"{model_number}-{revision}"
    device_model = DEVICE_MODELS.get(
        model_number, f"InBody (model {model_number})"
    )

    # Separate the primary region from any trailing secondary blocks.
    secondaries = list(_SECONDARY_CODE_RE.finditer(payload))
    primary_end = secondaries[0].start() if secondaries else len(payload)
    primary = payload[code_match.end():primary_end]

    # Serial = leading alphanumeric run of the primary region.
    serial_match = _SERIAL_RE.match(primary)
    serial = serial_match.group(0) if serial_match else ""
    serial_end = serial_match.end() if serial_match else 0

    # Test timestamp + gender marker (gender sits right before the stamp).
    dt_match = _DATETIME_RE.search(primary)
    test_datetime: Optional[datetime] = None
    gender: Optional[str] = None
    header_numeric = ""
    if dt_match:
        try:
            test_datetime = datetime.strptime(
                dt_match.group(0), "%Y%m%d%H%M%S"
            )
        except ValueError:
            logger.warning(
                "IBData timestamp %r is not a valid datetime",
                dt_match.group(0),
            )
        header = primary[serial_end:dt_match.start()].replace(
            EMPTY_FIELD_FILLER, ""
        )
        if header and header[-1] in ("M", "F"):
            gender = header[-1]
            header_numeric = header[:-1]
        else:
            header_numeric = header
        measurement_region = primary[dt_match.end():]
    else:
        logger.warning("no test timestamp found in IBData primary block")
        measurement_region = primary[serial_end:]

    # PASS/FAIL status splits the measurement payload from the trailing data.
    status: Optional[str] = None
    trailing = ""
    status_match = _STATUS_RE.search(measurement_region)
    if status_match:
        status = status_match.group(0)
        raw_measurement = measurement_region[:status_match.start()]
        trailing = measurement_region[status_match.end():]
    else:
        raw_measurement = measurement_region

    measurement_segments = [
        seg for seg in raw_measurement.split(EMPTY_FIELD_FILLER) if seg
    ]

    # Secondary blocks.
    secondary_blocks: list[SecondaryBlock] = []
    for i, sec_match in enumerate(secondaries):
        end = (
            secondaries[i + 1].start()
            if i + 1 < len(secondaries)
            else len(payload)
        )
        body = payload[sec_match.end():end].lstrip(EMPTY_FIELD_FILLER)
        segs = [s for s in body.split(EMPTY_FIELD_FILLER) if s]
        secondary_blocks.append(
            SecondaryBlock(code=sec_match.group(0), raw=body, segments=segs)
        )

    return IBDataResult(
        model_code=model_code,
        device_model=device_model,
        serial=serial,
        header_numeric=header_numeric,
        gender=gender,
        test_datetime=test_datetime,
        status=status,
        raw_measurement=raw_measurement,
        measurement_segments=measurement_segments,
        trailing=trailing,
        secondary_blocks=secondary_blocks,
        payload=payload,
    )


# --- logging ---------------------------------------------------------------


def log_result(result: IBDataResult, *, level: int = logging.INFO) -> None:
    """Emit the parsed result as a single structured JSON log line."""
    logger.log(
        level, "inbody.result %s", json.dumps(result.to_dict(), default=str)
    )


def parse_and_log(scanned: str) -> IBDataResult:
    """Parse a scanned InBody QR value and log the structured result."""
    try:
        result = parse_ibdata(scanned)
    except IBDataError:
        logger.exception("failed to parse InBody IBData payload")
        raise
    log_result(result)
    return result


# ---------------------------------------------------------------------------
# Named-metric mapping -- INTENTIONALLY INCOMPLETE.
#
# The measurement segments are zero-padded, fixed-width integers. Both the
# width of each field and the metric it carries are defined by InBody's
# proprietary IBData specification, which is not bundled in this repo.
#
# To finish the named-metric decoding:
#   1. Obtain the IBData field spec for the "270-53" data revision from
#      InBody (developer/partner support, or the LookinBody Web docs).
#   2. Fill FIELD_MAP with name -> (segment_index, offset, width, scale).
#   3. Read named metrics with measurement_value().
#
# Until then the raw segments are still parsed and logged, so no data is
# lost -- only the human-readable metric names are pending.
# ---------------------------------------------------------------------------

FIELD_MAP: dict[str, tuple[int, int, int, float]] = {
    # "weight_kg":   (segment_index, offset, width, scale),
    # "pbf_percent": (segment_index, offset, width, scale),
}


def measurement_value(result: IBDataResult, name: str) -> Optional[float]:
    """Return a named metric, or None if it is not in FIELD_MAP / not numeric."""
    spec = FIELD_MAP.get(name)
    if spec is None:
        return None
    seg_idx, offset, width, scale = spec
    if seg_idx >= len(result.measurement_segments):
        return None
    raw = result.measurement_segments[seg_idx][offset:offset + width]
    if not raw.isdigit():
        return None
    return int(raw) * scale


# --- demo ------------------------------------------------------------------

SAMPLE_URL = (
    "https://qrcode.inbody.com/?IBData=270-53F921003541"
    "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!19300470M20260521183526"
    "019801230151070904270521010600980197072704610563099611020696094213"
    "440577146400861029600961101102!0000!0000!0000252100780080009004581"
    "1013433160000000000000000000025102417015819532018216020920125169717"
    "52PASS00137004740123051203940819000002200150008511121000222220010000"
    "010270-2DM-0484!!!!026901850250030001000200057705930408148014471353"
    "139312011249122101103106270766"
)

if __name__ == "__main__":
    import sys

    logging.basicConfig(
        level=logging.INFO, format="%(levelname)s %(name)s %(message)s"
    )
    scanned_value = sys.argv[1] if len(sys.argv) > 1 else SAMPLE_URL
    parse_and_log(scanned_value)
