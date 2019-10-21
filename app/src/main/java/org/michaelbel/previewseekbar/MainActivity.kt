package org.michaelbel.previewseekbar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

class MainActivity: AppCompatActivity(R.layout.activity_main) {

    companion object {
        /**
         * 01:23:45
         */
        private const val TIME_MILLS = 5025000
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        previewSeekBar.max = TIME_MILLS
        previewSeekBar.addOnPreviewTextChangeListener(object: PreviewSeekBar.OnPreviewTextChangeListener {
            override fun onPreviewTextChanged(seekBar: PreviewSeekBar, progress: Int): String {
                val duration = seekBar.progress.toLong()

                val hours = TimeUnit.MILLISECONDS.toHours(duration)
                val minutes = TimeUnit.MILLISECONDS.toMinutes(duration) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(duration))
                val seconds = TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration))

                return String.format("%02d:%02d:%02d", hours, minutes, seconds)
            }
        })
    }
}