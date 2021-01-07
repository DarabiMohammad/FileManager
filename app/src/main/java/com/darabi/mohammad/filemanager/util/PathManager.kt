package com.darabi.mohammad.filemanager.util

import javax.inject.Inject

class PathManager @Inject constructor() {

    private val paths by lazy { arrayListOf<String>() }

    fun addPath(path: String): Boolean =
        if (paths.lastOrNull() == path)
            false
        else
            paths.add(path)

    fun pervousPath(): String? = paths.run {
        removeLast()
        lastOrNull()
    }

    fun lastPath(): String = paths.last()
}