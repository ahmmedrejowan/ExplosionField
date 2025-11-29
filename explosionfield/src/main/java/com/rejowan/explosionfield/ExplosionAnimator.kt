package com.rejowan.explosionfield

import android.animation.ValueAnimator
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import kotlin.random.Random

/**
 * Animator that controls the explosion particle effect.
 * Extends ValueAnimator to leverage Android's animation framework.
 *
 * @property container The view that will be invalidated for redraws
 * @property bitmap Source bitmap to sample particle colors from
 * @property bounds Bounds of the explosion area
 * @property config Configuration for the explosion animation
 */
class ExplosionAnimator(
    private val container: View,
    bitmap: Bitmap,
    private val bounds: Rect,
    private val config: ExplosionConfig = ExplosionConfig.DEFAULT
) : ValueAnimator() {

    private val paint = Paint()
    private val particles: Array<Particle>
    private val endValue = 1.4f

    init {
        // Generate particles based on configuration
        particles = generateParticles(bitmap)

        // Configure animator
        setFloatValues(0f, endValue)
        interpolator = config.interpolator
        duration = config.duration
    }

    /**
     * Generates particles by sampling the bitmap and applying physics.
     */
    private fun generateParticles(bitmap: Bitmap): Array<Particle> {
        val random = Random(System.currentTimeMillis())
        val gridSize = config.particleCount.gridSize
        val particleArray = Array(gridSize * gridSize) { index ->
            val i = index / gridSize
            val j = index % gridSize

            // Calculate sampling position (skip edges)
            val w = bitmap.width / (gridSize + 2)
            val h = bitmap.height / (gridSize + 2)

            // Sample color from bitmap
            val x = ((j + 1) * w).coerceIn(0, bitmap.width - 1)
            val y = ((i + 1) * h).coerceIn(0, bitmap.height - 1)
            val sampledColor = bitmap.getPixel(x, y)

            // Process color based on config
            val processedColor = ColorProcessor.processColor(
                sampledColor,
                config.colorMode,
                config.tintColor
            )

            // Generate particle with physics
            val physicsEngine = PhysicsEngineFactory.create(config.style)
            val particle = physicsEngine.generateParticle(random, bounds, config)

            // Set the color
            particle.copy(color = processedColor)
        }

        return particleArray
    }

    /**
     * Draws all particles to the canvas.
     *
     * @param canvas Canvas to draw on
     * @return true if animation is running, false otherwise
     */
    fun draw(canvas: Canvas): Boolean {
        if (!isStarted) {
            return false
        }

        val currentValue = animatedValue as Float

        // Update and draw each particle
        for (particle in particles) {
            particle.advance(currentValue, endValue)

            if (particle.alpha > 0f) {
                paint.color = particle.color
                paint.alpha = (Color.alpha(particle.color) * particle.alpha).toInt()
                canvas.drawCircle(particle.cx, particle.cy, particle.radius, paint)
            }
        }

        // Request next frame
        container.invalidate()
        return true
    }

    override fun start() {
        super.start()
        container.invalidate(bounds)
    }

    companion object {
        /** Default animation duration in milliseconds */
        const val DEFAULT_DURATION = 1024L
    }
}
