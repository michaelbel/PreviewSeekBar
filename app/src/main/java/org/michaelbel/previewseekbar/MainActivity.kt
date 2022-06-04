package org.michaelbel.previewseekbar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates
import kotlin.properties.Delegates.notNull
import org.michaelbel.previewseekbar.databinding.ActivityMainBinding

class MainActivity: AppCompatActivity(R.layout.activity_main) {

    private var binding: ActivityMainBinding by notNull()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.previewSeekBar.run {
            max = TIME_MILLS
            onPreviewTextChanged = ::previewTextChanged
        }
    }

    private fun previewTextChanged(progress: Int): String {
        val duration: Long = progress.toLong()

        val hours: Long = TimeUnit.MILLISECONDS.toHours(duration)
        val minutes: Long = TimeUnit.MILLISECONDS.toMinutes(duration) -
                TimeUnit.HOURS.toMinutes(hours)
        val seconds: Long = TimeUnit.MILLISECONDS.toSeconds(duration) -
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration))

        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    private companion object {
        /**
         * 01:23:45
         */
        private const val TIME_MILLS: Int = 5025000
    }
}