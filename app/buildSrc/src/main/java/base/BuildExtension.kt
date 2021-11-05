package base

import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.dsl.DependencyHandler

fun DependencyHandler.classpath(dependency: String): Dependency? =
    add("classpath", dependency)

fun DependencyHandler.implementation(dependency: String): Dependency? =
    add("implementation", dependency)

fun DependencyHandler.kotlinImplementation(dependency: String): Dependency? =
    add("kapt", dependency)

fun DependencyHandler.projectImplementation(dependency: String): Dependency? =
    add("implementation", project(mapOf("path" to dependency)))

fun DependencyHandler.testImplementation(dependencyNotation: String): Dependency? =
    add("testImplementation", dependencyNotation)

fun DependencyHandler.androidTestImplementation(dependencyNotation: String): Dependency? =
    add("androidTestImplementation", dependencyNotation)