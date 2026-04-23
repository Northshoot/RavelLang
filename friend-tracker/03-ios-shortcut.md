# iOS Shortcut — "Log friend event"

Target flow, thumb-on-lock-screen:

1. Tap the Shortcut.
2. Pick person (list pulled live from Notion).
3. Pick event type (fixed menu).
4. Dictate notes (optional — skip with the X button).
5. Done. Row appears in the **Events** database, dated today.

Total: ~8 seconds, no typing.

---

## 0. One-time setup

You need (from `01-notion-setup.md`):

- **`NOTION_TOKEN`** — your integration secret.
- **`PEOPLE_DB_ID`** — 32-char hex.
- **`EVENTS_DB_ID`** — 32-char hex.

Confirm your integration has access to **both** databases (Connections menu on each DB).

---

## 1. Build the Shortcut

Open **Shortcuts** app → `+` → name it `Log friend`.

### Step 1 — Variables

Use the **Text** action to store config, then **Set variable**. Repeat three times:

| Set variable   | Value                                |
| -------------- | ------------------------------------ |
| `NotionToken`  | `ntn_XXXXXXXXXXXX...` (your secret)  |
| `PeopleDB`     | `<32-char hex>`                      |
| `EventsDB`     | `<32-char hex>`                      |

(You can also use **Shortcut Input** / keychain, but an inline variable is fine for a personal shortcut.)

### Step 2 — Fetch people list

Add **Get contents of URL**:

- **URL:** `https://api.notion.com/v1/databases/[PeopleDB]/query`
- **Method:** `POST`
- **Headers:**
  - `Authorization` → `Bearer [NotionToken]`
  - `Notion-Version` → `2022-06-28`
  - `Content-Type` → `application/json`
- **Request body:** `JSON`
  ```json
  {
    "page_size": 100,
    "sorts": [{ "property": "Name", "direction": "ascending" }]
  }
  ```

### Step 3 — Extract names + IDs

Add **Get dictionary value** → Get `results` from previous step.

Add **Repeat with each** → over `results`. Inside the loop:

- **Get dictionary value** → `id` from Repeat Item → Set variable `personId`.
- **Get dictionary value** → `properties.Name.title.[0].plain_text` from Repeat Item → Set variable `personName`.
- **Add to dictionary** (variable `peopleMap`): key = `personName`, value = `personId`.

(The `peopleMap` dictionary accumulates across the loop.)

After the loop ends, add **Get dictionary keys** from `peopleMap` → store as `personNames`.

### Step 4 — Pick a person

Add **Choose from list**:

- **Input:** `personNames`
- **Prompt:** `Who did you see?`

Then **Get dictionary value** → key = (Chosen Item) from `peopleMap` → Set variable `chosenPersonId`.

### Step 5 — Pick event type

Add **Choose from Menu**:

- **Prompt:** `What kind?`
- Items: `Coffee`, `Meal`, `Drinks`, `Call`, `Video`, `Walk`, `Home hang`, `Party`, `Event`, `Trip`, `Work`, `Run`, `Other`.

Each menu item just runs **Set variable** `eventType` to the literal string of that menu label, then falls through. (Shortcuts forces one action per branch — that's fine.)

### Step 6 — Dictate notes

Add **Dictate text**:

- **Language:** (your choice)
- **Stop listening:** `After pause`

Set variable `notesText` to the dictated text. If you want "notes optional," wrap in an **If** that checks `Dictated Text` is not empty — but the API accepts empty strings fine.

### Step 7 — Today's date

Add **Current Date** → **Format Date**:

- **Format:** `Custom`
- **Format string:** `yyyy-MM-dd`

Set variable `todayISO`.

### Step 8 — Create the Notion page

Add **Get contents of URL**:

- **URL:** `https://api.notion.com/v1/pages`
- **Method:** `POST`
- **Headers:** same three as Step 2.
- **Request body:** `JSON` —

  ```json
  {
    "parent": { "database_id": "[EventsDB]" },
    "properties": {
      "Name": {
        "title": [
          { "text": { "content": "[eventType] with [Chosen Item]" } }
        ]
      },
      "Person": {
        "relation": [{ "id": "[chosenPersonId]" }]
      },
      "Type": {
        "select": { "name": "[eventType]" }
      },
      "Date": {
        "date": { "start": "[todayISO]" }
      },
      "Notes": {
        "rich_text": [
          { "text": { "content": "[notesText]" } }
        ]
      }
    }
  }
  ```

  Every `[bracket]` above is a Shortcuts magic-variable reference, not literal text. Tap each one and replace with the matching variable.

### Step 9 — Confirm

Add **Show notification**:

- **Title:** `Logged ✅`
- **Body:** `[eventType] with [Chosen Item]`

Optional: **Play Sound** → a short "ting" so you don't have to look.

---

## 2. Put it somewhere fast

- **Home screen widget:** long-press home → Add widget → Shortcuts → Single shortcut → pick `Log friend`. One-tap from home.
- **Lock screen (iOS 16+):** Settings → Wallpaper → Customize → Add widget → Shortcuts. Single tap from lock screen.
- **Back tap:** Settings → Accessibility → Touch → Back Tap → Double Tap → `Log friend`. Now double-tapping the back of your phone logs a meet.
- **Siri:** "Hey Siri, log friend." Works automatically — the Shortcut name is the phrase.

---

## 3. Troubleshooting

| Symptom                                     | Cause                                                               |
| ------------------------------------------- | ------------------------------------------------------------------- |
| `object_not_found` / 404 from query step    | Integration isn't connected to the People DB. Fix in Connections.   |
| `validation_error` on `properties.Type`     | Select option doesn't exist yet in the DB. Add it once in Notion.   |
| Notes show as blank                         | `content` was empty — harmless, or guard with an `If`.              |
| Person picker is empty                      | Your People DB has no rows yet, or wrong `PeopleDB` ID.             |
| `Date` looks wrong                          | Shortcut ran across midnight — switch to `ISO 8601` format action.  |

---

## 4. Nice-to-have extensions (later)

- **Multi-person meets:** change Step 4 to **Choose from list** with "Select multiple" → loop to build a `relation` array.
- **Duration prompt:** add an **Ask for input** (Number) between steps 5 and 6; send as `"Duration": { "number": <n> }`.
- **Location autofill:** **Get current location** → **Get details of location** → name → send as `"Location": { "rich_text": [...] }`.
- **Mood:** extra **Choose from Menu** with emoji options, send as `"Mood": { "select": { "name": "🙂" } }`.
- **Batch log past meets:** second Shortcut `Log past friend` — identical but adds a date picker step instead of `todayISO`.
