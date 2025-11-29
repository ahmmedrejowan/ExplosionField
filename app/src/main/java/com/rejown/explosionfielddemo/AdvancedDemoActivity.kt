package com.rejown.explosionfielddemo

import android.graphics.Color
import android.os.Bundle
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
        setupAdvancedDemos()
    }

    private fun setupAdvancedDemos() {
        binding.btnSuperFast.setOnClickListener { view -> explosionField.explode(view, ExplosionConfig(duration = 500L, shakeIntensity = 0.15f)) }
        binding.btnSuperSlow.setOnClickListener { view -> explosionField.explode(view, ExplosionConfig(duration = 3000L, shakeIntensity = 0.02f)) }
        binding.btnFewParticles.setOnClickListener { view -> explosionField.explode(view, ExplosionConfig(particleCount = ExplosionConfig.ParticleCount.LOW, particleSize = ExplosionConfig.ParticleSize.LARGE)) }
        binding.btnManyParticles.setOnClickListener { view -> explosionField.explode(view, ExplosionConfig(particleCount = ExplosionConfig.ParticleCount.HIGH, particleSize = ExplosionConfig.ParticleSize.TINY)) }
        binding.btnScatterGrayscale.setOnClickListener { view -> explosionField.explode(view, ExplosionConfig(style = ExplosionConfig.ExplosionStyle.SCATTER, colorMode = ExplosionConfig.ColorMode.GRAYSCALE)) }
        binding.btnImplodeRandom.setOnClickListener { view -> explosionField.explode(view, ExplosionConfig(style = ExplosionConfig.ExplosionStyle.IMPLODE, colorMode = ExplosionConfig.ColorMode.RANDOM)) }
        binding.btnFloatTinted.setOnClickListener { view -> explosionField.explode(view, ExplosionConfig(style = ExplosionConfig.ExplosionStyle.FLOAT_UP, colorMode = ExplosionConfig.ColorMode.TINTED, tintColor = Color.parseColor("#FF6B6B"))) }
        binding.btnNoShake.setOnClickListener { view -> explosionField.explode(view, ExplosionConfig(shakeBeforeExplode = false)) }
        binding.btnWithHaptic.setOnClickListener { view -> explosionField.explode(view, ExplosionConfig(hapticFeedback = true, duration = 800L)) }
        binding.btnUltimate.setOnClickListener { view -> explosionField.explode(view, ExplosionConfig(style = ExplosionConfig.ExplosionStyle.SCATTER, colorMode = ExplosionConfig.ColorMode.RANDOM, particleCount = ExplosionConfig.ParticleCount.HIGH, particleSize = ExplosionConfig.ParticleSize.MIXED, duration = 1500L, shakeIntensity = 0.1f, hapticFeedback = true)) }
        binding.btnBack.setOnClickListener { finish() }
    }
}
