package com.rejowan.explosionfield

import android.graphics.Rect
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
            ExplosionConfig.ExplosionStyle.IMPLODE -> ImplodePhysics()
            ExplosionConfig.ExplosionStyle.FLOAT_UP -> FloatUpPhysics()
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
 * Scatter-style physics - particles explode radially outward.
 */
class ScatterPhysics : PhysicsEngine {

    override fun generateParticle(
        random: Random,
        bounds: Rect,
        config: ExplosionConfig
    ): Particle {
        val baseRadius = getParticleRadius(random, config.particleSize)
        val baseCx = bounds.centerX().toFloat()
        val baseCy = bounds.centerY().toFloat()

        // Random angle for radial explosion
        val angle = random.nextFloat() * Math.PI.toFloat() * 2f
        val distance = random.nextFloat() * bounds.width() * 0.7f

        // Convert polar to cartesian
        val bottom = cos(angle) * distance
        val top = sin(angle) * distance * 0.5f  // Less vertical movement

        val mag = 0.5f + random.nextFloat() * 0.5f
        val neg = -0.001f  // Minimal gravity

        val life = 1.4f / 15f * random.nextFloat()
        val overflow = 0.3f * random.nextFloat()

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
 * Implode-style physics - particles move inward (reverse explosion).
 */
class ImplodePhysics : PhysicsEngine {

    override fun generateParticle(
        random: Random,
        bounds: Rect,
        config: ExplosionConfig
    ): Particle {
        val baseRadius = getParticleRadius(random, config.particleSize)

        // Start from expanded area
        val expandRadius = bounds.width() * 0.8f
        val angle = random.nextFloat() * Math.PI.toFloat() * 2f

        val baseCx = bounds.centerX() + cos(angle) * expandRadius
        val baseCy = bounds.centerY() + sin(angle) * expandRadius

        // Negative bottom for inward movement
        val bottom = -(0.8f + random.nextFloat() * 0.4f)
        val top = (random.nextFloat() - 0.5f) * 0.3f

        val mag = 0.2f
        val neg = 0.0001f  // Slight curve

        val life = 1.4f / 20f * random.nextFloat()
        val overflow = 0.5f * random.nextFloat()

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
 * Float-up style physics - particles gently float upward like bubbles or smoke.
 */
class FloatUpPhysics : PhysicsEngine {

    override fun generateParticle(
        random: Random,
        bounds: Rect,
        config: ExplosionConfig
    ): Particle {
        val baseRadius = getParticleRadius(random, config.particleSize)
        val baseCx = bounds.centerX() + (30.dpF() * (random.nextFloat() - 0.5f))
        val baseCy = bounds.centerY() + (20.dpF() * (random.nextFloat() - 0.5f))

        // Gentle upward float with slight horizontal drift
        val bottom = (random.nextFloat() - 0.5f) * 0.3f  // Minimal horizontal
        val top = bounds.height() * (0.5f + random.nextFloat() * 0.3f)  // Upward

        val mag = 1.5f + random.nextFloat() * 0.5f  // Gentle upward velocity
        val neg = 0.0001f  // Almost no gravity

        val life = 1.4f / 12f * random.nextFloat()
        val overflow = 0.6f * random.nextFloat()  // Longer fade

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
