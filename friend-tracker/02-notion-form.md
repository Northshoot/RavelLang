# Notion Form — fast event entry from the browser / home screen

Notion has native **Forms** on databases. Good fallback if the iOS Shortcut is overkill or you're on someone else's device.

---

## Build the form

1. Open the **Events** database.
2. Click the view switcher (top-left of the database) → **+ New view** → **Form**.
3. Name it `Log event`.

Configure fields (top to bottom, in order of speed of entry):

| Field     | Required | Default                      | Notes                                                                 |
| --------- | -------- | ---------------------------- | --------------------------------------------------------------------- |
| `Person`  | Yes      | —                            | Notion renders this as a searchable picker against the People DB.     |
| `Type`    | Yes      | —                            | Shows as select buttons.                                              |
| `Date`    | Yes      | **Today** (toggle "default") | Saves a tap in 95% of cases.                                          |
| `Notes`   | No       | —                            | One-line or long-form — whichever you prefer.                         |
| `Mood`    | No       | —                            | Keep it optional; zero friction when you're in a hurry.               |
| `Location`| No       | —                            |                                                                       |
| `Duration`| No       | —                            |                                                                       |

Hide `Name` from the form — leave it blank, or set up a formula later to auto-build it.

## Form settings

- **Who can respond:** `Anyone with the link`. (Or keep it workspace-only if you're paranoid — you'll just need to be logged in on your phone.)
- **After submit:** `Show a success message` → `Logged ✅`. Leave "submit another response" **on** so you can batch-log multiple meets.
- **Limit to one response per person:** off.

## Put it on your iPhone home screen

1. Click **Share form** → copy the public link.
2. On iPhone, open the link in Safari → tap the Share icon → **Add to Home Screen** → name it `Log friend`.
3. You now have a one-tap icon that opens straight to the form.

## When to use the form vs. the Shortcut

| Scenario                                    | Use           |
| ------------------------------------------- | ------------- |
| Sitting down, want to type notes            | Form          |
| Walking out of a coffee, 5-second log       | Shortcut      |
| Someone else's phone / computer             | Form link     |
| Dictating notes on the go                   | Shortcut      |
| Logging 3 meets at once after a busy weekend| Form (resubmit)|

They write to the same Events database, so mix freely.
