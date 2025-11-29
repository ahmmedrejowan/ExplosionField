package com.rejowan.explosionfield

import android.graphics.Color
import kotlin.random.Random

/**
 * Processes and manipulates colors for particle effects.
 */
object ColorProcessor {

    /**
     * Processes a color based on the specified color mode.
     *
     * @param color Original color value
     * @param mode Color processing mode
     * @param tintColor Tint color (used when mode is TINTED)
     * @return Processed color value
     */
    fun processColor(
        color: Int,
        mode: ExplosionConfig.ColorMode,
        tintColor: Int = 0xFF000000.toInt()
    ): Int {
        return when (mode) {
            ExplosionConfig.ColorMode.ORIGINAL -> color
            ExplosionConfig.ColorMode.GRAYSCALE -> toGrayscale(color)
            ExplosionConfig.ColorMode.TINTED -> applyTint(color, tintColor)
            ExplosionConfig.ColorMode.RANDOM -> randomBrightColor()
        }
    }

    /**
     * Converts a color to grayscale using luminosity method.
     * Uses standard coefficients: R=0.299, G=0.587, B=0.114
     *
     * @param color Original color
     * @return Grayscale version of the color
     */
    private fun toGrayscale(color: Int): Int {
        val red = Color.red(color)
        val green = Color.green(color)
        val blue = Color.blue(color)
        val alpha = Color.alpha(color)

        // Calculate luminosity
        val gray = (red * 0.299 + green * 0.587 + blue * 0.114).toInt()

        return Color.argb(alpha, gray, gray, gray)
    }

    /**
     * Applies a tint to the color by blending it with the tint color.
     * Uses 50% blend factor for balanced results.
     *
     * @param color Original color
     * @param tintColor Color to tint with
     * @return Tinted color
     */
    private fun applyTint(color: Int, tintColor: Int): Int {
        val blendFactor = 0.5f

        val red = Color.red(color)
        val green = Color.green(color)
        val blue = Color.blue(color)
        val alpha = Color.alpha(color)

        val tintRed = Color.red(tintColor)
        val tintGreen = Color.green(tintColor)
        val tintBlue = Color.blue(tintColor)

        // Blend colors
        val blendedRed = (red * (1 - blendFactor) + tintRed * blendFactor).toInt()
        val blendedGreen = (green * (1 - blendFactor) + tintGreen * blendFactor).toInt()
        val blendedBlue = (blue * (1 - blendFactor) + tintBlue * blendFactor).toInt()

        return Color.argb(alpha, blendedRed, blendedGreen, blendedBlue)
    }

    /**
     * Generates a random bright color with high saturation and value.
     *
     * @return Random bright color
     */
    private fun randomBrightColor(): Int {
        val hue = Random.nextFloat() * 360f  // Random hue (0-360)
        val saturation = 0.6f + Random.nextFloat() * 0.4f  // 60-100% saturation
        val value = 0.8f + Random.nextFloat() * 0.2f  // 80-100% value

        return Color.HSVToColor(floatArrayOf(hue, saturation, value))
    }
}
