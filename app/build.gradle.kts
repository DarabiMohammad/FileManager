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
        versionCode = Application.VERSION_CODE
        versionName = Application.VERSION_NAME

        vectorDrawables.useSupportLibrary = true
    }

    buildTypes {
        getByName(Application.BuildTypes.RELEASE) {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
        getByName(Application.BuildTypes.DEBUG) {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }

    flavorDimensions(Application.Flavor.DIMENSION)

    productFlavors {

        fun createFlavor(flavor: Application.Flavor) {

            create(flavor.flavorName) {

                applicationId = flavor.applicationId
                minSdkVersion(flavor.minSdkVersion)
                targetSdkVersion(flavor.targetSdkVersion)
            }
        }

        createFlavor(Application.Flavor.Api22)
        createFlavor(Application.Flavor.SharedSrc.Api29)
        createFlavor(Application.Flavor.SharedSrc.Api30)
    }

    sourceSets {

        fun setSourceSets(flavor: Application.Flavor) {
            getByName(flavor.flavorName) {
                flavor.srcDirPath.forEach { java.srcDirs(it) }
                flavor.resDirPath.forEach { res.srcDirs(it) }
            }
        }

        setSourceSets(Application.Flavor.Api22)
        setSourceSets(Application.Flavor.SharedSrc.Api29)
        setSourceSets(Application.Flavor.SharedSrc.Api30)
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    dependencies()
}