# Conway Life 3D

Android app for Conway's Game of Life on a 48×48 toroidal grid, rendered as rotatable pseudo-3D cubes with Jetpack Compose.

Features: Start/Pause, Step, Clear, Glider, Random, speed control and drag camera rotation/tilt.

## Build

GitHub Actions → Android debug APK → run workflow or push. Download `conway-life-3d-debug-apk` artifact and install `app-debug.apk`.

Locally use JDK 17, Android SDK 35 and Gradle 8.13: `gradle testDebugUnitTest assembleDebug`.

This is a 2D B3/S23 cellular automaton with 3D-style rendering, not a 48³ automaton.
