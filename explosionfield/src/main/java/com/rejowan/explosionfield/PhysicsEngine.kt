package com.rejowan.explosionfield

import android.graphics.Rect
import android.util.Log
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

/**
 * Physics engine interface for different explosion styles.
 */
interface PhysicsEngine {
    /**
     * Generates physics properties for a particle.
     *
     * @param random Random number generator
     * @param bounds Explosion bounds
     * @param config Explosion configuration
     * @return Particle with initialized physics properties
     */
    fun generateParticle(
        random: Random,
        bounds: Rect,
        config: ExplosionConfig
    ): Particle
}

/**
 * Factory for creating physics engines based on explosion style.
 */
object PhysicsEngineFactory {
    fun create(style: ExplosionConfig.ExplosionStyle): PhysicsEngine {
        return when (style) {
            ExplosionConfig.ExplosionStyle.FOUNTAIN -> FountainPhysics()
            ExplosionConfig.ExplosionStyle.SCATTER -> ScatterPhysics()
            ExplosionConfig.ExplosionStyle.FALL -> FallPhysics()
            ExplosionConfig.ExplosionStyle.VORTEX -> VortexPhysics()
        }
    }
}

/**
 * Original fountain-style physics - particles shoot up then fall down.
 * Matches the behavior of the original ExplosionField library.
 */
class FountainPhysics : PhysicsEngine {

    override fun generateParticle(
        random: Random,
        bounds: Rect,
        config: ExplosionConfig
    ): Particle {
        val baseRadius = getParticleRadius(random, config.particleSize)
        val baseCx = bounds.centerX() + (20.dpF() * (random.nextFloat() - 0.5f))
        val baseCy = bounds.centerY() + (20.dpF() * (random.nextFloat() - 0.5f))

        // Vertical motion parameters
        var top = bounds.height() * ((0.18f * random.nextFloat()) + 0.2f)
        val nextFloat = random.nextFloat()
        top = if (nextFloat < 0.2f) {
            top
        } else {
            top + ((top * 0.2f) * random.nextFloat())
        }

        var bottom = (bounds.height() * (random.nextFloat() - 0.5f)) * 1.8f
        bottom = when {
            nextFloat < 0.2f -> bottom
            nextFloat < 0.8f -> bottom * 0.6f
            else -> bottom * 0.3f
        }

        val mag = 4.0f * top / bottom
        val neg = -mag / bottom

        // Lifecycle parameters
        val life = 1.4f / 10f * random.nextFloat()
        val overflow = 0.4f * random.nextFloat()

        return Particle(
            color = 0, // Will be set from bitmap
            baseCx = baseCx,
            baseCy = baseCy,
            baseRadius = baseRadius,
            top = top,
            bottom = bottom,
            mag = mag,
            neg = neg,
            life = life,
            overflow = overflow
        )
    }
}

/**
 * Scatter-style physics - particles explode radially outward in all 360° directions.
 * Strong radial burst with particles shooting outward from the center.
 */
class ScatterPhysics : PhysicsEngine {

    override fun generateParticle(
        random: Random,
        bounds: Rect,
        config: ExplosionConfig
    ): Particle {
        val baseRadius = getParticleRadius(random, config.particleSize)

        // Start exactly from center (no random offset)
        val baseCx = bounds.centerX().toFloat()
        val baseCy = bounds.centerY().toFloat()

        // Random angle for 360° radial explosion
        val angle = random.nextFloat() * Math.PI.toFloat() * 2f

        // Variable speed - some particles shoot far, some stay close
        val speed = 0.6f + random.nextFloat() * 1.0f

        // Max distance particles travel (accounting for scaledProgress * endValue = 1.4)
        // We want particles to spread about 0.8x the bounds width/height
        val endValue = 1.4f
        val maxDist = (bounds.width().coerceAtLeast(bounds.height()) * 0.8f) / endValue

        // For radial scatter, we need independent X and Y motion components
        // bottom controls horizontal: cx = baseCx + (bottom * scaledProgress)
        val bottom = cos(angle) * maxDist * speed

        // top controls vertical: cy = baseCy - (top * scaledProgress)
        // Negative top means upward motion
        val top = -sin(angle) * maxDist * speed

        // No gravity or parabolic motion for scatter - pure linear radial motion
        val mag = 0f
        val neg = 0f

        val life = 1.4f / 25f * random.nextFloat()
        val overflow = 0.35f * random.nextFloat()

        return Particle(
            color = 0,
            baseCx = baseCx,
            baseCy = baseCy,
            baseRadius = baseRadius,
            top = top,
            bottom = bottom,
            mag = mag,
            neg = neg,
            life = life,
            overflow = overflow
        )
    }
}

/**
 * Fall/Shatter-style physics - particles fall downward like rain or shattered glass.
 * Opposite of fountain - particles break apart and fall down with gravity.
 */
class FallPhysics : PhysicsEngine {

