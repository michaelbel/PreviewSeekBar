package org.michaelbel.previewseekbar

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.PopupWindow
import android.widget.SeekBar
import androidx.appcompat.widget.AppCompatSeekBar
import androidx.appcompat.widget.AppCompatTextView
import kotlin.math.roundToLong

class PreviewSeekBar(context: Context, attrs: AttributeSet): AppCompatSeekBar(context, attrs), SeekBar.OnSeekBarChangeListener {

    interface OnPreviewTextChangeListener {
        fun onPreviewTextChanged(seekBar: PreviewSeekBar, progress: Int): String
    }

    private var previewWidth = 0F
    private var previewHeight = 0F
    private var previewMargin = 0F

    private var previewPopup: PopupWindow? = null
    private var previewPopupText: AppCompatTextView? = null
    private var previewTextChangeListener: OnPreviewTextChangeListener? = null

    init {
        setOnSeekBarChangeListener(this)

        val parent = View.inflate(context, R.layout.preview_seekbar, null)

        previewWidth = resources.getDimension(R.dimen.preview_width)
        previewHeight = resources.getDimension(R.dimen.preview_height)
        previewMargin = resources.getDimension(R.dimen.preview_margin)

        previewPopupText = parent.findViewById(R.id.text)

        previewPopup = PopupWindow(parent, WRAP_CONTENT, WRAP_CONTENT, false)
        previewPopup?.animationStyle = R.style.SeekBarPreviewAnimation
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {
        showPopup()
    }

    override fun onStopTrackingTouch(seekBar: SeekBar) {
        hidePopup()
    }

    override fun onProgressChanged(seekBar: SeekBar, progress: Int, b: Boolean) {
        val previewText = previewTextChangeListener?.onPreviewTextChanged(this, getProgress())
        previewPopupText?.text = previewText ?: progress.toString()
        updatePopup(seekBar)
    }

    fun addOnPreviewTextChangeListener(listener: OnPreviewTextChangeListener) {
        previewTextChangeListener = listener
    }

    private fun showPopup() {
        if (previewPopup?.isShowing == false) {
            previewPopup?.showAsDropDown(this, getXPosition(this), getYPosition(this))
        }
    }

    private fun hidePopup() {
        if (previewPopup?.isShowing == true) {
            previewPopup?.dismiss()
        }
    }

    private fun updatePopup(seekBar: SeekBar) {
        previewPopup?.update(seekBar, getXPosition(seekBar), getYPosition(seekBar), WRAP_CONTENT, WRAP_CONTENT)
    }

    private fun getXPosition(seekBar: SeekBar): Int {
        val width = seekBar.width.toDouble().roundToLong() - seekBar.paddingLeft - seekBar.paddingRight
        val seekMax = if (seekBar.max == 0) seekBar.progress else seekBar.max
        val thumbPos = seekBar.paddingLeft + width * seekBar.progress / if (seekMax == 0) 1 else seekMax
        return thumbPos.toInt() - previewWidth.toInt() / 2
    }

    private fun getYPosition(seekBar: SeekBar): Int {
        return -(seekBar.height + previewHeight.toInt() + previewMargin.toInt())
    }
}