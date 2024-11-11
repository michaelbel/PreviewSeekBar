import org.apache.commons.io.output.ByteArrayOutputStream
import java.nio.charset.Charset

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

private val gitCommitsCount: Int by lazy {
    val stdout = ByteArrayOutputStream()
    exec {
        commandLine("git", "rev-list", "--count", "HEAD")
        standardOutput = stdout
    }
    stdout.toString(Charset.defaultCharset()).trim().toInt()
}

kotlin {
    compilerOptions {
        jvmToolchain(libs.versions.jdk.get().toInt())
    }
}

android {
    namespace = "org.michaelbel.previewseekbar"
    compileSdk = libs.versions.compile.sdk.get().toInt()

    defaultConfig {
        applicationId = "org.michaelbel.previewseekbar"
        minSdk = libs.versions.min.sdk.get().toInt()
        targetSdk = libs.versions.target.sdk.get().toInt()
        versionName = "1.0.0"
        versionCode = gitCommitsCount
        setProperty("archivesBaseName", "PreviewSeekBar-v$versionName($versionCode)")
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.google.material)
}