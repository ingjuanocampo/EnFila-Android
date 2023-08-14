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
    namespace = "com.ingjuanocampo.enfila.data"

    compileSdk = compileAndroidSdkVersion
    defaultConfig {
        minSdk = minAndroidSdkVersion
        targetSdk = compileAndroidSdkVersion
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    // For Kotlin projects
    kotlinOptions {
        jvmTarget = "17"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
    hilt()
    retrofit()
}
