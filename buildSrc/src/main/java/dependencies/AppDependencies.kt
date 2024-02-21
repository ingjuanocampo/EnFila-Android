package dependencies

import base.*
import org.gradle.api.artifacts.dsl.DependencyHandler

object AppDependencies {

    const val coroutineCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion"
    const val coroutineAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion"

    const val dagger = "com.google.dagger:dagger:$daggerVersion"
    const val daggerCompiler = "com.google.dagger:dagger-compiler:$daggerVersion"
    const val daggerAndroidSupport = "com.google.dagger:dagger-android-support:$daggerVersion"
    const val daggerAndroidProcessor = "com.google.dagger:dagger-android-processor:$daggerVersion"

    const val hiltAndroid = "com.google.dagger:hilt-android:$hiltVersion"
    const val hiltCompiler = "com.google.dagger:hilt-android-compiler:$hiltVersion"



    const val picasso = "com.squareup.picasso:picasso:$picassoVersion"

    const val retrofit = "com.squareup.retrofit2:retrofit:$retrofitVersion"
    const val gsonConverter = "com.squareup.retrofit2:converter-gson:$retrofitVersion"
    const val gson = "com.google.code.gson:gson:2.9.1"


    const val extensions =  "androidx.lifecycle:lifecycle-extensions:2.2.0"
    const val viewModel =  "androidx.lifecycle:lifecycle-viewmodel-ktx:$architectureComponentVersion"
    const val liveDataLifeCycle = "androidx.lifecycle:lifecycle-runtime-ktx:$architectureComponentVersion"
    const val liveData = "androidx.lifecycle:lifecycle-livedata-ktx:$architectureComponentVersion"

    const val kotlinStdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlinVersion"
    const val appCompat = "androidx.appcompat:appcompat:$androidXVersion"
    const val androidCore = "androidx.core:core-ktx:1.8.0"
    const val constraitLayout = "androidx.constraintlayout:constraintlayout:$constraintLayoutVersion"
    const val recyclerView = "androidx.recyclerview:recyclerview:$recyclerViewXVersion"


    const val androidXLegacy = "androidx.legacy:legacy-support-v4:$androidLegacyXVersion"
    const val androidMaterial = "com.google.android.material:material:$materialVersion"
    const val material3 = "androidx.compose.material3:material3:$composeMaterial3Version"
    const val material3Windows = "androidx.compose.material3:material3-window-size-class:$composeMaterial3Version"

    const val navigationAndroid = "androidx.navigation:navigation-fragment-ktx:$navigationComponent"
    const val navigationUI = "androidx.navigation:navigation-ui-ktx:$navigationComponent"

    const val composeAdapter = "com.github.ingjuanocampo:CompositeDelegateAdapter:1.0.3"

    const val firebaseBom = "com.google.firebase:firebase-bom:31.1.0"
    const val firebaseAuth = "com.google.firebase:firebase-auth-ktx"
    const val firebaseStore = "com.google.firebase:firebase-firestore-ktx"
    const val firebaseRemoteConfig = "com.google.firebase:firebase-config-ktx"
    const val googleAuthenticator = "com.google.android.gms:play-services-auth:20.6.0"



    const val dataStore = "androidx.datastore:datastore-preferences:1.0.0"

    const val constraintLayoutCompose = "androidx.constraintlayout:constraintlayout-compose:$constraintCompose"
    const val composeUi = "androidx.compose.ui:ui:$composeVersion"
    const val composeActivity = "androidx.activity:activity-compose:$composeVersion"
    const val composeViewModel = "androidx.lifecycle:lifecycle-viewmodel-compose:$composeVMVersion"
    // Tooling support (Previews, etc.)
    const val tooling = "androidx.compose.ui:ui-tooling:$composeVersion"
    // Foundation (Border, Background, Box, Image, Scroll, shapes, animations, etc.)
    const val foundationCompose = "androidx.compose.foundation:foundation:$composeVersion"
    // Material Design
    const val materialCompose = "androidx.compose.material:material:$composeVersion"
    // Material design icons
    const val iconsMaterialCompose = "androidx.compose.material:material-icons-core:$composeVersion"
    const val iconsMaterialComposeExtended = "androidx.compose.material:material-icons-extended:$composeVersion"
    // Integration with observables
    const val liveDataCompose = "androidx.compose.runtime:runtime-livedata:$composeVersion"
    const val fragments = "androidx.fragment:fragment-ktx:1.5.7"


}


