package com.darabi.mohammad.filemanager.util.path

import java.io.File
import javax.inject.Inject

class PathManager @Inject constructor() : BasePathManager {

    private val emptyString = ""

    private var path = mutableMapOf<String, Int>()

    private fun lastPath(): String = path.takeIf { it.isNotEmpty() }?.keys?.last() ?: emptyString

    private fun lastDirName(): String = lastPath().run { this.substring(this.lastIndexOf(File.separator) + 1) }

    private fun lastPosition(): Int = path[lastPath()] ?: 0

    override fun getPath(): Triple<String, String, Int> = Triple(lastPath(), lastDirName(), lastPosition()).also { path.remove(lastPath()) }

    override fun savePath(currentPath: String, currentPosition: Int) {
        path[currentPath] = currentPosition
    }
}