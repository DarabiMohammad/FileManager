package com.darabi.mohammad.filemanager.util

import javax.inject.Inject
import kotlin.jvm.Throws

class PathManager @Inject constructor() {

    private val paths by lazy { arrayListOf<String>() }

    fun addPath(path: String): Boolean =
        if (paths.lastOrNull() == path)
            throw IllegalArgumentException()
        else
            paths.add(path)

    fun pervousPath(): String? = paths.run {
        removeLastOrNull()
        lastOrNull()
    }

    fun lastPath(): String? = paths.lastOrNull()
}