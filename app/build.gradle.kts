import java.nio.charset.StandardCharsets

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

private val gitCommitsCount: Int by lazy {
    try {
        val isWindows = System.getProperty("os.name").contains("Windows", ignoreCase = true)
        val processBuilder = when {
            isWindows -> ProcessBuilder("cmd", "/c", "git", "rev-list", "--count", "HEAD")
            else -> ProcessBuilder("git", "rev-list", "--count", "HEAD")
        }
        processBuilder.redirectErrorStream(true)
        processBuilder.start().inputStream.bufferedReader(StandardCharsets.UTF_8).readLine().trim().toInt()
    } catch (_: Exception) {
        1
    }
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
        versionCode = gitCommitsCount
        versionName = "1.0.0"
    }

    buildFeatures {
        viewBinding = true
    }
}

base {
    archivesName.set("PreviewSeekBar-v${android.defaultConfig.versionName}(${android.defaultConfig.versionCode})")
}

dependencies {
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.google.material)
}

tasks.register("printVersionName") { doLast { println(android.defaultConfig.versionName) } }
tasks.register("printVersionCode") { doLast { println(android.defaultConfig.versionCode.toString()) } }