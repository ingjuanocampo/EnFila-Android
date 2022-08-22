import dependencies.ANDROID_32
import org.gradle.api.artifacts.dsl.DependencyHandler
import dependencies.*

object Android {
    const val compileAndroidSdkVersion = ANDROID_32
    const val minAndroidSdkVersion = 26
}

fun DependencyHandler.appDependencies() {
    uiCommons()
    material()
    navigationComponent()
}