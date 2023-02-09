import Android.compileAndroidSdkVersion
import Android.minAndroidSdkVersion
import dependencies.*

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
}

android {
    compileSdkVersion(compileAndroidSdkVersion)
    defaultConfig {
        minSdkVersion(minAndroidSdkVersion)
        targetSdkVersion(compileAndroidSdkVersion)
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    // For Kotlin projects
    kotlinOptions {
        jvmTarget = "1.8"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation ("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
    hilt()
    retrofit()

}