#!/usr/bin/env bash
# Builds the Finora APK with the Android SDK command-line tools (no Gradle).
# Requires: ANDROID_HOME set, build-tools 36.x, platform android-36, JDK 17+.
# Usage: KS=path/to/keystore KS_PASS=secret KS_ALIAS=alias ./build.sh
set -euo pipefail
BT="$ANDROID_HOME/build-tools/$(ls "$ANDROID_HOME/build-tools" | sort -V | tail -1)"
JAR="$ANDROID_HOME/platforms/android-36/android.jar"
rm -rf build; mkdir -p build/gen build/classes build/dex
"$BT/aapt2" compile --dir res -o build/res.zip
"$BT/aapt2" link -I "$JAR" --manifest AndroidManifest.xml -o build/base.apk --java build/gen build/res.zip
javac -source 11 -target 11 -classpath "$JAR" -d build/classes $(find src build/gen -name '*.java')
"$BT/d8" --release --min-api 24 --lib "$JAR" --output build/dex $(find build/classes -name '*.class')
cp build/base.apk build/unsigned.apk && (cd build/dex && "$BT/aapt" add ../unsigned.apk classes.dex)
"$BT/zipalign" -f -p 4 build/unsigned.apk build/aligned.apk
"$BT/apksigner" sign --ks "$KS" --ks-pass "pass:$KS_PASS" --ks-key-alias "$KS_ALIAS" --out apk/Finora.apk build/aligned.apk
