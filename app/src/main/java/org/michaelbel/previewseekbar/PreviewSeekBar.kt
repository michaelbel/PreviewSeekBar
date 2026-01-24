package org.michaelbel.previewseekbar

import android.content.Context
import android.util.AttributeSet
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
            val trackWidth = width.toDouble().roundToLong() - paddingLeft - paddingRight
            val range = (max - min).coerceAtLeast(1)
            val clampedProgress = (progress - min).coerceIn(0, range)
            val thumbPos = paddingLeft + trackWidth * clampedProgress / range
            val leftAligned = thumbPos.toInt() - previewWidth.roundToInt() / 2
            val rawX = if (layoutDirection == LAYOUT_DIRECTION_RTL) width - leftAligned - previewWidth.roundToInt() else leftAligned
            val maxX = (width - previewWidth.roundToInt()).coerceAtLeast(0)
            return rawX.coerceIn(0, maxX)
        }
    private val yPosition: Int
        get() = -(height + previewHeight.roundToInt() + previewMargin.roundToInt())

    private var previewPopup: PopupWindow by notNull()
    private var previewPopupText: AppCompatTextView by notNull()

    init {
        val contentView = inflate(context, R.layout.preview_seekbar, null)
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

    override fun onDetachedFromWindow() {
        if (previewPopup.isShowing) {
            previewPopup.dismiss()
        }
        super.onDetachedFromWindow()
    }
}
