package com.rejowan.explosionfield

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.view.View
import android.widget.ImageView
import kotlin.math.roundToInt

/**
 * Converts density-independent pixels (dp) to actual pixels (px).
 *
 * @return The pixel value
 */
fun Int.dp(): Int = (this * Resources.getSystem().displayMetrics.density).roundToInt()

/**
 * Converts density-independent pixels (dp) to actual pixels (px) as Float.
 *
 * @return The pixel value as Float
 */
fun Int.dpF(): Float = this * Resources.getSystem().displayMetrics.density

/**
 * Converts this View to a Bitmap representation.
 *
 * For ImageView: Returns the drawable's bitmap if available.
 * For other Views: Renders the view to a new bitmap.
 *
 * @return Bitmap of the view, or null if the view has no dimensions
 */
fun View.toBitmap(): Bitmap? {
    // Special case for ImageView - try to get bitmap directly
    if (this is ImageView) {
        val drawable = this.drawable
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }
    }

    // Check if view has valid dimensions
    if (width <= 0 || height <= 0) {
        return null
    }

    // Create bitmap and render view to it
    return try {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        // Clear focus to get clean render
        clearFocus()

        // Draw view to canvas
        draw(canvas)

        bitmap
    } catch (e: OutOfMemoryError) {
        e.printStackTrace()
        null
    }
}

/**
 * Safely creates a bitmap with the given dimensions.
 * Includes retry logic with garbage collection on OutOfMemoryError.
 *
 * @param width Width in pixels
 * @param height Height in pixels
 * @param config Bitmap configuration
 * @param retryCount Number of times to retry on OOM (default: 1)
 * @return Created bitmap, or null if creation failed
 */
fun createBitmapSafely(
    width: Int,
    height: Int,
    config: Bitmap.Config = Bitmap.Config.ARGB_8888,
    retryCount: Int = 1
): Bitmap? {
    return try {
        Bitmap.createBitmap(width, height, config)
    } catch (e: OutOfMemoryError) {
        e.printStackTrace()
        if (retryCount > 0) {
            // Force garbage collection and retry
            System.gc()
            createBitmapSafely(width, height, config, retryCount - 1)
        } else {
            null
        }
    }
}
