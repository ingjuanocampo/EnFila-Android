import Android.compileAndroidSdkVersion
import Android.minAndroidSdkVersion
import base.implementation
import dependencies.*

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
    id ("com.google.gms.google-services")
}

android {
    namespace = "com.ingjuanocampo.enfila.android"

    compileSdk = compileAndroidSdkVersion
    defaultConfig {
        applicationId = "com.ingjuanocampo.enfila.android"

        minSdk = minAndroidSdkVersion
        targetSdk = compileAndroidSdkVersion
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
        compose = true
    }

    // For Kotlin projects
    kotlinOptions {
        jvmTarget = "17"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.8"
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
    appDependencies()
    implementation(platform(AppDependencies.firebaseBom))
    fireStore()
    dataStore()
    coroutinesWithAndroid()
    architectureComponents()
}

