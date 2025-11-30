package com.rejowan.explosionfield

import android.util.Log
import kotlin.math.cos
import kotlin.math.sin

/**
 * Represents a single particle in an explosion animation.
 *
 * @property color The ARGB color value of this particle
 * @property baseCx The initial horizontal center position
 * @property baseCy The initial vertical center position
 * @property baseRadius The target radius this particle will grow to
 * @property top Maximum height the particle will reach
 * @property bottom Maximum distance the particle will fall
 * @property mag Initial upward velocity magnitude
 * @property neg Gravity coefficient (negative acceleration)
 * @property life Delay before particle appears (normalized 0-1)
 * @property overflow Early fade-out factor (normalized 0-1)
 */
data class Particle(
    // Immutable properties (set once during creation)
    val color: Int,
    val baseCx: Float,
    val baseCy: Float,
    val baseRadius: Float,
    val top: Float,
    val bottom: Float,
    val mag: Float,
    val neg: Float,
    val life: Float,
    val overflow: Float,

    // Mutable state (updated during animation)
    var cx: Float = baseCx,
    var cy: Float = baseCy,
    var radius: Float = 0f,
    var alpha: Float = 1f
) {

    companion object {
        private const val TAG = "Particle"
        private var vortexLogCount = 0
    }

    /**
     * Updates the particle's position, size, and alpha based on animation progress.
     *
     * @param factor Current animation progress value (0 to END_VALUE)
     * @param endValue Maximum animation progress value
     */
    fun advance(factor: Float, endValue: Float) {
        // Normalize progress to 0-1 range
        val normalization = factor / endValue

        // Check if particle should be visible based on lifecycle
        if (normalization < life || normalization > 1f - overflow) {
            alpha = 0f
            return
        }

        // Adjust normalization for particle's lifecycle window
        val adjustedProgress = (normalization - life) / (1f - life - overflow)
        val scaledProgress = adjustedProgress * endValue

        // Calculate fade-out alpha (starts fading at 70% progress)
        val fadeOutStart = 0.7f
        alpha = if (adjustedProgress >= fadeOutStart) {
            val fadeProgress = (adjustedProgress - fadeOutStart) / (1f - fadeOutStart)
            1f - fadeProgress
        } else {
            1f
        }

        // Update position based on explosion style
        when {
            // SCATTER: Linear radial motion (mag=0, neg=0)
            mag == 0f && neg == 0f -> {
                val distance = bottom * scaledProgress
                cx = baseCx + distance
                cy = baseCy - (top * scaledProgress)
            }
            // FALL: Downward motion with gravity (neg=0, mag>0)
            neg == 0f && mag > 0f -> {
                val distance = bottom * scaledProgress
                cx = baseCx + distance
                // cy = baseCy + (initial_velocity * time + 0.5 * gravity * time²)
                val fallDistance = (top * scaledProgress) + (0.5f * mag * scaledProgress * scaledProgress)
                cy = baseCy + fallDistance
            }
            // VORTEX: Spiral motion (neg=-1.0)
            neg == -1.0f -> {
                // bottom = start angle, top = rotation amount, mag = final radius
                val currentAngle = bottom + (top * adjustedProgress)
                val currentRadius = mag * scaledProgress

                cx = baseCx + (cos(currentAngle) * currentRadius)
                cy = baseCy + (sin(currentAngle) * currentRadius)

                if (vortexLogCount < 5 && factor > 0.01f && factor < 0.02f) {
                    vortexLogCount++
                    Log.d(TAG, "VORTEX: angle=${Math.toDegrees(currentAngle.toDouble()).toInt()}°, radius=$currentRadius")
                    Log.d(TAG, "  pos: ($cx, $cy) from center ($baseCx, $baseCy)")
                }
            }
            // FOUNTAIN: Parabolic motion (original behavior)
            else -> {
                val distance = bottom * scaledProgress
                cx = baseCx + distance
                cy = baseCy - (neg * distance * distance) - (distance * mag)
            }
        }

        // Update particle size - start at full size, don't grow from 0
        // For scatter effect, particles should be visible immediately
        radius = baseRadius
    }
}
