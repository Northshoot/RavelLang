# Friend Tracker

A minimal, no-fuss way to log who you see and how often. No app to build — just a Notion workspace + an iOS Shortcut.

## Files

1. **[01-notion-setup.md](./01-notion-setup.md)** — People + Events databases, dashboard views, how to grab the IDs you'll need.
2. **[02-notion-form.md](./02-notion-form.md)** — Native Notion form for fast entry from any browser; add-to-home-screen instructions.
3. **[03-ios-shortcut.md](./03-ios-shortcut.md)** — Step-by-step iOS Shortcut build with the exact Notion API payloads. Dictate notes, one-tap log.

## Setup order

1. Build the two databases (`01`).
2. Create the integration + connect it to both DBs (`01` step 3).
3. Build the form (`02`) — takes 2 minutes, gets you running today.
4. Build the Shortcut (`03`) when you want the fast path.

## Analytics (later)

Everything lives in the Events DB. Post-process options:

- **In Notion:** group by Person / Type, rollups, calendar view.
- **CSV export:** `⋯` → `Export` → CSV, then load into anything — Numbers, Python, duckdb.
- **Direct API:** query `/v1/databases/{EVENTS_DB_ID}/query` and aggregate however you like.
