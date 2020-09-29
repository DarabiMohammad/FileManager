package com.darabi.mohammad.buildsrc

import org.gradle.api.artifacts.dsl.DependencyHandler

object Application {
    const val ID = "com.darabi.mohammad.filemanager"
    const val VERSION_CODE = 1
    const val VERSION_NAME = "1.0.0"
    const val MIN_SDK_VERSION = 19
    const val TARGET_SDK_VERSION = 30
    const val COMPILE_SDK_VERSION = TARGET_SDK_VERSION
}

object Dependencies {

    private const val IMPLEMENTATION = "implementation"
    private const val KAPT = "kapt"

    private const val KOTLIN_VERSION = "4.0.1"
    private const val ANDROID_PLUGIN_VERSION = "1.4.10"
    private const val CORE_VERSION = "1.3.1"
    private const val APPCOMPAT_VERSION = "1.1.0"
    private const val COROUTINES_VERSION = "1.3.3"
    private const val CONSTRAINT_VERSION = "2.0.1"
    private const val MATERIAL_VERSION = "1.2.1"
    private const val LIFECYCLE_VERSION = "2.2.0"
    private const val DAGGER_VERSION = "2.27"

    const val TOOLS_PLUGIN = "com.android.tools.build:gradle:${KOTLIN_VERSION}"
    const val KOTLIN_PLUGIN = "org.jetbrains.kotlin:kotlin-gradle-plugin:${ANDROID_PLUGIN_VERSION}"

    private val libs = listOf (
        // core libs
//        "org.jetbrains.kotlin:kotlin-stdlib:${KOTLIN_VERSION}",
        "androidx.core:core-ktx:${CORE_VERSION}",
        "androidx.appcompat:appcompat:${APPCOMPAT_VERSION}",
        "androidx.activity:activity-ktx:${APPCOMPAT_VERSION}",
        "androidx.fragment:fragment-ktx:${APPCOMPAT_VERSION}",
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:${COROUTINES_VERSION}",
        "com.google.dagger:dagger:${DAGGER_VERSION}",
        "com.google.dagger:dagger-android-support:${DAGGER_VERSION}",
        // design libs
        "androidx.constraintlayout:constraintlayout:${CONSTRAINT_VERSION}",
        "com.google.android.material:material:${MATERIAL_VERSION}",
        // lifeccyle libs
        "androidx.lifecycle:lifecycle-viewmodel-ktx:${LIFECYCLE_VERSION}",
        "androidx.lifecycle:lifecycle-extensions:${LIFECYCLE_VERSION}",
        "androidx.lifecycle:lifecycle-livedata-ktx:${LIFECYCLE_VERSION}"
    )

    private val proccessors = listOf (
        "com.google.dagger:dagger-android-processor:${DAGGER_VERSION}",
        "com.google.dagger:dagger-compiler:${DAGGER_VERSION}"
    )

    fun DependencyHandler.dependencies() {
        libs.forEach { dependecy ->
            add(IMPLEMENTATION, dependecy)
        }
        proccessors.forEach { proccessor ->
            add(KAPT, proccessor)
        }
    }
}