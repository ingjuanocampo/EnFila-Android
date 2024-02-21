import org.gradle.api.artifacts.dsl.DependencyHandler
import dependencies.*

object Android {
    const val compileAndroidSdkVersion = 34
    const val minAndroidSdkVersion = 26
}

fun DependencyHandler.appDependencies() {
    uiCommons()
    material()
    navigationComponent()
    hilt()
}