package com.darabi.mohammad.buildsrc

import org.gradle.api.artifacts.dsl.DependencyHandler
import java.util.*
import kotlin.collections.ArrayList

object Application {

    //Core Infos
    private const val MIN_SDK_VERSION = 19
    private const val TARGET_SDK_VERSION = 30
    const val ID = "com.darabi.mohammad.filemanager"
    const val VERSION_CODE = 1
    const val VERSION_NAME = "1.0.0"
    const val COMPILE_SDK_VERSION = TARGET_SDK_VERSION

    //Providers
    private const val PROVIDER_SUFFIX = ".documents"
    const val PRIMARY_EXT_STPRAGE_PROVIDER = "PRIMARY_EXTERNAL_STPRAGE_PROVIDER"
    const val PRIMARY_EXTERNAL_STPRAGE_PROVIDER_AUTHORITY = "${ID}.primary_external_storage$PROVIDER_SUFFIX"

    object BuildTypes {
        const val DEBUG = "debug"
        const val RELEASE = "release"
    }

    sealed class Flavor {

        companion object {
            const val DIMENSION = "default"
        }

        abstract val minSdkVersion: Int
        abstract val targetSdkVersion: Int

        val flavorName: String by lazy { javaClass.simpleName.toLowerCase(Locale.ROOT) }
        val applicationId: String by lazy { "$ID.$flavorName" }
        val srcDirPath: ArrayList<String> by lazy { arrayListOf("src\\$flavorName\\java") }
        val resDirPath: ArrayList<String> by lazy { arrayListOf("src\\$flavorName\\res") }

        object Api22 : Flavor() {
            override val minSdkVersion = MIN_SDK_VERSION
            override val targetSdkVersion = 22
        }

        abstract class SharedSrc : Flavor() {

            val sharedSrcDirPath: String by lazy { "src\\sharedsrc\\java" }
            val sharedResDirPath: String by lazy { "src\\sharedsrc\\res" }

            init {
                srcDirPath.add(sharedSrcDirPath)
                resDirPath.add(sharedResDirPath)
            }

            object Api29 : SharedSrc() {
                override val minSdkVersion = 23
                override val targetSdkVersion = 29
            }

            object Api30 : SharedSrc() {
                override val minSdkVersion: Int get() = 30
                override val targetSdkVersion: Int get() = 30
            }
        }
    }
}

object Plugin {

    //Android Gradle Plugin Version
    private const val AGP_VERSION = "4.1.1"

    //Kotlin Plugin Version
    private const val KOTLIN_PLUGIN_VERSION = "1.4.10"

    const val ANDROID_GRADLE_PLUGIN = "com.android.tools.build:gradle:${AGP_VERSION}"
    const val KOTLIN_PLUGIN = "org.jetbrains.kotlin:kotlin-gradle-plugin:${KOTLIN_PLUGIN_VERSION}"
}

object Dependencies {

    private const val IMPLEMENTATION = "implementation"
    private const val KAPT = "kapt"

    private const val CORE_VERSION = "1.3.1"
    private const val APPCOMPAT_VERSION = "1.1.0"
    private const val COROUTINES_VERSION = "1.3.3"
    private const val CONSTRAINT_VERSION = "2.0.1"
    private const val MATERIAL_VERSION = "1.2.1"
    private const val LIFECYCLE_VERSION = "2.2.0"
    private const val DAGGER_VERSION = "2.27"
    private const val GLIDE_VERSION = "4.11.0"

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
        "androidx.lifecycle:lifecycle-livedata-ktx:${LIFECYCLE_VERSION}",
        "com.github.bumptech.glide:glide:${GLIDE_VERSION}"
    )

    private val proccessors = listOf (
        "com.google.dagger:dagger-android-processor:${DAGGER_VERSION}",
        "com.google.dagger:dagger-compiler:${DAGGER_VERSION}",
        "com.github.bumptech.glide:compiler:${GLIDE_VERSION}"
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