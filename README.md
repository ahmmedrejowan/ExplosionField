<p align="center"><img src="https://raw.githubusercontent.com/ahmmedrejowan/ExplosionField/main/files/logo.png" width="100px" align="center"/></p>
<h1 align="center">ExplosionField</h1> 
<h3 align="center"><b>A powerful and customizable Android explosion animation library with multiple explosion styles</b></h3>

<p align="center"> <a href="https://www.android.com"><img src="https://img.shields.io/badge/platform-Android-yellow.svg" alt="platform"></a>
 <a href="https://android-arsenal.com/api?level=21"><img src="https://img.shields.io/badge/API-24%2B-brightgreen.svg?style=flat" alt="API"></a> <a href="https://jitpack.io/#ahmmedrejowan/ExplosionField/"><img src="https://jitpack.io/v/ahmmedrejowan/ExplosionField.svg" alt="JitPack"></a> <a href="https://github.com/ahmmedrejowan/ExplosionField/blob/main/LICENSE"><img src="https://img.shields.io/github/license/ahmmedrejowan/ExplosionField" alt="GitHub license"></a> </p>

 <p align="center"> <a href="https://github.com/ahmmedrejowan/ExplosionField/issues"><img src="https://img.shields.io/github/issues/ahmmedrejowan/ExplosionField" alt="GitHub issues"></a> <a href="https://github.com/ahmmedrejowan/ExplosionField/network"><img src="https://img.shields.io/github/forks/ahmmedrejowan/ExplosionField" alt="GitHub forks"></a> <a href="https://github.com/ahmmedrejowan/ExplosionField/stargazers"><img src="https://img.shields.io/github/stars/ahmmedrejowan/ExplosionField" alt="GitHub stars"></a> <a href="https://github.com/ahmmedrejowan/ExplosionField/graphs/contributors"> <img src="https://img.shields.io/github/contributors/ahmmedrejowan/ExplosionField" alt="GitHub contributors"></a>   </p>

## Table of Contents

