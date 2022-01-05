import dependencies.ANDROID_OREO
import dependencies.ANDROID_RED_VELVELT
import org.gradle.api.artifacts.dsl.DependencyHandler
import dependencies.*

object Android {
    const val compileAndroidSdkVersion = ANDROID_RED_VELVELT
    const val minAndroidSdkVersion = ANDROID_OREO
}

fun DependencyHandler.appDependencies() {
    uiCommons()
    material()
    navigationComponent()
}