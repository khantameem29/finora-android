# Finora — Android App

Android app for **Finora**, my AI-powered finance habit tracker. A lightweight WebView wrapper that opens the live web app full-screen, so it can be installed and used like a native app.

**Web app:** https://finora-app.netlify.app
**Download APK:** [`apk/Finora.apk`](apk/Finora.apk)

## Features

- Full-screen app experience with themed status and navigation bars
- Hardware back button navigates inside the app
- File uploads supported
- Friendly offline screen with a retry button
- Minimum Android 7.0 (API 24)

## Install

Download the APK on an Android phone, open it, and allow "Install unknown apps" when prompted. Requires an internet connection (the app loads the live site).

## Build from source

Plain Java + Android SDK command-line tools, no Gradle:

```bash
KS=release.keystore KS_PASS=yourpass KS_ALIAS=youralias ./build.sh
```

---
Tameem Khan · [Portfolio](https://khantameem29.github.io) · [GitHub](https://github.com/khantameem29) · ktameem2004@gmail.com