- [Purpose](#purpose)
- [Features](#features)
- [Demo](#demo)
- [Prerequisites](#prerequisites)
- [Dependency](#dependency)
- [Usage](#usage)
- [Customization](#customization)
- [Attributes](#attribute)
- [Notes](#notes)
- [Contribute](#contribute)
- [License](#license)

## Purpose

This library is a modern recreation of the original [ExplosionField](https://github.com/tyrantgit/ExplosionField) by tyrantgit, which was created over 10 years ago and has not been updated for a long time. As a developer who started with Android, I really loved this library, seeing it become outdated truly hurt me.

That's why I've recreated it from scratch with:
- **Modern Android features** and best practices
- **Written in Kotlin** for better performance and code safety
- **Enhanced physics engine** with multiple explosion styles
- **Extensive customization options** and flexible API
- **Active maintenance** with more improvements coming soon

This is my tribute to a library that inspired countless developers, now brought back to life for modern Android development.

## Features

- ** 4 Explosion Styles**: Fountain (parabolic), Scatter (radial), Fall (gravity), and Vortex (spiral)
- **️ Flexible Configuration**: Customize particle count, size, duration, colors, and physics behavior
- ** Color Modes**: Original colors, grayscale, random, or custom tint
- ** Built-in Presets**: Gentle, Aggressive, Dust, and Chunky ready-to-use configurations
- ** Advanced Physics**: Realistic particle motion with gravity, velocity, and rotation
- ** Easy Integration**: Simple one-line implementation with fluent API
- ** Lifecycle Aware**: Automatic cleanup and memory management
- ** View Targeting**: Explode any Android View with pixel-perfect particle generation
- ** High Performance**: Optimized rendering with minimal overhead
- **️ Highly Customizable**: Fine-tune every aspect of the explosion animation

## Demo

| Gif                                                                                            | Gif                                                                                            | 
|------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------|
| ![Shot1](https://raw.githubusercontent.com/ahmmedrejowan/ExplosionField/main/files/demo_1.gif) | ![Shot1](https://raw.githubusercontent.com/ahmmedrejowan/ExplosionField/main/files/demo_2.gif) |


| Shots                                                                                          | Shots                                                                                          |
|------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------|
| ![Shot1](https://raw.githubusercontent.com/ahmmedrejowan/ExplosionField/main/files/shot_1.jpg) | ![Shot1](https://raw.githubusercontent.com/ahmmedrejowan/ExplosionField/main/files/shot_2.jpg) |

### Run Locally

Want to test the library locally? Follow these steps:

**1. Clone the Repository:**
```bash
git clone https://github.com/ahmmedrejowan/ExplosionField.git
cd ExplosionField
```

**2. Open in Android Studio**

**3. Build the Project**

**4. Run the Demo App**

**5. Test Different Features**

## Prerequisites

### Kotlin DSL

``` Kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven{
            url = uri("https://jitpack.io")
        }
    }
}
```


## Dependency

Add this to your module's `build.gradle.kts` file (latest
version <a href="https://jitpack.io/#ahmmedrejowan/ExplosionField"><img src="https://jitpack.io/v/ahmmedrejowan/ExplosionField.svg" alt="JitPack"></a>):

``` kotlin
implementation("com.github.ahmmedrejowan:ExplosionField:1.0.0")
```
## Usage

### Basic Usage

**Kotlin:**
```kotlin
// Attach explosion field to your activity
val explosionField = ExplosionField.attach(this)

// Explode a view with default configuration
explosionField.explode(view)
```

**Java:**
```java
// Attach explosion field to your activity
ExplosionField explosionField = ExplosionField.attach(this);

// Explode a view with default configuration
explosionField.explode(view);
```

### Using Built-in Presets

**Kotlin:**
```kotlin
val explosionField = ExplosionField.attach(this)

// Gentle explosion
explosionField.explode(view, ExplosionConfig.GENTLE)

// Aggressive explosion
explosionField.explode(view, ExplosionConfig.AGGRESSIVE)

// Dust particles
explosionField.explode(view, ExplosionConfig.DUST)

// Chunky particles
explosionField.explode(view, ExplosionConfig.CHUNKY)
```

**Java:**
```java
ExplosionField explosionField = ExplosionField.attach(this);

// Gentle explosion
explosionField.explode(view, ExplosionConfig.GENTLE);

// Aggressive explosion
explosionField.explode(view, ExplosionConfig.AGGRESSIVE);
```

### Using Different Explosion Styles

**Kotlin:**
```kotlin
val explosionField = ExplosionField.attach(this)

// Fountain style (parabolic arc motion)
explosionField.explode(view, ExplosionConfig(
    style = ExplosionConfig.ExplosionStyle.FOUNTAIN
))

// Scatter style (360° radial expansion)
explosionField.explode(view, ExplosionConfig(
    style = ExplosionConfig.ExplosionStyle.SCATTER
))

// Fall style (gravity-based falling)
explosionField.explode(view, ExplosionConfig(
    style = ExplosionConfig.ExplosionStyle.FALL
))

// Vortex style (spiral rotation)
explosionField.explode(view, ExplosionConfig(
    style = ExplosionConfig.ExplosionStyle.VORTEX
))
```

**Java:**
```java
ExplosionField explosionField = ExplosionField.attach(this);

// Fountain style
explosionField.explode(view, new ExplosionConfig.Builder()
    .setStyle(ExplosionConfig.ExplosionStyle.FOUNTAIN)
    .build());

// Scatter style
explosionField.explode(view, new ExplosionConfig.Builder()
    .setStyle(ExplosionConfig.ExplosionStyle.SCATTER)
    .build());
```

### Explosion Listener

**Kotlin:**
```kotlin
explosionField.explosionListener = object : ExplosionField.OnExplosionListener {
    override fun onExplosionStart(view: View) {
        Log.d("Explosion", "Started for: ${view.javaClass.simpleName}")
    }

    override fun onExplosionEnd(view: View) {
        Log.d("Explosion", "Ended for: ${view.javaClass.simpleName}")
    }
}
```

**Java:**
```java
explosionField.setExplosionListener(new ExplosionField.OnExplosionListener() {
    @Override
    public void onExplosionStart(@NonNull View view) {
        Log.d("Explosion", "Started for: " + view.getClass().getSimpleName());
    }

    @Override
    public void onExplosionEnd(@NonNull View view) {
        Log.d("Explosion", "Ended for: " + view.getClass().getSimpleName());
    }
});
```

## Customization

### Custom Configuration

**Kotlin:**
```kotlin
val config = ExplosionConfig(
    // Explosion style
    style = ExplosionConfig.ExplosionStyle.SCATTER,

    // Particle count (LOW, MEDIUM, HIGH)
    particleCount = ExplosionConfig.ParticleCount.HIGH,

    // Particle size (TINY, SMALL, MEDIUM, LARGE, MIXED)
    particleSize = ExplosionConfig.ParticleSize.MIXED,

    // Duration in milliseconds
    duration = 1500L,

    // Color mode (ORIGINAL, GRAYSCALE, RANDOM, TINTED)
    colorMode = ExplosionConfig.ColorMode.RANDOM,

    // Custom tint color (only used with ColorMode.TINTED)
    tintColor = Color.parseColor("#FF6B6B"),

    // Shake before explode
    shakeBeforeExplode = true,

    // Shake intensity (0.0 to 1.0)
    shakeIntensity = 0.05f,

    // Haptic feedback
    hapticFeedback = true
)

explosionField.explode(view, config)
```

**Java:**
```java
ExplosionConfig config = new ExplosionConfig.Builder()
    .setStyle(ExplosionConfig.ExplosionStyle.SCATTER)
    .setParticleCount(ExplosionConfig.ParticleCount.HIGH)
    .setParticleSize(ExplosionConfig.ParticleSize.MIXED)
    .setDuration(1500L)
    .setColorMode(ExplosionConfig.ColorMode.RANDOM)
    .setTintColor(Color.parseColor("#FF6B6B"))
    .setShakeBeforeExplode(true)
    .setShakeIntensity(0.05f)
    .setHapticFeedback(true)
    .build();

explosionField.explode(view, config);
```

### Advanced Examples

**Grayscale Scatter Explosion:**
```kotlin
explosionField.explode(view, ExplosionConfig(
    style = ExplosionConfig.ExplosionStyle.SCATTER,
    colorMode = ExplosionConfig.ColorMode.GRAYSCALE,
    particleCount = ExplosionConfig.ParticleCount.HIGH,
    duration = 1400L
))
```

**Fast Fountain with Haptic:**
```kotlin
explosionField.explode(view, ExplosionConfig(
    style = ExplosionConfig.ExplosionStyle.FOUNTAIN,
    duration = 500L,
    hapticFeedback = true,
    shakeIntensity = 0.15f
))
```

**Slow Vortex with Red Tint:**
```kotlin
explosionField.explode(view, ExplosionConfig(
    style = ExplosionConfig.ExplosionStyle.VORTEX,
    colorMode = ExplosionConfig.ColorMode.TINTED,
    tintColor = Color.RED,
    duration = 2000L,
    particleSize = ExplosionConfig.ParticleSize.LARGE
))
```

## Attributes

### ExplosionConfig Parameters

| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| `style` | `ExplosionStyle` | `FOUNTAIN` | Explosion animation style (FOUNTAIN, SCATTER, FALL, VORTEX) |
| `particleCount` | `ParticleCount` | `MEDIUM` | Number of particles (LOW, MEDIUM, HIGH) |
| `particleSize` | `ParticleSize` | `MEDIUM` | Size of particles (TINY, SMALL, MEDIUM, LARGE, MIXED) |
| `duration` | `Long` | `1500L` | Animation duration in milliseconds |
| `colorMode` | `ColorMode` | `ORIGINAL` | Particle coloring mode (ORIGINAL, GRAYSCALE, RANDOM, TINTED) |
| `tintColor` | `Int` | `null` | Custom tint color (used only with TINTED mode) |
| `shakeBeforeExplode` | `Boolean` | `true` | Enable shake animation before explosion |
| `shakeIntensity` | `Float` | `0.05f` | Shake intensity (0.0 to 1.0) |
| `hapticFeedback` | `Boolean` | `false` | Enable haptic vibration on explosion |

### Explosion Styles

| Style | Description | Use Case |
|-------|-------------|----------|
| `FOUNTAIN` | Parabolic arc motion with gravity | Classic explosion effect, game over screens |
| `SCATTER` | 360° radial expansion outward | Button clicks, item removal |
| `FALL` | Gravity-based falling particles | Dropping items, deletion effects |
| `VORTEX` | Spiral rotation motion | Special effects, transitions |

### Particle Count

| Count | Approximate Particles | Performance |
|-------|----------------------|-------------|
| `LOW` | 20-40 | Best |
| `MEDIUM` | 40-60 | Good |
| `HIGH` | 60-100 | Moderate |

### Particle Size

| Size | Description | Visual Effect |
|------|-------------|---------------|
| `TINY` | Very small particles | Dust, smoke effect |
| `SMALL` | Small particles | Fine debris |
| `MEDIUM` | Medium particles | Balanced appearance |
| `LARGE` | Large particles | Chunky, bold effect |
| `MIXED` | Random sizes | Natural, varied look |

### Color Modes

| Mode | Description |
|------|-------------|
| `ORIGINAL` | Uses original view colors |
| `GRAYSCALE` | Converts to grayscale |
| `RANDOM` | Random vibrant colors |
| `TINTED` | Applies custom tint color |

### Built-in Presets

```kotlin
// Gentle - Soft, slow explosion
ExplosionConfig.GENTLE

// Aggressive - Fast, intense explosion
ExplosionConfig.AGGRESSIVE

// Dust - Many tiny particles
ExplosionConfig.DUST

// Chunky - Large, bold particles
ExplosionConfig.CHUNKY
```

## Notes

- **Performance**: For best performance, use `ParticleCount.LOW` or `MEDIUM` for list items
- **Memory**: The library automatically cleans up animations and releases resources
- **Threading**: All animations run on the main thread using Android's ValueAnimator
- **View Recycling**: Safe to use with RecyclerView and ListView
- **API Level**: Minimum SDK 24 (Android 7.0 Nougat)
- **Lifecycle**: ExplosionField is lifecycle-aware and cleans up when the activity is destroyed

## Inspiration and Credit

This library is inspired by and pays homage to the original [ExplosionField](https://github.com/tyrantgit/ExplosionField) by [@tyrantgit](https://github.com/tyrantgit). The original library was a pioneering work in Android animations that inspired countless developers and projects.

Special thanks to:
- **tyrantgit** - For creating the original ExplosionField library that started it all
- The Android developer community - For keeping the spirit of creative animations alive
- All contributors and users of this library

## Contribute

Please fork this repository and contribute back
using [pull requests](https://github.com/ahmmedrejowan/ExplosionField/pulls).

Any contributions, large or small, major features, bug fixes, are welcomed and appreciated.

Let me know which features you want in the future in `Request Feature` tab.

If this project helps you a little bit, then give a to Star ⭐ the Repo.

## License

* [Apache Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)

```
Copyright 2025 ahmmedrejowan

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
