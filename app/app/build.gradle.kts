import Android.compileAndroidSdkVersion
import Android.minAndroidSdkVersion
import base.implementation
import dependencies.*

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
}

android {
    compileSdkVersion(compileAndroidSdkVersion)
    defaultConfig {
        applicationId = "com.ingjuanocampo.enfila.android"

        minSdkVersion(minAndroidSdkVersion)
        targetSdkVersion(compileAndroidSdkVersion)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }

    buildFeatures {
        viewBinding = true
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
    appDependencies()
    implementation(platform(Dependencies.firebaseBom))
    fireStore()
    dataStore()
    coroutinesWithAndroid()
    architectureComponents()

}

apply(plugin = "com.google.gms.google-services")