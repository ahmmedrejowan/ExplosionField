package com.rejown.explosionfielddemo

import android.content.Intent
import android.os.Bundle
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

        setupExplosionStyles()
        setupPresets()
        setupSampleCards()
        setupHelperButtons()
        setupNavigation()
    }

    private fun setupExplosionStyles() {
        // FOUNTAIN Style
        binding.btnFountain.setOnClickListener { view ->
            explosionField.explode(view, ExplosionConfig(
                style = ExplosionConfig.ExplosionStyle.FOUNTAIN,
                duration = 1500L
            ))
            showToast("Fountain explosion!")
        }

        // SCATTER Style
        binding.btnScatter.setOnClickListener { view ->
            explosionField.explode(view, ExplosionConfig(
                style = ExplosionConfig.ExplosionStyle.SCATTER,
                duration = 1400L
            ))
            showToast("Scatter explosion!")
        }

        // FALL Style
        binding.btnFall.setOnClickListener { view ->
            explosionField.explode(view, ExplosionConfig(
                style = ExplosionConfig.ExplosionStyle.FALL,
                duration = 1600L
            ))
            showToast("Fall explosion!")
        }

        // VORTEX Style
        binding.btnVortex.setOnClickListener { view ->
            explosionField.explode(view, ExplosionConfig(
                style = ExplosionConfig.ExplosionStyle.VORTEX,
                duration = 1800L
            ))
            showToast("Vortex explosion!")
        }
    }

    private fun setupPresets() {
        binding.btnGentle.setOnClickListener { view ->
            explosionField.explode(view, ExplosionConfig.GENTLE)
            showToast("Gentle explosion")
        }

        binding.btnAggressive.setOnClickListener { view ->
            explosionField.explode(view, ExplosionConfig.AGGRESSIVE)
            showToast("Aggressive explosion!")
        }

        binding.btnDust.setOnClickListener { view ->
            explosionField.explode(view, ExplosionConfig.DUST)
            showToast("Dust explosion")
        }

        binding.btnChunky.setOnClickListener { view ->
            explosionField.explode(view, ExplosionConfig.CHUNKY)
            showToast("Chunky explosion!")
        }
    }

    private fun setupSampleCards() {
        // Card 1: FOUNTAIN Style
        binding.sampleCard1.setOnClickListener {
            explosionField.explode(it, ExplosionConfig(
                style = ExplosionConfig.ExplosionStyle.FOUNTAIN,
                particleCount = ExplosionConfig.ParticleCount.HIGH,
                duration = 1500L,
                hapticFeedback = true
            ))
        }

        // Card 2: SCATTER Style
        binding.sampleCard2.setOnClickListener {
            explosionField.explode(it, ExplosionConfig(
                style = ExplosionConfig.ExplosionStyle.SCATTER,
                particleCount = ExplosionConfig.ParticleCount.HIGH,
                duration = 1400L,
                hapticFeedback = true
            ))
        }

        // Card 3: FALL Style
        binding.sampleCard3.setOnClickListener {
            explosionField.explode(it, ExplosionConfig(
                style = ExplosionConfig.ExplosionStyle.FALL,
                particleCount = ExplosionConfig.ParticleCount.HIGH,
                duration = 1600L,
                hapticFeedback = true
            ))
        }

        // Card 4: VORTEX Style
        binding.sampleCard4.setOnClickListener {
            explosionField.explode(it, ExplosionConfig(
                style = ExplosionConfig.ExplosionStyle.VORTEX,
                particleCount = ExplosionConfig.ParticleCount.HIGH,
                duration = 1800L,
                hapticFeedback = true
            ))
        }
    }

    private fun setupHelperButtons() {
        // Explode All button - uses different style for each card
        binding.btnExplodeAll.setOnClickListener {
            // Card 1: FOUNTAIN
            binding.sampleCard1.postDelayed({
                explosionField.explode(binding.sampleCard1, ExplosionConfig(
                    style = ExplosionConfig.ExplosionStyle.FOUNTAIN,
                    particleCount = ExplosionConfig.ParticleCount.HIGH,
                    duration = 1500L
                ))
            }, 0)

            // Card 2: SCATTER
            binding.sampleCard2.postDelayed({
                explosionField.explode(binding.sampleCard2, ExplosionConfig(
                    style = ExplosionConfig.ExplosionStyle.SCATTER,
                    particleCount = ExplosionConfig.ParticleCount.HIGH,
                    duration = 1400L
                ))
            }, 100)

            // Card 3: FALL
            binding.sampleCard3.postDelayed({
                explosionField.explode(binding.sampleCard3, ExplosionConfig(
                    style = ExplosionConfig.ExplosionStyle.FALL,
                    particleCount = ExplosionConfig.ParticleCount.HIGH,
                    duration = 1600L
                ))
            }, 200)

            // Card 4: VORTEX
            binding.sampleCard4.postDelayed({
                explosionField.explode(binding.sampleCard4, ExplosionConfig(
                    style = ExplosionConfig.ExplosionStyle.VORTEX,
                    particleCount = ExplosionConfig.ParticleCount.HIGH,
                    duration = 1800L
                ))
            }, 300)

            showToast("💥 Boom!")
        }

        // Reset button
        binding.btnReset.setOnClickListener {
            recreate()
        }
    }

    private fun setupNavigation() {
        binding.btnAdvancedDemo.setOnClickListener {
            startActivity(Intent(this, AdvancedDemoActivity::class.java))
        }

        binding.btnPerformanceTest.setOnClickListener {
            startActivity(Intent(this, PerformanceTestActivity::class.java))
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
