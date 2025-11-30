package com.rejown.explosionfielddemo

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Choreographer
import android.view.View
import android.widget.GridLayout
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
    private val handler = Handler(Looper.getMainLooper())

    // Performance metrics
    private var totalExplosions = 0
    private var activeAnimations = 0
    private var frameCount = 0
    private var lastFrameTime = 0L
    private var currentFps = 60

    // FPS tracking
    private val frameCallback = object : Choreographer.FrameCallback {
        override fun doFrame(frameTimeNanos: Long) {
            val currentTime = System.currentTimeMillis()
            frameCount++

            // Calculate FPS every second
            if (lastFrameTime == 0L) {
                lastFrameTime = currentTime
            } else if (currentTime - lastFrameTime >= 1000) {
                currentFps = frameCount
                frameCount = 0
                lastFrameTime = currentTime
                updateMetrics()
            }

            Choreographer.getInstance().postFrameCallback(this)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        explosionField = ExplosionField.attach(this)
        setupExplosionListener()
        createTestViews()
        setupButtons()
        startFpsTracking()
        startMemoryTracking()
    }

    private fun setupExplosionListener() {
        explosionField.explosionListener = object : ExplosionField.OnExplosionListener {
            override fun onExplosionStart(view: View) {
                totalExplosions++
                activeAnimations++
                updateMetrics()
            }

            override fun onExplosionEnd(view: View) {
                activeAnimations--
                updateMetrics()
            }
        }
    }

    private fun createTestViews() {
        val colors = listOf(
            "#F44336", "#E91E63", "#9C27B0", "#673AB7",
            "#3F51B5", "#2196F3", "#03A9F4", "#00BCD4",
            "#009688", "#4CAF50", "#8BC34A", "#CDDC39",
            "#FFEB3B", "#FFC107", "#FF9800", "#FF5722"
        )

        binding.gridLayout.removeAllViews()
        val cardSize = (resources.displayMetrics.widthPixels - 96) / 4 // 4 columns with padding

        for (i in 0 until 16) {
            val card = MaterialCardView(this).apply {
                layoutParams = GridLayout.LayoutParams().apply {
                    width = cardSize
                    height = cardSize
                    columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                    setMargins(4, 4, 4, 4)
                }
                radius = 8f
                cardElevation = 4f
                setCardBackgroundColor(android.graphics.Color.parseColor(colors[i]))
                setOnClickListener { view -> explodeSingleView(view) }
            }
            binding.gridLayout.addView(card)
        }
    }

    private fun setupButtons() {
        binding.btnTest10.setOnClickListener {
            batchExplode(10, "10 explosions test started!")
        }

        binding.btnTest50.setOnClickListener {
            batchExplode(50, "50 explosions test started!")
        }

        binding.btnTestAll.setOnClickListener {
            explodeAllSimultaneous()
        }

        binding.btnTestSequential.setOnClickListener {
            explodeSequential()
        }

        binding.btnClearMetrics.setOnClickListener {
            clearMetrics()
        }

        binding.btnReset.setOnClickListener {
            recreate()
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun explodeSingleView(view: View) {
        val styles = ExplosionConfig.ExplosionStyle.values()
        val randomStyle = styles[Random.nextInt(styles.size)]

        explosionField.explode(view, ExplosionConfig(
            style = randomStyle,
            particleCount = ExplosionConfig.ParticleCount.MEDIUM,
            duration = 1200L
        ))
    }

    private fun batchExplode(count: Int, message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

        repeat(count) { index ->
            val randomViewIndex = Random.nextInt(binding.gridLayout.childCount)
            val view = binding.gridLayout.getChildAt(randomViewIndex)

            handler.postDelayed({
                explosionField.explode(view, ExplosionConfig(
                    style = ExplosionConfig.ExplosionStyle.SCATTER,
                    particleCount = ExplosionConfig.ParticleCount.LOW,
                    duration = 1000L
                ))
            }, index * 50L) // 50ms between each
        }
    }

    private fun explodeAllSimultaneous() {
        Toast.makeText(this, "All 16 views exploding simultaneously!", Toast.LENGTH_SHORT).show()

        for (i in 0 until binding.gridLayout.childCount) {
            val view = binding.gridLayout.getChildAt(i)
            explosionField.explode(view, ExplosionConfig(
                style = ExplosionConfig.ExplosionStyle.SCATTER,
                particleCount = ExplosionConfig.ParticleCount.MEDIUM,
                duration = 1200L
            ))
        }
    }

    private fun explodeSequential() {
        Toast.makeText(this, "Sequential explosion!", Toast.LENGTH_SHORT).show()

        for (i in 0 until binding.gridLayout.childCount) {
            val view = binding.gridLayout.getChildAt(i)
            handler.postDelayed({
                explosionField.explode(view, ExplosionConfig(
                    style = ExplosionConfig.ExplosionStyle.FOUNTAIN,
                    particleCount = ExplosionConfig.ParticleCount.MEDIUM,
                    duration = 1400L
                ))
            }, i * 200L) // 200ms between each
        }
    }

    private fun clearMetrics() {
        totalExplosions = 0
        updateMetrics()
        Toast.makeText(this, "Metrics cleared", Toast.LENGTH_SHORT).show()
    }

    private fun startFpsTracking() {
        Choreographer.getInstance().postFrameCallback(frameCallback)
    }

    private fun startMemoryTracking() {
        // Update memory every 500ms
        handler.postDelayed(object : Runnable {
            override fun run() {
                updateMetrics()
                handler.postDelayed(this, 500)
            }
        }, 500)
    }

    private fun updateMetrics() {
        runOnUiThread {
            // FPS
            binding.tvFps.text = currentFps.toString()

            // Memory in MB
            val runtime = Runtime.getRuntime()
            val usedMemory = (runtime.totalMemory() - runtime.freeMemory()) / (1024 * 1024)
            binding.tvMemory.text = usedMemory.toString()

            // Active animations
            binding.tvActiveAnimations.text = activeAnimations.toString()

            // Total explosions
            binding.tvTotalExplosions.text = totalExplosions.toString()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Choreographer.getInstance().removeFrameCallback(frameCallback)
        handler.removeCallbacksAndMessages(null)
    }
}
