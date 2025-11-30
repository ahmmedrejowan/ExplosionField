package com.rejown.explosionfielddemo

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.card.MaterialCardView
import com.rejown.explosionfielddemo.databinding.ActivityPerformanceTestBinding
import com.rejowan.explosionfield.ExplosionConfig
import com.rejowan.explosionfield.ExplosionField
import kotlin.random.Random

class PerformanceTestActivity : AppCompatActivity() {

    private val binding by lazy { ActivityPerformanceTestBinding.inflate(layoutInflater) }
    private lateinit var explosionField: ExplosionField
    private var explosionCount = 0
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        explosionField = ExplosionField.attach(this)
        explosionField.explosionListener = object : ExplosionField.OnExplosionListener {
            override fun onExplosionStart(view: View) {
                explosionCount++
                updateStats()
            }
        }

        setupButtons()
        createTestViews()
    }

    private fun setupButtons() {
        binding.btnExplodeAll.setOnClickListener { explodeAllViews() }
        binding.btnExplodeSequential.setOnClickListener { explodeSequential() }
        binding.btnExplodeRandom.setOnClickListener { explodeRandom(10) }
        binding.btnReset.setOnClickListener { explosionCount = 0; recreate() }
        binding.btnBack.setOnClickListener { finish() }
    }

    private fun createTestViews() {
        val colors = listOf("#F44336", "#E91E63", "#9C27B0", "#673AB7", "#3F51B5", "#2196F3", "#03A9F4", "#00BCD4", "#009688", "#4CAF50", "#8BC34A", "#CDDC39", "#FFEB3B", "#FFC107", "#FF9800", "#FF5722")
        binding.gridLayout.removeAllViews()
        for (i in 0 until 16) {
            val card = MaterialCardView(this).apply {
                layoutParams = android.widget.GridLayout.LayoutParams().apply {
                    width = 0
                    height = resources.displayMetrics.widthPixels / 4 - 24
                    columnSpec = android.widget.GridLayout.spec(i % 4, 1f)
                    setMargins(8, 8, 8, 8)
                }
                radius = 12f
                cardElevation = 4f
                setCardBackgroundColor(android.graphics.Color.parseColor(colors[i]))
                setOnClickListener { view -> explodeSingleView(view) }
            }
            binding.gridLayout.addView(card)
        }
    }

    private fun explodeSingleView(view: View) {
        explosionField.explode(view, ExplosionConfig(style = listOf(ExplosionConfig.ExplosionStyle.FOUNTAIN, ExplosionConfig.ExplosionStyle.SCATTER, ExplosionConfig.ExplosionStyle.FALL, ExplosionConfig.ExplosionStyle.VORTEX).random(), colorMode = ExplosionConfig.ColorMode.ORIGINAL, particleCount = ExplosionConfig.ParticleCount.MEDIUM))
    }

    private fun explodeAllViews() {
        Toast.makeText(this, "Exploding all 16 views simultaneously!", Toast.LENGTH_SHORT).show()
        for (i in 0 until binding.gridLayout.childCount) {
            val view = binding.gridLayout.getChildAt(i)
            handler.postDelayed({ explosionField.explode(view, ExplosionConfig(style = ExplosionConfig.ExplosionStyle.SCATTER, particleCount = ExplosionConfig.ParticleCount.LOW, duration = 1200L)) }, (i * 50).toLong())
        }
    }

    private fun explodeSequential() {
        Toast.makeText(this, "Sequential explosion!", Toast.LENGTH_SHORT).show()
        for (i in 0 until binding.gridLayout.childCount) {
            val view = binding.gridLayout.getChildAt(i)
            handler.postDelayed({ explosionField.explode(view, ExplosionConfig(style = ExplosionConfig.ExplosionStyle.FOUNTAIN, particleCount = ExplosionConfig.ParticleCount.MEDIUM)) }, (i * 200).toLong())
        }
    }

    private fun explodeRandom(count: Int) {
        Toast.makeText(this, "Random $count explosions!", Toast.LENGTH_SHORT).show()
        repeat(count) {
            val randomIndex = Random.nextInt(binding.gridLayout.childCount)
            val view = binding.gridLayout.getChildAt(randomIndex)
            handler.postDelayed({ explosionField.explode(view, ExplosionConfig(style = listOf(ExplosionConfig.ExplosionStyle.FOUNTAIN, ExplosionConfig.ExplosionStyle.SCATTER, ExplosionConfig.ExplosionStyle.FALL, ExplosionConfig.ExplosionStyle.VORTEX).random(), colorMode = listOf(ExplosionConfig.ColorMode.ORIGINAL, ExplosionConfig.ColorMode.GRAYSCALE, ExplosionConfig.ColorMode.RANDOM).random(), particleCount = ExplosionConfig.ParticleCount.MEDIUM)) }, (it * 150).toLong())
        }
    }

    private fun updateStats() {
        binding.statsText.text = "Total Explosions: $explosionCount"
    }
}
