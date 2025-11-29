package com.rejowan.explosionfield

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import android.util.AttributeSet
import android.view.HapticFeedbackConstants
import android.view.View
import android.view.ViewGroup
import android.view.Window
import kotlin.random.Random

/**
 * A view that creates an explosive particle effect when views are "exploded".
 *
 * This view should be attached to an Activity's root view and acts as an overlay
 * where explosion animations are rendered.
 *
 * Usage:
 * ```kotlin
 * val explosionField = ExplosionField.attach(activity)
 * explosionField.explode(myButton)
 * ```
 *
 * @see ExplosionConfig for customization options
 */
class ExplosionField @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val explosions = mutableListOf<ExplosionAnimator>()
    private var expandInset = intArrayOf(32.dp(), 32.dp())

    /**
     * Listener for explosion lifecycle events.
     */
    var explosionListener: OnExplosionListener? = null

    init {
        // View is transparent and non-interactive
        setWillNotDraw(false)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        for (explosion in explosions) {
            explosion.draw(canvas)
        }
    }

    /**
     * Sets the expansion bounds for explosions.
     * Particles can spread beyond the view bounds by this amount.
     *
     * @param dx Horizontal expansion in dp
     * @param dy Vertical expansion in dp
     */
    fun expandExplosionBound(dx: Int, dy: Int) {
        expandInset[0] = dx.dp()
        expandInset[1] = dy.dp()
    }

    /**
     * Explodes a view with default configuration.
     *
     * @param view The view to explode
     */
    fun explode(view: View) {
        explode(view, ExplosionConfig.DEFAULT)
    }

    /**
     * Explodes a view with custom configuration.
     *
     * @param view The view to explode
     * @param config Explosion configuration
     */
    fun explode(view: View, config: ExplosionConfig = ExplosionConfig.DEFAULT) {
        // Get view bounds in global coordinates
        val rect = Rect()
        view.getGlobalVisibleRect(rect)

        // Convert to local coordinates
        val location = IntArray(2)
        getLocationOnScreen(location)
        rect.offset(-location[0], -location[1])

        // Expand bounds for particle spread
        rect.inset(-expandInset[0], -expandInset[1])

        // Capture view as bitmap
        val bitmap = view.toBitmap() ?: return

        // Trigger haptic feedback if enabled
        if (config.hapticFeedback) {
            view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
        }

        // Shake animation before explosion (if enabled)
        if (config.shakeBeforeExplode) {
            animateShake(view, config)
        }

        // Fade out the original view
        val startDelay = if (config.shakeBeforeExplode) 100L else 0L
        view.animate()
            .setDuration(150)
            .setStartDelay(startDelay)
            .scaleX(0f)
            .scaleY(0f)
            .alpha(0f)
            .withEndAction {
                if (config.removeViewAfterExplosion) {
                    (view.parent as? ViewGroup)?.removeView(view)
                }
            }
            .start()

        // Notify listener
        explosionListener?.onExplosionStart(view)

        // Start particle explosion
        explode(bitmap, rect, startDelay, config, view)
    }

    /**
     * Animates a shake effect on the view before explosion.
     */
    private fun animateShake(view: View, config: ExplosionConfig) {
        val random = Random(System.currentTimeMillis())
        val animator = ValueAnimator.ofFloat(0f, 1f).setDuration(config.shakeDuration)

        animator.addUpdateListener {
            view.translationX = (random.nextFloat() - 0.5f) * view.width * config.shakeIntensity
            view.translationY = (random.nextFloat() - 0.5f) * view.height * config.shakeIntensity
        }

        animator.start()
    }

    /**
     * Explodes a bitmap with custom parameters.
     *
     * @param bitmap Source bitmap for particle colors
     * @param bounds Explosion bounds
     * @param startDelay Delay before starting animation
     * @param config Explosion configuration
     * @param originalView Original view being exploded (for callbacks)
     */
    fun explode(
        bitmap: Bitmap,
        bounds: Rect,
        startDelay: Long = 0L,
        config: ExplosionConfig = ExplosionConfig.DEFAULT,
        originalView: View? = null
    ) {
        val explosion = ExplosionAnimator(this, bitmap, bounds, config)

        explosion.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                explosions.remove(animation)
                originalView?.let {
                    explosionListener?.onExplosionEnd(it)
                }
            }
        })

        explosion.startDelay = startDelay
        explosions.add(explosion)
        explosion.start()
    }

    /**
     * Clears all active explosions.
     */
    fun clear() {
        explosions.clear()
        invalidate()
    }

    /**
     * Listener interface for explosion lifecycle events.
     */
    interface OnExplosionListener {
        /**
         * Called when an explosion animation starts.
         *
         * @param view The view being exploded
         */
        fun onExplosionStart(view: View) {}

        /**
         * Called when an explosion animation ends.
         *
         * @param view The view that was exploded
         */
        fun onExplosionEnd(view: View) {}
    }

    companion object {
        /**
         * Attaches an ExplosionField to an Activity's root view.
         *
         * @param activity The activity to attach to
         * @return The created ExplosionField instance
         */
        @JvmStatic
        fun attach(activity: Activity): ExplosionField {
            val rootView = activity.findViewById<ViewGroup>(Window.ID_ANDROID_CONTENT)
            val explosionField = ExplosionField(activity)

            rootView.addView(
                explosionField,
                ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            )

            return explosionField
        }
    }
}
