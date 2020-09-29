// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {

    repositories {
        google()
        jcenter()
    }

    dependencies {
        classpath(com.darabi.mohammad.buildsrc.Dependencies.TOOLS_PLUGIN)
        classpath(com.darabi.mohammad.buildsrc.Dependencies.KOTLIN_PLUGIN)

        // /NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle.kts.kts.kts.kts files
    }
}

allprojects {
    repositories {
        google()
        jcenter()
    }
}