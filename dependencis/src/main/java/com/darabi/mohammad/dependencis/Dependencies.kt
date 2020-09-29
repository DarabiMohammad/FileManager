package com.darabi.mohammad.dependencis

import org.gradle.api.artifacts.dsl.DependencyHandler

object Dependencies {

    private const val IMPLEMENTATION = "implementation"
    private const val KAPT = "kapt"

    private const val KOTLIN_VERSION = "4.0.1"
    private const val CORE_VERSION = "1.3.1"
    private const val APPCOMPAT_VERSION = "1.1.0"
    private const val COROUTINES_VERSION = "1.3.3"
    private const val CONSTRAINT_VERSION = "2.0.1"
    private const val MATERIAL_VERSION = "1.2.1"
    private const val LIFECYCLE_VERSION = "2.2.0"
    private const val DAGGER_VERSION = "2.27"

    private val libs = listOf (
        // core libs
        "org.jetbrains.kotlin:kotlin-stdlib:${KOTLIN_VERSION}",
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
        for(dependecy in libs) {
            add(IMPLEMENTATION, dependecy)
        }
        proccessors.forEach { proccessor ->
            add(KAPT, proccessor)
        }
    }
}