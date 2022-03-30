import dependencies.ANDROID_31
import org.gradle.api.artifacts.dsl.DependencyHandler
import dependencies.*

object Android {
    const val compileAndroidSdkVersion = ANDROID_31
    const val minAndroidSdkVersion = ANDROID_31
}

fun DependencyHandler.appDependencies() {
    uiCommons()
    material()
    navigationComponent()
}