package com.hackvlc.cropcompanion.ui

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.core.graphics.ColorUtils

class AlarmFrameLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    private val alertColor = Color.RED

    private val animator = ObjectAnimator.ofInt(this, "foreground", 0, 125).apply {
        this.duration = 500
        this.repeatCount = 5
        repeatMode = ObjectAnimator.REVERSE
    }

    fun setForeground(alpha: Int) {
        applyAlpha(alpha)
    }

    fun startBlink() {
        applyAlpha(0)
        animator.start()
    }

    fun stopBlink() {
        applyAlpha(0)
        animator.cancel()
    }

    private fun applyAlpha(alpha: Int) {
        foreground = ColorDrawable(ColorUtils.setAlphaComponent(alertColor, alpha))
    }
}