package com.rejown.explosionfielddemo

import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.rejown.explosionfielddemo.databinding.ActivityAdvancedDemoBinding
import com.rejowan.explosionfield.ExplosionConfig
import com.rejowan.explosionfield.ExplosionField

class AdvancedDemoActivity : AppCompatActivity() {

    private val binding by lazy { ActivityAdvancedDemoBinding.inflate(layoutInflater) }
    private lateinit var explosionField: ExplosionField

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        explosionField = ExplosionField.attach(this)
        setupDurationVariations()
        setupParticleVariations()
        setupCombinedEffects()
        setupSpecialOptions()
        setupUltimate()
        setupHelperButtons()
    }

    private fun setupDurationVariations() {
        binding.btnSuperFast.setOnClickListener {
            val config = ExplosionConfig(
                duration = 500L,
                shakeIntensity = 0.15f
            )
            explodeAllCards(config)
            Toast.makeText(this, "Super Fast explosion!", Toast.LENGTH_SHORT).show()
        }

        binding.btnSuperSlow.setOnClickListener {
            val config = ExplosionConfig(
                duration = 3000L,
                shakeIntensity = 0.02f
            )
            explodeAllCards(config)
            Toast.makeText(this, "Super Slow explosion!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupParticleVariations() {
        binding.btnFewParticles.setOnClickListener {
            val config = ExplosionConfig(
                particleCount = ExplosionConfig.ParticleCount.LOW,
                particleSize = ExplosionConfig.ParticleSize.LARGE
            )
            explodeAllCards(config)
            Toast.makeText(this, "Few Large Particles!", Toast.LENGTH_SHORT).show()
        }

        binding.btnManyParticles.setOnClickListener {
            val config = ExplosionConfig(
                particleCount = ExplosionConfig.ParticleCount.HIGH,
                particleSize = ExplosionConfig.ParticleSize.TINY
            )
            explodeAllCards(config)
            Toast.makeText(this, "Many Tiny Particles!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupCombinedEffects() {
        binding.btnScatterGrayscale.setOnClickListener {
            val config = ExplosionConfig(
                style = ExplosionConfig.ExplosionStyle.SCATTER,
                colorMode = ExplosionConfig.ColorMode.GRAYSCALE
            )
            explodeAllCards(config)
            Toast.makeText(this, "Scatter + Grayscale!", Toast.LENGTH_SHORT).show()
        }

        binding.btnImplodeRandom.setOnClickListener {
            val config = ExplosionConfig(
                style = ExplosionConfig.ExplosionStyle.FALL,
                colorMode = ExplosionConfig.ColorMode.RANDOM
            )
            explodeAllCards(config)
            Toast.makeText(this, "Fall + Random Colors!", Toast.LENGTH_SHORT).show()
        }

        binding.btnFloatTinted.setOnClickListener {
            val config = ExplosionConfig(
                style = ExplosionConfig.ExplosionStyle.VORTEX,
                colorMode = ExplosionConfig.ColorMode.TINTED,
                tintColor = Color.parseColor("#FF6B6B")
            )
            explodeAllCards(config)
            Toast.makeText(this, "Vortex + Red Tint!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupSpecialOptions() {
        binding.btnNoShake.setOnClickListener {
            val config = ExplosionConfig(
                shakeBeforeExplode = false
            )
            explodeAllCards(config)
            Toast.makeText(this, "No Shake Effect!", Toast.LENGTH_SHORT).show()
        }

        binding.btnWithHaptic.setOnClickListener {
            val config = ExplosionConfig(
                hapticFeedback = true,
                duration = 800L
            )
            explodeAllCards(config)
            Toast.makeText(this, "Haptic Feedback!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupUltimate() {
        binding.btnUltimate.setOnClickListener {
            Toast.makeText(this, "ULTIMATE EXPLOSION!", Toast.LENGTH_SHORT).show()
            explodeAllCards(
                ExplosionConfig(
                    style = ExplosionConfig.ExplosionStyle.SCATTER,
                    colorMode = ExplosionConfig.ColorMode.RANDOM,
                    particleCount = ExplosionConfig.ParticleCount.HIGH,
                    particleSize = ExplosionConfig.ParticleSize.MIXED,
                    duration = 1500L,
                    shakeIntensity = 0.1f,
                    hapticFeedback = true
                )
            )
        }
    }

    private fun setupHelperButtons() {
        binding.btnExplodeAll.setOnClickListener {
            val config = ExplosionConfig(
                style = ExplosionConfig.ExplosionStyle.SCATTER,
                colorMode = ExplosionConfig.ColorMode.RANDOM,
                particleCount = ExplosionConfig.ParticleCount.HIGH,
                duration = 1500L
            )
            explodeAllCards(config)
            Toast.makeText(this, "Exploding all cards!", Toast.LENGTH_SHORT).show()
        }

        binding.btnReset.setOnClickListener {
            recreate()
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun explodeAllCards(config: ExplosionConfig) {
        binding.demoCard1.postDelayed({ explosionField.explode(binding.demoCard1, config) }, 0)
        binding.demoCard2.postDelayed({ explosionField.explode(binding.demoCard2, config) }, 100)
        binding.demoCard3.postDelayed({ explosionField.explode(binding.demoCard3, config) }, 200)
        binding.demoCard4.postDelayed({ explosionField.explode(binding.demoCard4, config) }, 300)
    }
}
