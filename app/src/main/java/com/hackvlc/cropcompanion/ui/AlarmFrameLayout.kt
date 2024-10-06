package com.hackvlc.cropcompanion.ui

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.core.graphics.ColorUtils

class AlarmFrameLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    private val originalColor = Color.RED

    private var foregroundColor = ColorUtils.setAlphaComponent(originalColor, 0)

    private val animator = ObjectAnimator.ofInt(this, "foreground", 0, 255).apply {
        this.duration = 500
        this.repeatCount = 5
        repeatMode = ObjectAnimator.REVERSE
    }

    fun setForeground(alpha: Int) {
        applyAlpha(alpha)
        invalidate()
    }

    fun startBlink() {
        applyAlpha(0)
        animator.start()
    }

    fun stopBlink() {
        applyAlpha(0)
        animator.cancel()
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        canvas.drawColor(foregroundColor)
    }

    private fun applyAlpha(alpha: Int) {
        foregroundColor = ColorUtils.setAlphaComponent(foregroundColor, alpha)
    }
}