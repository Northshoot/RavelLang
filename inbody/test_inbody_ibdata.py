"""Tests for inbody_ibdata.

Run with:  python3 -m unittest inbody.test_inbody_ibdata -v
"""

import logging
import unittest
from datetime import datetime

from inbody.inbody_ibdata import (
    IBDataError,
    extract_ibdata,
    parse_and_log,
    parse_ibdata,
)

# Real InBody 270 QR payload captured from a scanned result sheet.
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
SAMPLE_PAYLOAD = SAMPLE_URL.split("IBData=", 1)[1]


class ExtractIBDataTests(unittest.TestCase):
    def test_extract_from_full_url(self):
        self.assertEqual(extract_ibdata(SAMPLE_URL), SAMPLE_PAYLOAD)

    def test_extract_is_case_insensitive(self):
        self.assertEqual(
            extract_ibdata("https://qrcode.inbody.com/?ibdata=270-53X"),
            "270-53X",
        )

    def test_extract_bare_payload(self):
        self.assertEqual(extract_ibdata(SAMPLE_PAYLOAD), SAMPLE_PAYLOAD)

    def test_extract_preserves_special_chars(self):
        # & = + must survive: parsing must not use urllib query parsing.
        self.assertEqual(
            extract_ibdata("https://qrcode.inbody.com/?IBData=270-53A&B=C+D"),
            "270-53A&B=C+D",
        )

    def test_extract_raises_on_url_without_param(self):
        with self.assertRaises(IBDataError):
            extract_ibdata("https://qrcode.inbody.com/")

    def test_extract_raises_on_empty(self):
        with self.assertRaises(IBDataError):
            extract_ibdata("   ")


class ParseIBDataTests(unittest.TestCase):
    def setUp(self):
        self.result = parse_ibdata(SAMPLE_URL)

    def test_model_code_and_device(self):
        self.assertEqual(self.result.model_code, "270-53")
        self.assertEqual(self.result.device_model, "InBody 270")

    def test_serial(self):
        self.assertEqual(self.result.serial, "F921003541")

    def test_gender(self):
        self.assertEqual(self.result.gender, "M")

    def test_header_numeric(self):
        self.assertEqual(self.result.header_numeric, "19300470")

    def test_test_datetime(self):
        self.assertEqual(
            self.result.test_datetime, datetime(2026, 5, 21, 18, 35, 26)
        )

    def test_status(self):
        self.assertEqual(self.result.status, "PASS")

    def test_measurement_segments_present(self):
        self.assertTrue(self.result.measurement_segments)
        self.assertTrue(
            all(s.isdigit() for s in self.result.measurement_segments)
        )

    def test_trailing_data_captured(self):
        self.assertTrue(self.result.trailing.isdigit())

    def test_secondary_block(self):
        self.assertEqual(len(self.result.secondary_blocks), 1)
        self.assertEqual(self.result.secondary_blocks[0].code, "270-2DM-0484")
        self.assertTrue(self.result.secondary_blocks[0].segments)

    def test_bare_payload_parses_same(self):
        self.assertEqual(
            parse_ibdata(SAMPLE_PAYLOAD).to_dict(), self.result.to_dict()
        )

    def test_to_dict_is_json_safe(self):
        import json

        json.dumps(self.result.to_dict())  # must not raise

    def test_rejects_non_inbody_payload(self):
        with self.assertRaises(IBDataError):
            parse_ibdata("https://example.com/?IBData=not-a-real-payload")


class LoggingTests(unittest.TestCase):
    def test_parse_and_log_emits_structured_line(self):
        with self.assertLogs("inbody.ibdata", level=logging.INFO) as captured:
            result = parse_and_log(SAMPLE_URL)
        self.assertEqual(result.model_code, "270-53")
        self.assertEqual(len(captured.records), 1)
        self.assertIn("inbody.result", captured.records[0].getMessage())

    def test_parse_and_log_logs_failure(self):
        with self.assertLogs("inbody.ibdata", level=logging.ERROR):
            with self.assertRaises(IBDataError):
                parse_and_log("https://qrcode.inbody.com/")


if __name__ == "__main__":
    unittest.main()