    override fun generateParticle(
        random: Random,
        bounds: Rect,
        config: ExplosionConfig
    ): Particle {
        val baseRadius = getParticleRadius(random, config.particleSize)
        val baseCx = bounds.centerX() + (30.dpF() * (random.nextFloat() - 0.5f))
        val baseCy = bounds.centerY() + (20.dpF() * (random.nextFloat() - 0.5f))

        // For FALL: particles should fall straight down with gravity
        // Using similar approach to SCATTER with independent X/Y motion

        // Slight horizontal drift (left or right)
        val endValue = 1.4f
        val horizontalDrift = (random.nextFloat() - 0.5f) * bounds.width() * 0.3f / endValue
        val bottom = horizontalDrift

        // Downward motion with acceleration (gravity)
        // Particles accelerate as they fall: distance = initial_velocity * t + 0.5 * gravity * t²
        val initialDownwardVelocity = bounds.height() * (0.3f + random.nextFloat() * 0.4f) / endValue
        val gravityAcceleration = bounds.height() * 0.3f / endValue

        // top will be used for downward motion (positive top = fall down)
        val top = initialDownwardVelocity

        // mag is gravity acceleration, neg=0 marks this as FALL physics
        val mag = gravityAcceleration
        val neg = 0f

        val life = 1.4f / 15f * random.nextFloat()
        val overflow = 0.35f * random.nextFloat()

        return Particle(
            color = 0,
            baseCx = baseCx,
            baseCy = baseCy,
            baseRadius = baseRadius,
            top = top,
            bottom = bottom,
            mag = mag,
            neg = neg,
            life = life,
            overflow = overflow
        )
    }
}

/**
 * Vortex/Spiral-style physics - particles spiral outward in a rotating motion.
 * Creates a vortex or tornado-like effect with particles rotating as they expand.
 */
class VortexPhysics : PhysicsEngine {

    companion object {
        private const val TAG = "VortexPhysics"
        private var particleCount = 0
    }

    override fun generateParticle(
        random: Random,
        bounds: Rect,
        config: ExplosionConfig
    ): Particle {
        particleCount++

        val baseRadius = getParticleRadius(random, config.particleSize)
        val baseCx = bounds.centerX() + (15.dpF() * (random.nextFloat() - 0.5f))
        val baseCy = bounds.centerY() + (15.dpF() * (random.nextFloat() - 0.5f))

        // For VORTEX: particles spiral outward with rotation
        // We'll encode: initial angle in bottom, rotation amount in top, max radius in mag

        // Starting angle (0 to 2π)
        val startAngle = random.nextFloat() * Math.PI.toFloat() * 2f

        // Rotation during animation (2-4 full rotations)
        val rotationAmount = (2f + random.nextFloat() * 2f) * Math.PI.toFloat() * 2f

        // Max distance to spiral out
        val endValue = 1.4f
        val maxRadius = (bounds.width().coerceAtLeast(bounds.height()) * 0.6f) / endValue
        val spiralSpeed = 0.7f + random.nextFloat() * 0.6f
        val finalRadius = maxRadius * spiralSpeed

        // Encode vortex parameters:
        // bottom = starting angle
        // top = total rotation amount
        // mag = final radius
        // neg = -1.0 to mark as VORTEX physics
        val bottom = startAngle
        val top = rotationAmount
        val mag = finalRadius
        val neg = -1.0f  // Special marker for VORTEX physics

        val life = 1.4f / 18f * random.nextFloat()
        val overflow = 0.45f * random.nextFloat()

        if (particleCount <= 5) {
            Log.d(TAG, "VORTEX Particle #$particleCount: startAngle=${Math.toDegrees(startAngle.toDouble()).toInt()}°")
            Log.d(TAG, "  rotation=${Math.toDegrees(rotationAmount.toDouble()).toInt()}°, finalRadius=$finalRadius")
        }

        return Particle(
            color = 0,
            baseCx = baseCx,
            baseCy = baseCy,
            baseRadius = baseRadius,
            top = top,
            bottom = bottom,
            mag = mag,
            neg = neg,
            life = life,
            overflow = overflow
        )
    }
}

/**
 * Determines particle radius based on size configuration.
 */
private fun getParticleRadius(random: Random, size: ExplosionConfig.ParticleSize): Float {
    val V = 2.dpF()  // Standard size
    val W = 1.dpF()  // Minimum size
    val X = 5.dpF()  // Maximum size

    return when (size) {
        ExplosionConfig.ParticleSize.TINY -> {
            W + (V - W) * random.nextFloat()  // 1-2dp
        }
        ExplosionConfig.ParticleSize.SMALL -> {
            V + ((V + 1.dpF()) - V) * random.nextFloat()  // 2-3dp
        }
        ExplosionConfig.ParticleSize.MIXED -> {
            // 20% chance for large particles, 80% small (original behavior)
            if (random.nextFloat() < 0.2f) {
                V + ((X - V) * random.nextFloat())  // 2-5dp
            } else {
                W + ((V - W) * random.nextFloat())  // 1-2dp
            }
        }
        ExplosionConfig.ParticleSize.LARGE -> {
            (X - 1.dpF()) + (2.dpF() * random.nextFloat())  // 4-6dp
        }
    }
}
