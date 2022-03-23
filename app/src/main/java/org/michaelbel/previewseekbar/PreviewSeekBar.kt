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

class PreviewSeekBar(
    context: Context,
    attrs: AttributeSet
): AppCompatSeekBar(context, attrs), SeekBar.OnSeekBarChangeListener {

    var onPreviewTextChanged: (progress: Int) -> String = { "" }

    private val previewWidth: Float
        get() = resources.getDimension(R.dimen.preview_width)
    private val previewHeight: Float
        get() = resources.getDimension(R.dimen.preview_height)
    private val previewMargin: Float
        get() = resources.getDimension(R.dimen.preview_margin)

    private val xPosition: Int
        get() {
            val width: Long = width.toDouble().roundToLong() - paddingLeft - paddingRight
            val seekMax: Int = if (max == 0) progress else max
            val thumbPos: Long = paddingLeft + width * progress / if (seekMax == 0) 1 else seekMax
            return thumbPos.toInt() - previewWidth.roundToInt() / 2
        }
    private val yPosition: Int
        get() = -(height + previewHeight.roundToInt() + previewMargin.roundToInt())

    private var previewPopup: PopupWindow? = null
    private var previewPopupText: AppCompatTextView? = null

    init {
        val contentView: View = View.inflate(context, R.layout.preview_seekbar, null)
        previewPopupText = contentView.findViewById(R.id.previewText)
        previewPopup = PopupWindow(
            contentView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            false
        ).also { popupWindow: PopupWindow ->
            popupWindow.animationStyle = R.style.SeekBarPreviewAnimation
        }
        setOnSeekBarChangeListener(this)
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {
        if (previewPopup?.isShowing == false) {
            previewPopup?.showAsDropDown(seekBar, xPosition, yPosition)
        }
    }

    override fun onStopTrackingTouch(seekBar: SeekBar) {
        if (previewPopup?.isShowing == true) {
            previewPopup?.dismiss()
        }
    }

    override fun onProgressChanged(seekBar: SeekBar, progress: Int, b: Boolean) {
        previewPopupText?.text = onPreviewTextChanged(progress)
        previewPopup?.update(
            seekBar,
            xPosition,
            yPosition,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
}