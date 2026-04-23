# Notion setup — Friend Tracker

Two linked databases: **People** and **Events**. Everything else (dashboards, form, Shortcut) hangs off this schema.

---

## 1. Create the databases

In Notion, make a new page called `Friend Tracker`. Inside it, add two full-page databases.

### People (database)

| Property      | Type          | Options / notes                                                                    |
| ------------- | ------------- | ---------------------------------------------------------------------------------- |
| `Name`        | Title         | Built-in title property.                                                           |
| `Description` | Text          | Freeform — how you know them, vibe, anything.                                      |
| `Tags`        | Multi-select  | Suggested: `Close`, `Work`, `Family`, `Partner`, `Old friend`, `New`, `Long-dist`. |
| `City`        | Text          | Optional. Handy for travel planning.                                               |
| `First met`   | Date          | Optional.                                                                          |
| `Events`      | Relation      | Relates to **Events → Person**. Two-way.                                           |
| `Event count` | Rollup        | Source: `Events` • Property: `Name` • Calculate: **Count all**.                    |
| `Last seen`   | Rollup        | Source: `Events` • Property: `Date` • Calculate: **Latest date**.                  |

### Events (database)

| Property    | Type         | Options / notes                                                                                                              |
| ----------- | ------------ | ---------------------------------------------------------------------------------------------------------------------------- |
| `Name`      | Title        | Auto-fill pattern: `{Type} with {Person}` — fine to leave blank and just type.                                               |
| `Person`    | Relation     | Relates to **People → Events**. Allow multiple (groups).                                                                     |
| `Type`      | Select       | Suggested: `Coffee`, `Meal`, `Drinks`, `Call`, `Video`, `Walk`, `Home hang`, `Party`, `Event`, `Trip`, `Work`, `Run`, `Other`. |
| `Date`      | Date         | Include time if you care about duration.                                                                                     |
| `Duration`  | Number       | Format: `Number`. Unit is "hours" — you pick convention.                                                                     |
| `Location`  | Text         | Optional.                                                                                                                    |
| `Notes`     | Text         | What happened, what you talked about.                                                                                        |
| `Mood`      | Select       | Optional: `🔥`, `🙂`, `😐`, `😕`. Useful signal over time.                                                                   |

Tip: when you create the `Person` relation on Events, Notion will offer to add a reciprocal property on People — say yes, name it `Events`.

---

## 2. Dashboard views

Open the `Friend Tracker` page and add **linked database** views of **Events**:

1. **This week** — Filter: `Date` is within `this week`. Sort: `Date` descending.
2. **This month by person** — Group by `Person`. Sort groups by count descending.
3. **By type** — Board view, group by `Type`.
4. **Calendar** — Calendar view on `Date`.
5. **All events** — Table, sort `Date` descending. The raw log.

Add a **linked database** view of **People**:

6. **Seen recently** — Sort by `Last seen` descending. Surfaces who you saw last.
7. **Overdue** — Filter: `Last seen` is more than `30 days ago` AND `Tags` contains `Close`. Your "ping these people" list.

---

## 3. Grab the IDs you'll need for the Shortcut

You need three things for the iOS Shortcut (see `03-ios-shortcut.md`):

1. **Integration token** — at <https://www.notion.so/my-integrations>, create a new internal integration (name: `Friend Tracker Shortcut`). Copy the secret — starts with `ntn_` or `secret_`.
2. **Share each database with the integration** — open the People database → `⋯` menu → `Connections` → add your integration. Repeat for Events. Without this, the API returns 404.
3. **Database IDs** — open each DB as a full page; the URL looks like  
   `https://www.notion.so/<workspace>/<DB_ID>?v=<view>`  
   The `DB_ID` is a 32-char hex string. Save both.

Store these somewhere you won't lose them (password manager). You'll paste them into the Shortcut once.