object TestDependecies {
    const val junit = "junit:junit:$junitVersion"
    const val androidTest = "androidx.test:runner:$androidRunnerVersion"
    const val espresso = "androidx.test.espresso:espresso-core:$androidEspresso"

}

fun DependencyHandler.kotlinClassPath() {
    classpath("com.android.tools.build:gradle:$kotlinAndroidGradleVersion")
    classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
}

fun DependencyHandler.retrofit() {
    implementation(AppDependencies.retrofit)
    implementation(AppDependencies.gsonConverter)
    //implementation(Dependencies.gson)
}

fun DependencyHandler.dataStore() {
    implementation(AppDependencies.dataStore)
    implementation(AppDependencies.gson)
}

fun DependencyHandler.fireStore() {
    implementation(AppDependencies.firebaseAuth)
    implementation ("com.google.android.gms:play-services-safetynet:18.0.1")
    implementation(AppDependencies.firebaseStore)
    implementation(AppDependencies.firebaseRemoteConfig)
    implementation(AppDependencies.googleAuthenticator)
}

fun DependencyHandler.dagger() {
    implementation(AppDependencies.dagger)
    kotlinImplementation(AppDependencies.daggerCompiler)
}

fun DependencyHandler.hilt() {
    implementation(AppDependencies.hiltAndroid)
    kotlinImplementation(AppDependencies.hiltCompiler)
}

fun DependencyHandler.coroutines() {
    implementation(AppDependencies.coroutineCore)
}

fun DependencyHandler.navigationComponent() {
    implementation(AppDependencies.navigationAndroid)
    implementation(AppDependencies.navigationUI)
}

fun DependencyHandler.material() {
    implementation(AppDependencies.androidMaterial)
    implementation(AppDependencies.material3)
    implementation(AppDependencies.material3Windows)
    implementation(AppDependencies.androidXLegacy)
}

fun DependencyHandler.coroutinesWithAndroid() {
    coroutines()
    implementation(AppDependencies.coroutineAndroid)
}

fun DependencyHandler.daggerWithAndroid() {
    dagger()
    implementation(AppDependencies.daggerAndroidSupport)
    kotlinImplementation(AppDependencies.daggerAndroidProcessor)
}

fun DependencyHandler.testDependencies() {
    testImplementation(TestDependecies.junit)
}

fun DependencyHandler.testAndroidDependencies() {
    testDependencies()
    androidTestImplementation(TestDependecies.androidTest)
    androidTestImplementation(TestDependecies.espresso)
}

fun DependencyHandler.architectureComponents() {
    implementation(AppDependencies.viewModel)
    implementation(AppDependencies.extensions)
    implementation(AppDependencies.liveDataLifeCycle)
    implementation(AppDependencies.liveData)
}

fun DependencyHandler.uiCommons() {
    //implementation(Dependencies.picasso) non compatible with latest gradle
    implementation(AppDependencies.appCompat)
    implementation(AppDependencies.kotlinStdlib)
    implementation(AppDependencies.constraitLayout)
    implementation(AppDependencies.recyclerView)
    implementation(AppDependencies.androidCore)
    implementation(AppDependencies.composeAdapter)
    implementation(AppDependencies.fragments)
    compose()
    projectImplementation(":data")
}

fun DependencyHandler.compose() {
    implementation(AppDependencies.composeUi)
    implementation(AppDependencies.composeViewModel)
    implementation(AppDependencies.tooling)
    implementation(AppDependencies.foundationCompose)
    implementation(AppDependencies.materialCompose)
    implementation(AppDependencies.iconsMaterialCompose)
    implementation(AppDependencies.iconsMaterialComposeExtended)
    implementation(AppDependencies.liveDataCompose)
    implementation(AppDependencies.composeActivity)
    implementation(AppDependencies.constraintLayoutCompose)
}







