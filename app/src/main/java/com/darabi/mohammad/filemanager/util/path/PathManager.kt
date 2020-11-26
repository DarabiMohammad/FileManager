package com.darabi.mohammad.filemanager.util.path

import java.io.File
import javax.inject.Inject

class PathManager @Inject constructor() : BasePathManager {

    private var path = mutableMapOf<String, Int>()
    private val pathSeparatorChar = File.pathSeparatorChar

    override fun lastPath(): String = path.keys.last()

    override fun lastDirName(): String = lastPath().run {
        this.substring(this.lastIndexOf(File.separator) + 1)
    }

    override fun lastPosition(): Int = path[lastPath()] ?: 0

    override fun saveCurrentLocation(currentPath: String, currentPosition: Int) {
        path[currentPath] = currentPosition
    }
}