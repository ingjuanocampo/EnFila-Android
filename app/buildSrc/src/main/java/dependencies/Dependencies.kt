package dependencies

import base.*
import org.gradle.api.artifacts.dsl.DependencyHandler

object Dependencies {

    const val coroutineCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion"
    const val coroutineAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion"

    const val dagger = "com.google.dagger:dagger:$daggerVersion"
    const val daggerCompiler = "com.google.dagger:dagger-compiler:$daggerVersion"
    const val daggerAndroidSupport = "com.google.dagger:dagger-android-support:$daggerVersion"
    const val daggerAndroidProcessor = "com.google.dagger:dagger-android-processor:$daggerVersion"

    const val picasso = "com.squareup.picasso:picasso:$picassoVersion"

    const val retrofit = "com.squareup.retrofit2:retrofit:$retrofitVersion"
    const val gsonConverter = "com.squareup.retrofit2:converter-gson:$retrofitVersion"
    const val gson = "com.google.code.gson:gson:2.8.8"


    const val extensions =  "androidx.lifecycle:lifecycle-extensions:$architectureComponentVersion"
    const val viewModel =  "androidx.lifecycle:lifecycle-viewmodel-ktx:$architectureComponentVersion"
    const val liveDataLifeCycle = "androidx.lifecycle:lifecycle-runtime-ktx:$architectureComponentVersion"
    const val liveData = "androidx.lifecycle:lifecycle-livedata-ktx:$architectureComponentVersion"

    const val kotlinStdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlinVersion"
    const val appCompat = "androidx.appcompat:appcompat:$androidXVersion"
    const val androidCore = "androidx.core:core-ktx:$androidXVersion"
    const val constraitLayout = "androidx.constraintlayout:constraintlayout:$constraintLayoutVersion"
    const val recyclerView = "androidx.recyclerview:recyclerview:$recyclerViewXVersion"

    const val androidXLegacy = "androidx.legacy:legacy-support-v4:$androidLegacyXVersion"
    const val androidMaterial = "com.google.android.material:material:$materialVersion"

    const val navigationAndroid = "androidx.navigation:navigation-fragment-ktx:$navigationComponent"
    const val navigationUI = "androidx.navigation:navigation-ui-ktx:$navigationComponent"

    const val composeAdapter = "com.github.ingjuanocampo:CompositeDelegateAdapter:1.0.3"

    const val firebaseBom = "com.google.firebase:firebase-bom:28.4.2"
    const val firebaseAuth = "com.google.firebase:firebase-auth:21.0.1"
    const val firebaseStore = "com.google.firebase:firebase-firestore-ktx:23.0.4"


    const val dataStore = "androidx.datastore:datastore-preferences:1.0.0"

}

object Android {
    const val compileAndroidSdkVersion = ANDROID_RED_VELVELT
    const val minAndroidSdkVersion = ANDROID_LOLLIPOP
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
    implementation(Dependencies.retrofit)
    implementation(Dependencies.gsonConverter)
    //implementation(Dependencies.gson)
}

fun DependencyHandler.dataStore() {
    implementation(Dependencies.dataStore)
   // implementation(Dependencies.gson)
}

fun DependencyHandler.fireStore() {
    implementation(Dependencies.firebaseBom)
    implementation(Dependencies.firebaseAuth)
    implementation(Dependencies.firebaseStore)

}

fun DependencyHandler.dagger() {
    implementation(Dependencies.dagger)
    kotlinImplementation(Dependencies.daggerCompiler)
}

fun DependencyHandler.coroutines() {
    implementation(Dependencies.coroutineCore)
}

fun DependencyHandler.navigationComponent() {
    implementation(Dependencies.navigationAndroid)
    implementation(Dependencies.navigationUI)
}

fun DependencyHandler.material() {
    implementation(Dependencies.androidMaterial)
    implementation(Dependencies.androidXLegacy)
}

fun DependencyHandler.coroutinesWithAndroid() {
    coroutines()
    implementation(Dependencies.coroutineAndroid)
}

fun DependencyHandler.daggerWithAndroid() {
    dagger()
    implementation(Dependencies.daggerAndroidSupport)
    kotlinImplementation(Dependencies.daggerAndroidProcessor)
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
    implementation(Dependencies.viewModel)
    implementation(Dependencies.extensions)
    implementation(Dependencies.liveDataLifeCycle)
    implementation(Dependencies.liveData)
}

fun DependencyHandler.uiCommons() {
    implementation(Dependencies.picasso)
    implementation(Dependencies.appCompat)
    implementation(Dependencies.kotlinStdlib)
    implementation(Dependencies.constraitLayout)
    implementation(Dependencies.recyclerView)
    implementation(Dependencies.androidCore)
    implementation(Dependencies.composeAdapter)
    projectImplementation(":data")

}







