package com.darabi.mohammad.filemanager.util.path

interface BasePathManager {

    fun lastPath(): String

    fun lastDirName(): String

    fun lastPosition(): Int

    fun saveCurrentLocation(currentPath: String, currentPosition: Int)
}