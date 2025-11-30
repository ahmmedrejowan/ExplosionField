package com.rejowan.explosionfield

import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator

/**
 * Configuration for explosion animations.
 *
 * @property duration Total animation duration in milliseconds
 * @property interpolator Animation timing interpolator
 * @property particleCount Particle density (affects grid size)
 * @property particleSize Particle size variation
 * @property expandBounds Additional space around explosion bounds (in dp)
 * @property shakeBeforeExplode Whether to shake the view before exploding
 * @property shakeDuration Duration of shake animation in milliseconds
 * @property shakeIntensity Shake intensity factor (0.0 to 1.0)
 * @property colorMode How to process particle colors
 * @property tintColor Color to use when colorMode is TINTED
 * @property fadeOutStart Progress value (0-1) when particles start fading out
 * @property style Physics style for particle motion
 * @property removeViewAfterExplosion Whether to remove the view from its parent after explosion
 * @property hapticFeedback Whether to trigger haptic feedback on explosion
 */
data class ExplosionConfig(
    // Animation timing
    val duration: Long = DEFAULT_DURATION,
    val interpolator: Interpolator = DEFAULT_INTERPOLATOR,

    // Particle properties
    val particleCount: ParticleCount = ParticleCount.MEDIUM,
    val particleSize: ParticleSize = ParticleSize.MIXED,

    // Explosion bounds
    val expandBounds: Int = 32,

    // Pre-explosion effects
    val shakeBeforeExplode: Boolean = true,
    val shakeDuration: Long = 150L,
    val shakeIntensity: Float = 0.05f,

    // Color customization
    val colorMode: ColorMode = ColorMode.ORIGINAL,
    val tintColor: Int = 0xFF000000.toInt(),

    // Animation behavior
    val fadeOutStart: Float = 0.7f,
    val style: ExplosionStyle = ExplosionStyle.FOUNTAIN,

    // Post-explosion
    val removeViewAfterExplosion: Boolean = false,
    val hapticFeedback: Boolean = false
) {

    /**
     * Particle density options.
     *
     * @property gridSize Number of particles per row/column
     */
    enum class ParticleCount(val gridSize: Int) {
        /** 100 particles - Faster performance, less detail */
        LOW(10),

        /** 225 particles - Balanced (original behavior) */
        MEDIUM(15),

        /** 400 particles - More detail, slower performance */
        HIGH(20)
    }

    /**
     * Particle size variation options.
     */
    enum class ParticleSize {
        /** Very small particles (1-2dp) */
        TINY,

        /** Small particles (2-3dp) */
        SMALL,

        /** Mixed sizes (1-5dp, original behavior) */
        MIXED,

        /** Large particles (4-6dp) */
        LARGE
    }

    /**
     * Color processing modes.
     */
    enum class ColorMode {
        /** Use original view colors (sampled from bitmap) */
        ORIGINAL,

        /** Convert to grayscale */
        GRAYSCALE,

        /** Apply a color tint */
        TINTED,

        /** Use random bright colors */
        RANDOM
    }

    /**
     * Explosion animation styles with different physics behaviors.
     */
    enum class ExplosionStyle {
        /** Original behavior - particles shoot up then fall down in parabolic arc */
        FOUNTAIN,

        /** Particles explode radially outward in all 360° directions */
        SCATTER,

        /** Particles fall downward like rain or shattered glass */
        FALL,

        /** Particles spiral outward in a rotating vortex motion */
        VORTEX
    }

    companion object {
        /** Default animation duration (1024ms) */
        const val DEFAULT_DURATION = 1024L

        /** Default interpolator */
        val DEFAULT_INTERPOLATOR: Interpolator = AccelerateInterpolator(0.6f)

        /** Default configuration - matches original library behavior */
        val DEFAULT = ExplosionConfig()

        /**
         * Gentle explosion with slower animation and less intensity.
         */
        val GENTLE = ExplosionConfig(
            duration = 1500L,
            particleCount = ParticleCount.LOW,
            interpolator = DecelerateInterpolator(),
            shakeIntensity = 0.02f
        )

        /**
         * Aggressive explosion with fast animation and high intensity.
         */
        val AGGRESSIVE = ExplosionConfig(
            duration = 800L,
            particleCount = ParticleCount.HIGH,
            interpolator = AccelerateInterpolator(0.8f),
            shakeIntensity = 0.1f
        )

        /**
         * Dust-like effect with many tiny particles.
         */
        val DUST = ExplosionConfig(
            particleCount = ParticleCount.HIGH,
            particleSize = ParticleSize.TINY,
            duration = 1200L
        )

        /**
         * Chunky explosion with fewer, larger particles.
         */
        val CHUNKY = ExplosionConfig(
            particleCount = ParticleCount.LOW,
            particleSize = ParticleSize.LARGE,
            duration = 900L
        )
    }
}
