package com.hackvlc.cropcompanion.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import com.hackvlc.cropcompanion.R
import com.marcinmoskala.arcseekbar.ArcSeekBar
import com.marcinmoskala.arcseekbar.ProgressListener

class SeekProgressView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    var progressListener: (Int) -> Unit = {}

    lateinit var seekBar : ArcSeekBar

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.seek_bar_layout, this, true)
        val text = view.findViewById<TextView>(R.id.text)

        seekBar = findViewById(R.id.seek)

        seekBar.onProgressChangedListener =
            ProgressListener { progress ->
                progressListener(progress)
                text.text = progress.toString()
            }

        text.text = seekBar.progress.toString()

    }

    fun addProgressListener(listener: (Int) -> Unit) {
        progressListener = listener
    }
}