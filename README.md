# Fingerprint Unlock Button — Android

A minimal Android app with a single on-screen button that verifies the
user's fingerprint and shows an "Unlocked" state on success.

Works on stock Android **and Samsung phones** — Samsung's fingerprint
sensors (side button on many Galaxy A-series, in-display on S/Note series)
are all exposed through the standard AndroidX `BiometricPrompt` API used
here, so no Samsung-specific SDK is needed.

## What's in this project
- `app/src/main/java/.../MainActivity.kt` — button click handler that
  checks fingerprint availability, opens the system `BiometricPrompt`
  dialog, and handles success/failure/cancel.
- `app/src/main/res/layout/activity_main.xml` — the on-screen fingerprint
  icon button + status text.
- `app/build.gradle` — adds the `androidx.biometric:biometric` dependency.
- `AndroidManifest.xml` — declares the `USE_BIOMETRIC` permission.

## How to run it
1. Open this folder in **Android Studio** (File → Open).
2. Let Gradle sync (it will pull the AndroidX Biometric library).
3. Run on a device or emulator with a fingerprint enrolled:
   - Real device: enroll a fingerprint in Settings → Biometrics.
   - Emulator: create an AVD, then enroll a virtual fingerprint in
     Settings, and use `adb -e emu finger touch <finger_id>` to simulate
     a touch when the prompt appears.
4. Tap the fingerprint button — the system dialog appears, verify, and
   the screen switches to "Unlocked".

## Building an APK via GitHub Actions (no Android Studio needed)
This project includes `.github/workflows/build-apk.yml`, which builds a
debug APK on every push to `main` and uploads it as a downloadable
artifact.

1. Push this whole `FingerprintUnlock` folder as the root of a GitHub
   repo (the `.github`, `app`, `build.gradle`, `settings.gradle` files
   should all sit at the repo's top level).
2. Go to the repo's **Actions** tab — the "Build APK" workflow runs
   automatically. You can also trigger it manually from there
   ("Run workflow").
3. Once it finishes (green check), open that run and scroll to
   **Artifacts** at the bottom — download `app-debug-apk`, which
   contains `app-debug.apk`.
4. Transfer that `.apk` to your Samsung phone (or any Android device)
   and install it — you'll need to allow "Install unknown apps" for
   whichever app you use to open the file (Files, Chrome, etc.), since
   it's not from the Play Store.

## Customizing
- Swap `BIOMETRIC_STRONG` for `BIOMETRIC_STRONG or DEVICE_CREDENTIAL` in
  `MainActivity.kt` if you also want to allow PIN/pattern as a fallback.
- Replace the `// TODO` in `onUnlocked()` with whatever the real unlock
  action should do (navigate, decrypt data, unlock a feature, etc).
- The button's look is defined in `bg_fingerprint_circle.xml` (the
  circle) and `ic_fingerprint.xml` (the icon) — restyle freely.
