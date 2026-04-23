# Prompt for a Notion-connected LLM

Paste this into Claude (with the Notion MCP server), ChatGPT (with the Notion
connector), or any agent that has Notion API access. It packages the schema
from `01-notion-setup.md` and `02-notion-form.md` into a single autonomous
setup task.

---

```
You have access to Notion (via MCP / connector / the Notion API). Set up a
"Friend Tracker" workspace for me from scratch. Work autonomously — only stop to
ask a question if something is genuinely ambiguous and you can't make a
reasonable default.

GOAL
Track which friends I see, how often, and what we did, so I can review
analytics (events per person, per type, per month) later. Minimal friction for
data entry. Single-user, personal use.

STEPS — do these in order and report back at the end

1. Create a top-level page titled "Friend Tracker".

2. Inside it, create a full-page database called "People" with these
   properties (exact names, exact types):
   - Name         (Title)
   - Description  (Text)
   - Tags         (Multi-select) with options:
                  Close, Work, Family, Partner, Old friend, New, Long-dist
   - City         (Text)
   - First met    (Date)
   - Events       (Relation → the Events DB you'll create in step 3; two-way;
                  reciprocal property on Events should be named "Person")
   - Event count  (Rollup: source=Events, property=Name, calc=Count all)
   - Last seen    (Rollup: source=Events, property=Date, calc=Latest date)

3. Inside "Friend Tracker", create a full-page database called "Events" with
   these properties:
   - Name      (Title)   — leave blank default; I'll type or auto-fill later
   - Person    (Relation → People; allow multiple; reciprocal is the "Events"
               property on People from step 2)
   - Type      (Select) with options:
               Coffee, Meal, Drinks, Call, Video, Walk, Home hang, Party,
               Event, Trip, Work, Run, Other
   - Date      (Date; include time)
   - Duration  (Number; plain number, unit is hours by convention)
   - Location  (Text)
   - Notes     (Text)
   - Mood      (Select) with options: 🔥, 🙂, 😐, 😕

4. On the "Friend Tracker" page, add these linked-database views (use linked
   views, not new databases):

   Events views:
   - "This week"           — Filter: Date is within this week; Sort: Date desc.
   - "This month by person"— Group by Person; sort groups by count desc.
   - "By type"             — Board view grouped by Type.
   - "Calendar"            — Calendar view on Date.
   - "All events"          — Table, sort Date desc.

   People views:
   - "Seen recently"       — Sort by Last seen desc.
   - "Overdue"             — Filter: Last seen > 30 days ago AND Tags contains
                             Close.

5. On the Events database, create a Form view called "Log event":
   - Visible fields in this order: Person, Type, Date, Notes, Mood, Location,
     Duration.
   - Person, Type, Date required; rest optional.
   - Date default = Today.
   - Hide Name from the form.
   - Access: Anyone with the link.
   - After submit: show "Logged ✅", allow submit another.

6. Seed the People database with these rows so I can test immediately (leave
   Description/Tags blank unless I gave them): Alice, Bob, Carol.
   (If the user provides a real list in a follow-up, add those instead.)

7. Create one sample Event: Type=Coffee, Person=Alice, Date=today, Notes="Test
   entry — safe to delete."

REPORT BACK with:
- Link to the Friend Tracker page.
- Link to the Log event form (public form URL).
- The People database ID and Events database ID (32-char hex).
- The Notion API version you used.
- Any step you skipped or adapted, and why.
- A 3-line "next steps" note reminding me to:
    (a) create an internal integration at notion.so/my-integrations,
    (b) connect it to both databases via the Connections menu,
    (c) paste the token + the two DB IDs into my iOS Shortcut.

CONSTRAINTS
- Don't create any databases or pages outside the Friend Tracker page.
- Don't invent extra properties "to be helpful" — stick to the schema above.
- If a property type isn't supported by your tool (e.g. Rollup via API),
  create it with the closest supported type and flag it in the report so I
  can fix it manually in the Notion UI.
- Don't share anything publicly except the form URL in step 5.
```

---

## Notes on common connectors

- **Claude Desktop + official Notion MCP** — supports creating pages, databases,
  and most property types. Rollups typically must be added in the UI after the
  fact; the prompt's "CONSTRAINTS" clause covers that gracefully.
- **ChatGPT Notion connector** — read/write scoped to pages you've shared with
  the connector; may refuse to create top-level pages. If so, share an existing
  parent page first and tell it to create "Friend Tracker" inside that page.
- **Custom agent hitting the Notion REST API directly** — fully capable; no
  caveats.
