package org.michaelbel.previewseekbar

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.SeekBar
import androidx.appcompat.widget.AppCompatSeekBar
import androidx.appcompat.widget.AppCompatTextView
import kotlin.math.roundToInt
import kotlin.math.roundToLong
import kotlin.properties.Delegates.notNull

class PreviewSeekBar(
    context: Context,
    attrs: AttributeSet
): AppCompatSeekBar(context, attrs), SeekBar.OnSeekBarChangeListener {

    var onPreviewTextChanged: (progress: Int) -> String = { "" }

    private val previewWidth: Float by lazy { resources.getDimension(R.dimen.preview_width) }
    private val previewHeight: Float by lazy { resources.getDimension(R.dimen.preview_height) }
    private val previewMargin: Float by lazy { resources.getDimension(R.dimen.preview_margin) }

    private val xPosition: Int
        get() {
            val width = width.toDouble().roundToLong() - paddingLeft - paddingRight
            val seekMax = if (max == 0) progress else max
            val thumbPos = paddingLeft + width * progress / if (seekMax == 0) 1 else seekMax
            return thumbPos.toInt() - previewWidth.roundToInt() / 2
        }
    private val yPosition: Int
        get() = -(height + previewHeight.roundToInt() + previewMargin.roundToInt())

    private var previewPopup: PopupWindow by notNull()
    private var previewPopupText: AppCompatTextView by notNull()

    init {
        val contentView = View.inflate(context, R.layout.preview_seekbar, null)
        previewPopupText = contentView.findViewById(R.id.previewText)
        previewPopup = PopupWindow(
            contentView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            false
        ).apply {
            animationStyle = R.style.SeekBarPreviewAnimation
        }
        setOnSeekBarChangeListener(this)
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {
        if (previewPopup.isShowing) return
        previewPopup.showAsDropDown(seekBar, xPosition, yPosition)
    }

    override fun onStopTrackingTouch(seekBar: SeekBar) {
        if (!previewPopup.isShowing) return
        previewPopup.dismiss()
    }

    override fun onProgressChanged(seekBar: SeekBar, progress: Int, b: Boolean) {
        previewPopupText.text = onPreviewTextChanged(progress)
        previewPopup.update(
            seekBar,
            xPosition,
            yPosition,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
}