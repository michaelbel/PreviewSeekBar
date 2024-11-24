package org.michaelbel.previewseekbar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates.notNull
import org.michaelbel.previewseekbar.databinding.ActivityMainBinding
import java.util.Locale

class MainActivity: ComponentActivity(R.layout.activity_main) {

    private var binding: ActivityMainBinding by notNull()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.previewSeekBar.run {
            max = TIME_MILLS
            onPreviewTextChanged = ::previewTextChanged
        }
    }

    private fun previewTextChanged(progress: Int): String {
        val duration = progress.toLong()
        val hours = TimeUnit.MILLISECONDS.toHours(duration)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(duration) - TimeUnit.HOURS.toMinutes(hours)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration))
        return String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds)
    }

    private companion object {
        /**
         * 01:23:45
         */
        private const val TIME_MILLS = 5025000
    }
}