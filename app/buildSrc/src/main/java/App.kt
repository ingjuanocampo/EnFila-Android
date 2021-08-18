import dependencies.ANDROID_LOLLIPOP
import dependencies.ANDROID_RED_VELVELT
import org.gradle.api.artifacts.dsl.DependencyHandler
import dependencies.*

object Android {
    const val compileAndroidSdkVersion = ANDROID_RED_VELVELT
    const val minAndroidSdkVersion = ANDROID_LOLLIPOP
}

fun DependencyHandler.appDependencies() {
    uiCommons()
    material()
}