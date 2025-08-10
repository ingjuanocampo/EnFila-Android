// Version catalog provides all dependency management
// No imports needed from buildSrc dependencies package

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    kotlin("kapt")
    alias(libs.plugins.hilt)
    alias(libs.plugins.google.services)
    alias(libs.plugins.ktlint)
}

android {
    namespace = "com.ingjuanocampo.enfila.android"

    compileSdk = libs.versions.compileSdk.get().toInt()
    defaultConfig {
        applicationId = "com.ingjuanocampo.enfila.android"

        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
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
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }
    lint {
        baseline = file("lint-baseline.xml")
    }
}

dependencies {
    implementation(
        fileTree("libs") {
            include("*.jar")
        },
    )

    // Kotlin
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlin.coroutines.core)
    implementation(libs.kotlin.coroutines.android)

    // Android Core
    implementation(libs.android.core)
    implementation(libs.android.appcompat)
    implementation(libs.android.legacy)
    implementation(libs.constraintlayout)
    implementation(libs.recyclerview)
    implementation(libs.fragments)

    // UI & Material Design
    implementation(libs.material)

    // Compose
    implementation(libs.compose.ui)
    implementation(libs.compose.activity)
    implementation(libs.compose.viewmodel)
    implementation(libs.compose.tooling)
    implementation(libs.compose.foundation)
    implementation(libs.compose.material)
    implementation(libs.compose.material3)
    implementation(libs.compose.material3.window)
    implementation(libs.compose.icons.core)
    implementation(libs.compose.icons.extended)
    implementation(libs.compose.livedata)
    implementation(libs.compose.constraintlayout)

    // Architecture Components
    implementation(libs.lifecycle.viewmodel)
    implementation(libs.lifecycle.extensions)
    implementation(libs.lifecycle.runtime)
    implementation(libs.lifecycle.livedata)

    // Navigation
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)

    // Dependency Injection
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.config)

    // Google Services
    implementation(libs.google.auth)
    implementation(libs.google.safetynet)

    // Data Storage
    implementation(libs.datastore)
    implementation(libs.gson)

    // Custom Libraries
    implementation(libs.compose.adapter)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.android.test.runner)
    androidTestImplementation(libs.android.test.espresso)

    // Modules
    implementation(project(":data"))
}
