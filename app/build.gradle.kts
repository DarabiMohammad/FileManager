import com.darabi.mohammad.buildsrc.Dependencies.dependencies
import com.darabi.mohammad.buildsrc.Application

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
}

android {
    
    compileSdkVersion(Application.COMPILE_SDK_VERSION)

    defaultConfig {
        applicationId = Application.ID
        minSdkVersion(Application.MIN_SDK_VERSION)
        targetSdkVersion(Application.TARGET_SDK_VERSION)
        versionCode = Application.VERSION_CODE
        versionName = Application.VERSION_NAME

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true

        manifestPlaceholders[Application.DOCUMENTS_AUTHORITY] = Application.DOCUMENTS_AUTHORITY_VALUE

        buildConfigField("String", Application.DOCUMENTS_AUTHORITY, "\"${Application.DOCUMENTS_AUTHORITY_VALUE}\"")
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
        getByName("debug") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    dependencies()
}