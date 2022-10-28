import dependencies.ANDROID_33
import org.gradle.api.artifacts.dsl.DependencyHandler
import dependencies.*

object Android {
    const val compileAndroidSdkVersion = ANDROID_33
    const val minAndroidSdkVersion = 26
}

fun DependencyHandler.appDependencies() {
    uiCommons()
    material()
    navigationComponent()
}