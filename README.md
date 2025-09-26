## ⚠️ Partial Readme (Will be updated after draw tool is completed)
---
# <img src="https://github.com/user-attachments/assets/8ed6acde-46c9-43e0-a68c-905bec182234" align="center" width="60" height="60"> QuickEdit - Photo Editor (Compose, Modular, Pluggable)

[![Release](https://img.shields.io/github/v/release/Abizer-R/QuickEdit-Photo-Editor)](https://github.com/Abizer-R/QuickEdit-Photo-Editor/releases)
[![Min SDK](https://img.shields.io/badge/minSdk-24-blue)](#)
[![Target SDK](https://img.shields.io/badge/targetSdk-35-blue)](#)
[![License](https://img.shields.io/badge/license-Apache--2.0-green)](#license)

**QuickEdit** is a modular, pluggable photo editor for Android with a **stable core engine**, a **Compose UI shell**, and **tool plugins** (Crop ✅, Draw/Text/Effects scaffolds).  
It preserves legacy UX (slide-out editor bars → full-screen tools) while cleaning layering, performance, and testability.

- 📱 **Play Store:** [Install](https://play.google.com/store/apps/details?id=com.abizer_r.quickedit)  
- 📦 **Latest APK:** [Releases](https://github.com/Abizer-R/QuickEdit-Photo-Editor/releases)  

<div style="display: flex; flex-wrap: wrap; gap: 10px; align-items: flex-start;">
  <img src="https://github.com/user-attachments/assets/78217ce9-4771-4db0-9dae-345214eb28f1" alt="1" width="200" />
  <img src="https://github.com/user-attachments/assets/8c0214c4-f067-4bed-bac0-73bfd80fc01b" alt="2" width="200" />
  <img src="https://github.com/user-attachments/assets/3619584a-f672-424f-8040-baf9a563f278" alt="3" width="200" />
  <img src="https://github.com/user-attachments/assets/c26f4c38-b0cf-47c5-bd91-aa09df16deea" alt="4" width="200" />
</div>

<div style="display: flex; flex-wrap: wrap; gap: 10px; align-items: flex-start;">
  <img src="https://github.com/user-attachments/assets/91ee2ad6-f4de-4bda-af93-e9b7232d1c00" alt="5" width="200" />
  <img src="https://github.com/user-attachments/assets/5cd3f808-90f4-451f-b4d4-9518acb747d2" alt="6" width="200" />
  <img src="https://github.com/user-attachments/assets/ebd2db8c-43b9-4271-81b9-9800e6277b98" alt="7" width="200" />
  <img src="https://github.com/user-attachments/assets/0d863b8f-b69a-4b3d-a29d-f6b0fc57a8be" alt="8" width="200" />
</div>

## Features

- **Basic Editing Tools**: Crop, Draw, and Apply filters to your photos.
- **Filters**: Apply filter effects using GPUImage.
- **Add Text**: Add text with option to customize fonts and format (bold, italic and more).
- **Smooth Animations**: Enjoy a seamless experience thanks to Jetpack Compose.

---

## Architecture (v0.x)

**Goal:** a reusable editor published as Maven artifacts with a stable engine API and pluggable tools.  
**Status:** engine + shell done; Crop tool delivered; Draw in progress; Effects & Text planned.

## Modules & Dependency Rules

```
app-sample/                 # Host app; exercises the library
quickedit/                  # Legacy façade (keeps old entrypoints stable)
quickedit-core-engine/      # Engine API + default impl (no Compose/UI deps)
quickedit-compose-ui/       # Shell + tool contracts (Compose UI, no tool logic)
quickedit-tool-crop/        # Crop tool (full-screen)
quickedit-tool-draw/        # (ongoing)
quickedit-tool-text/        # (planned)
quickedit-tool-effects/     # (planned)
```

### **Dependency graph (enforced):**
```
app-sample ──► quickedit (legacy)
                ├──► quickedit-compose-ui  ──api──► quickedit-core-engine
                └──► quickedit-tool-*
quickedit-tool-* ──► quickedit-compose-ui
quickedit-core-engine  (Android framework only; no UI)
```

**Why `api(..)` from UI → Engine?** The UI public API mentions engine types (`EditImage`, `SaveFormat`). Re-exporting via `api(project(":quickedit-core-engine"))` makes those types visible to consumers cleanly.


## Tech Stack & Versions (from `libs.versions.toml`)

- **Android:** minSdk 24 · target/compile 35 · JDK 17  
- **Gradle/AGP:** 8.6.1  
- **Kotlin:** 2.1.10 (+ Compose plugin)  
- **Compose BOM:** 2025.05.00  
- **Compose libs:** Material3, Animation `1.7.0-beta04`, UI/Test/Tooling  
- **AndroidX:** Activity Compose 1.9.0 · Lifecycle 2.8.2 · Navigation 2.7.7 · AppCompat 1.7.0 · Core-ktx 1.13.1  
- **DI:** Hilt 2.57.1 (with hilt-navigation-compose 1.2.0)  
- **Coroutines:** 1.10.1  
- **Imaging:** GPUImage 2.1.0 · CanHub Cropper 4.5.0 · Compose-Screenshot 1.0.3  
- **UX Utils:** Cloudy 0.2.7 · ColorPicker 1.0.0  
- **Testing:** JUnit 4.13.2 · AndroidX JUnit 1.2.0 · Espresso 3.6.0

---

## Libraries Used

QuickEdit makes use of the following libraries to provide its features:

- **Jetpack Compose**: A modern toolkit for building native Android UI.
- **Compose Animations**: For smooth and customizable UI animations.
- **[Image Cropper](https://github.com/CanHub/Android-Image-Cropper)**: A cropping library by Canhub that allows users to crop images seamlessly.


# License
```xml
Designed and developed by 2024 Abizer-R (Abizer Rampurawala)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
