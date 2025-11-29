package com.rejowan.explosionfield

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

        // Calculate horizontal distance traveled
        val distance = bottom * scaledProgress

        // Update horizontal position (linear motion)
        cx = baseCx + distance

        // Update vertical position (parabolic motion with gravity)
        // Optimized: removed Math.pow(), using simple multiplication
        cy = baseCy - (neg * distance * distance) - (distance * mag)

        // Update particle size (grows over time)
        radius = baseRadius * scaledProgress
    }
}
