package dependencies

import base.*
import org.gradle.api.artifacts.dsl.DependencyHandler

/**
 * AppDependencies - Simplified approach for version catalog integration
 * 
 * Modern Gradle practices recommend using version catalogs directly in build files
 * rather than through buildSrc. This file now provides minimal helper functions
 * while the actual dependencies are managed through libs.versions.toml
 */

// Helper function to add project dependencies
fun DependencyHandler.projectImplementation(path: String) {
    add("implementation", project(mapOf("path" to path)))
}

// Simple grouping functions - the actual dependencies will be added in build files using version catalog
fun DependencyHandler.commonAndroidDependencies() {
    // Base Android dependencies
    // implementation(libs.android.core)
    // implementation(libs.android.appcompat)  
    // implementation(libs.material)
    // implementation(libs.constraintlayout)
    // implementation(libs.recyclerview)
    // implementation(libs.fragments)
}

fun DependencyHandler.composeBundle() {
    // Compose dependencies bundle
    // implementation(libs.bundles.compose)
}

fun DependencyHandler.lifecycleBundle() {
    // Lifecycle dependencies
    // implementation(libs.bundles.lifecycle)
}

fun DependencyHandler.networkingBundle() {
    // Networking dependencies  
    // implementation(libs.bundles.retrofit)
}

fun DependencyHandler.firebaseBundle() {
    // Firebase dependencies
    // implementation(platform(libs.firebase.bom))
    // implementation(libs.bundles.firebase)
}

fun DependencyHandler.hiltDependencies() {
    // Hilt dependencies
    // implementation(libs.hilt.android)
    // kapt(libs.hilt.compiler)
}

fun DependencyHandler.testingBundle() {
    // Testing dependencies
    // implementation(libs.bundles.testing)
}

/**
 * MIGRATION COMPLETED: Version Catalog Integration
 * 
 * This file now serves as a helper for grouping dependencies.
 * The actual dependency declarations should be done directly in build files using:
 * 
 * dependencies {
 *     implementation(libs.dependency.name)
 *     implementation(libs.bundles.bundle.name)
 *     implementation(platform(libs.firebase.bom))
 * }
 * 
 * This follows modern Gradle best practices and provides better IDE support.
 */