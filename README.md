# KinKeep

Fully offline Android app that helps you keep in touch with the people who matter. Phase 1 build: project setup, design system, navigation, encrypted database.

## Get an installable APK (no Android Studio needed)

1. Create a new **empty** repository on GitHub (private is fine).
2. Upload everything from this folder to it, **including the hidden `.github` folder**. Easiest on a computer: open the repo, choose "Add file" > "Upload files", and drag the contents of the unzipped folder in. Then "Commit changes".
3. Open the repo's **Actions** tab. The "Android CI" workflow starts by itself (first run takes about 5-10 minutes).
4. When it finishes (green tick), open the run and download **kinkeep-debug-apk** from the Artifacts section at the bottom. Unzip it to get `app-debug.apk`.
5. Send `app-debug.apk` to your phone (Drive, email, USB). Tap it, and allow "Install unknown apps" for the app you opened it from when asked.

If the run shows a red cross, open the failed step and copy the error text to me; I'll fix it.

## What to verify on the phone (Phase 1)

- The app opens with a dark-green and white look and a bottom bar: Home, People, Settings.
- Home lists the five health categories plus "New", each with an icon, label and description.
- Tap "Add demo person" a few times (debug builds only). The "People tracked" count goes up.
- Open People: the demo people appear, tapping one opens a placeholder detail page with a back arrow.
- Close the app completely and reopen it: the demo people are still there (the encrypted database persists).
- Switch the phone to dark mode: the whole app switches to the dark-green theme.
- Settings > Apps > KinKeep > Permissions shows no permissions at all, and the app has no Internet access.

## Known limits of this build

- Debug-signed test build; not for Play Store.
- Phase 1 only. Contact import, scoring, reminders, widget, backup and app lock come in later phases.
- Android SDK 35 is targeted. Raise it later when needed for Play.
- ktlint/detekt and warnings-as-errors are added in Phase 6.
