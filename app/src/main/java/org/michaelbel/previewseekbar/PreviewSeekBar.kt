package org.michaelbel.previewseekbar

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.SeekBar
import androidx.appcompat.widget.AppCompatSeekBar
import androidx.appcompat.widget.AppCompatTextView
import kotlin.math.roundToLong

class PreviewSeekBar(
    context: Context,
    attrs: AttributeSet
): AppCompatSeekBar(context, attrs), SeekBar.OnSeekBarChangeListener {

    var onPreviewTextChanged: (seekBar: PreviewSeekBar, progress: Int) -> String = { _, _ -> "" }

    private val previewWidth: Float
        get() = resources.getDimension(R.dimen.preview_width)
    private val previewHeight: Float
        get() = resources.getDimension(R.dimen.preview_height)
    private val previewMargin: Float
        get() = resources.getDimension(R.dimen.preview_margin)

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
        )
        previewPopup?.animationStyle = R.style.SeekBarPreviewAnimation
        setOnSeekBarChangeListener(this)
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {
        if (previewPopup?.isShowing == false) {
            previewPopup?.showAsDropDown(this, getXPosition(this), getYPosition(this))
        }
    }

    override fun onStopTrackingTouch(seekBar: SeekBar) {
        if (previewPopup?.isShowing == true) {
            previewPopup?.dismiss()
        }
    }

    override fun onProgressChanged(seekBar: SeekBar, progress: Int, b: Boolean) {
        previewPopupText?.text = onPreviewTextChanged(this, getProgress())
        previewPopup?.update(
            seekBar,
            getXPosition(seekBar),
            getYPosition(seekBar),
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    private fun getXPosition(seekBar: SeekBar): Int {
        val width: Long = seekBar.width.toDouble().roundToLong() - seekBar.paddingLeft - seekBar.paddingRight
        val seekMax: Int = if (seekBar.max == 0) seekBar.progress else seekBar.max
        val thumbPos: Long = seekBar.paddingLeft + width * seekBar.progress / if (seekMax == 0) 1 else seekMax
        return thumbPos.toInt() - previewWidth.toInt() / 2
    }

    private fun getYPosition(seekBar: SeekBar): Int {
        return -(seekBar.height + previewHeight.toInt() + previewMargin.toInt())
    }
}