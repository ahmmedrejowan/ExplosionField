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
    private var currentConfig: ExplosionConfig = ExplosionConfig()

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
        setupDemoCards()
        setupHelperButtons()
    }

    private fun setupDurationVariations() {
        binding.btnSuperFast.setOnClickListener {
            currentConfig = ExplosionConfig(
                duration = 500L,
                shakeIntensity = 0.15f
            )
            Toast.makeText(this, "Super Fast mode: Tap a demo card!", Toast.LENGTH_SHORT).show()
        }

        binding.btnSuperSlow.setOnClickListener {
            currentConfig = ExplosionConfig(
                duration = 3000L,
                shakeIntensity = 0.02f
            )
            Toast.makeText(this, "Super Slow mode: Tap a demo card!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupParticleVariations() {
        binding.btnFewParticles.setOnClickListener {
            currentConfig = ExplosionConfig(
                particleCount = ExplosionConfig.ParticleCount.LOW,
                particleSize = ExplosionConfig.ParticleSize.LARGE
            )
            Toast.makeText(this, "Few Large Particles: Tap a demo card!", Toast.LENGTH_SHORT).show()
        }

        binding.btnManyParticles.setOnClickListener {
            currentConfig = ExplosionConfig(
                particleCount = ExplosionConfig.ParticleCount.HIGH,
                particleSize = ExplosionConfig.ParticleSize.TINY
            )
            Toast.makeText(this, "Many Tiny Particles: Tap a demo card!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupCombinedEffects() {
        binding.btnScatterGrayscale.setOnClickListener {
            currentConfig = ExplosionConfig(
                style = ExplosionConfig.ExplosionStyle.SCATTER,
                colorMode = ExplosionConfig.ColorMode.GRAYSCALE
            )
            Toast.makeText(this, "Scatter + Grayscale: Tap a demo card!", Toast.LENGTH_SHORT).show()
        }

        binding.btnImplodeRandom.setOnClickListener {
            currentConfig = ExplosionConfig(
                style = ExplosionConfig.ExplosionStyle.FALL,
                colorMode = ExplosionConfig.ColorMode.RANDOM
            )
            Toast.makeText(this, "Fall + Random Colors: Tap a demo card!", Toast.LENGTH_SHORT).show()
        }

        binding.btnFloatTinted.setOnClickListener {
            currentConfig = ExplosionConfig(
                style = ExplosionConfig.ExplosionStyle.VORTEX,
                colorMode = ExplosionConfig.ColorMode.TINTED,
                tintColor = Color.parseColor("#FF6B6B")
            )
            Toast.makeText(this, "Vortex + Red Tint: Tap a demo card!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupSpecialOptions() {
        binding.btnNoShake.setOnClickListener {
            currentConfig = ExplosionConfig(
                shakeBeforeExplode = false
            )
            Toast.makeText(this, "No Shake: Tap a demo card!", Toast.LENGTH_SHORT).show()
        }

        binding.btnWithHaptic.setOnClickListener {
            currentConfig = ExplosionConfig(
                hapticFeedback = true,
                duration = 800L
            )
            Toast.makeText(this, "Haptic Feedback: Tap a demo card!", Toast.LENGTH_SHORT).show()
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

    private fun setupDemoCards() {
        binding.demoCard1.setOnClickListener { view ->
            explosionField.explode(view, currentConfig)
        }

        binding.demoCard2.setOnClickListener { view ->
            explosionField.explode(view, currentConfig)
        }

        binding.demoCard3.setOnClickListener { view ->
            explosionField.explode(view, currentConfig)
        }

        binding.demoCard4.setOnClickListener { view ->
            explosionField.explode(view, currentConfig)
        }
    }

    private fun setupHelperButtons() {
        binding.btnExplodeAll.setOnClickListener {
            explodeAllCards(currentConfig)
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
