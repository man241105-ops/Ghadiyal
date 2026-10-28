# ગુજરાતી ઘડિયાળ — Gujarati Clock + Home-Screen Widget (Android)

Shows the weekday in Gujarati (સોમવાર, મંગળવાર ...), the time in Gujarati digits (૧૦:૩૦),
the period of day (સવારે / બપોરે / સાંજે / રાત્રે) and the date (૨૮ ઓક્ટોબર ૨૦૨૬).

Customizable: 6 colour themes, text size, background opacity, show/hide date. Resizable widget.

(Gujarati version of these instructions: see **README_ગુજરાતી.txt**)

## What is in this folder
- `app/`                  Android app + widget source code
- `.github/workflows/`    Online build recipe (builds the APK automatically)
- `BUILD_WORKFLOW_COPY/`  Same recipe, visible copy (use if `.github` does not upload)
- `web-version/`          Bonus: simple web version (open gujarati_clock.html in Chrome)

## Get the APK (no Android Studio, free, about 10 minutes)
1. Create a free account at https://github.com
2. Click **+ → New repository**. Name: `GujaratiClock`. Choose **Public**. Click **Create repository**.
3. Click **uploading an existing file**. Unzip this zip on your computer first, then drag ALL files and
   folders from inside `GujaratiClock` (including `.github`, `app`, `build.gradle`, `settings.gradle`,
   `gradle.properties`) into the page. Click **Commit changes**.
   - If `.github` did not upload: click **Add file → Create new file**, type the name
     `.github/workflows/build.yml`, paste the contents of `BUILD_WORKFLOW_COPY/build.yml`, then commit.
4. Click the **Actions** tab. Wait 3-5 minutes until **Build APK** shows a green tick.
   (If it asks, click "I understand my workflows, go ahead and enable them".)
5. Click the finished run → scroll to **Artifacts** → download **GujaratiClock-APK**.
6. Unzip it. You get `app-debug.apk`.

## Install on the phone
1. Send `app-debug.apk` to the phone (WhatsApp, Google Drive, or USB cable).
2. Tap the file. If asked, allow **Install unknown apps** for that app, then Install.
3. Open **ગુજરાતી ઘડિયાળ**, pick a theme / size, then tap **Add widget** and confirm.
   Or: long-press the home screen → **Widgets** → **ગુજરાતી ઘડિયાળ**.
4. Tap the widget any time to open settings. Drag its corners to resize.

## Notes
- Works on Android 7.0 (API 24) and newer. No internet needed.
- The widget updates every minute. When the phone is asleep it may lag a little and catches up when the screen turns on.
- Some phones (Xiaomi, Oppo, Vivo, Samsung) stop background updates to save battery. If the time looks stuck,
  open Settings → Apps → ગુજરાતી ઘડિયાળ → Battery → set to **Unrestricted / No restrictions**.
- If the build shows a red cross, open the failed run, copy the red error text and ask for a fix.

## Change things later
- Colour themes: `MainActivity.java` (array `P`).
- Gujarati names / format: `Gu.java`.
- Widget design: `app/src/main/res/layout/widget.xml`.
