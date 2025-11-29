package com.rejown.explosionfielddemo

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.rejown.explosionfielddemo.databinding.ActivityMainBinding
import com.rejowan.explosionfield.ExplosionConfig
import com.rejowan.explosionfield.ExplosionField

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var explosionField: ExplosionField
    private var explosionCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Attach explosion field to activity
        explosionField = ExplosionField.attach(this)

        // Set up explosion listener
        explosionField.explosionListener = object : ExplosionField.OnExplosionListener {
            override fun onExplosionStart(view: View) {
                explosionCount++
            }

            override fun onExplosionEnd(view: View) {
                Toast.makeText(
                    this@MainActivity,
                    "💥 Boom! Total explosions: $explosionCount",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        setupButtons()
        setupCards()
        setupExplosionStylesShowcase()
        setupNavigationButtons()
    }

    private fun setupButtons() {
        // Preset configurations
        binding.btnDefault.setOnClickListener { view ->
            explosionField.explode(view)
        }

        binding.btnGentle.setOnClickListener { view ->
            explosionField.explode(view, ExplosionConfig.GENTLE)
        }

        binding.btnAggressive.setOnClickListener { view ->
            explosionField.explode(view, ExplosionConfig.AGGRESSIVE)
        }

        binding.btnDust.setOnClickListener { view ->
            explosionField.explode(view, ExplosionConfig.DUST)
        }

        binding.btnChunky.setOnClickListener { view ->
            explosionField.explode(view, ExplosionConfig.CHUNKY)
        }

        // Explosion styles
        binding.btnScatter.setOnClickListener { view ->
            explosionField.explode(view, ExplosionConfig(
                style = ExplosionConfig.ExplosionStyle.SCATTER,
                duration = 1500L
            ))
        }

        binding.btnImplode.setOnClickListener { view ->
            explosionField.explode(view, ExplosionConfig(
                style = ExplosionConfig.ExplosionStyle.IMPLODE,
                duration = 1200L
            ))
        }

        binding.btnFloatUp.setOnClickListener { view ->
            explosionField.explode(view, ExplosionConfig(
                style = ExplosionConfig.ExplosionStyle.FLOAT_UP,
                duration = 1800L
            ))
        }

        // Color modes
        binding.btnGrayscale.setOnClickListener { view ->
            explosionField.explode(view, ExplosionConfig(
                colorMode = ExplosionConfig.ColorMode.GRAYSCALE
            ))
        }

        binding.btnRandomColors.setOnClickListener { view ->
            explosionField.explode(view, ExplosionConfig(
                colorMode = ExplosionConfig.ColorMode.RANDOM,
                particleCount = ExplosionConfig.ParticleCount.HIGH
            ))
        }

        // Reset button
        binding.btnReset.setOnClickListener {
            explosionCount = 0
            recreate()
        }
    }

    private fun setupCards() {
        // Different view types - Material Cards with scatter explosion
        val cards = listOf(
            binding.imageCard1,
            binding.imageCard2,
            binding.imageCard3,
            binding.imageCard4
        )

        cards.forEach { card ->
            card.setOnClickListener { view ->
                explosionField.explode(view, ExplosionConfig(
                    style = ExplosionConfig.ExplosionStyle.SCATTER,
                    particleCount = ExplosionConfig.ParticleCount.HIGH,
                    particleSize = ExplosionConfig.ParticleSize.MIXED,
                    duration = 1200L,
                    hapticFeedback = true
                ))
            }
        }
    }

    private fun setupExplosionStylesShowcase() {
        val fountainConfig = ExplosionConfig(
            style = ExplosionConfig.ExplosionStyle.FOUNTAIN,
            particleCount = ExplosionConfig.ParticleCount.MEDIUM,
            particleSize = ExplosionConfig.ParticleSize.MIXED,
            duration = 1500L,
            hapticFeedback = true
        )

        val scatterConfig = ExplosionConfig(
            style = ExplosionConfig.ExplosionStyle.SCATTER,
            particleCount = ExplosionConfig.ParticleCount.HIGH,
            particleSize = ExplosionConfig.ParticleSize.SMALL,
            duration = 1400L,
            hapticFeedback = true
        )

        val implodeConfig = ExplosionConfig(
            style = ExplosionConfig.ExplosionStyle.IMPLODE,
            particleCount = ExplosionConfig.ParticleCount.MEDIUM,
            particleSize = ExplosionConfig.ParticleSize.LARGE,
            duration = 1600L,
            hapticFeedback = true
        )

        val floatConfig = ExplosionConfig(
            style = ExplosionConfig.ExplosionStyle.FLOAT_UP,
            particleCount = ExplosionConfig.ParticleCount.LOW,
            particleSize = ExplosionConfig.ParticleSize.MIXED,
            duration = 2000L,
            hapticFeedback = true
        )

        // FOUNTAIN STYLE (Row 1)
        binding.fountainCircle.setOnClickListener { explosionField.explode(it, fountainConfig) }
        binding.fountainIcon.setOnClickListener { explosionField.explode(it, fountainConfig) }
        binding.fountainImage.setOnClickListener { explosionField.explode(it, fountainConfig) }
        binding.fountainCard.setOnClickListener { explosionField.explode(it, fountainConfig) }

        // SCATTER STYLE (Row 2)
        binding.scatterCircle.setOnClickListener { explosionField.explode(it, scatterConfig) }
        binding.scatterIcon.setOnClickListener { explosionField.explode(it, scatterConfig) }
        binding.scatterImage.setOnClickListener { explosionField.explode(it, scatterConfig) }
        binding.scatterCard.setOnClickListener { explosionField.explode(it, scatterConfig) }

        // IMPLODE STYLE (Row 3)
        binding.implodeCircle.setOnClickListener { explosionField.explode(it, implodeConfig) }
        binding.implodeIcon.setOnClickListener { explosionField.explode(it, implodeConfig) }
        binding.implodeImage.setOnClickListener { explosionField.explode(it, implodeConfig) }
        binding.implodeCard.setOnClickListener { explosionField.explode(it, implodeConfig) }

        // FLOAT UP STYLE (Row 4)
        binding.floatCircle.setOnClickListener { explosionField.explode(it, floatConfig) }
        binding.floatIcon.setOnClickListener { explosionField.explode(it, floatConfig) }
        binding.floatImage.setOnClickListener { explosionField.explode(it, floatConfig) }
        binding.floatCard.setOnClickListener { explosionField.explode(it, floatConfig) }
    }

    private fun setupNavigationButtons() {
        binding.btnAdvancedDemo.setOnClickListener {
            startActivity(Intent(this, AdvancedDemoActivity::class.java))
        }

        binding.btnPerformanceTest.setOnClickListener {
            startActivity(Intent(this, PerformanceTestActivity::class.java))
        }
    }
}
