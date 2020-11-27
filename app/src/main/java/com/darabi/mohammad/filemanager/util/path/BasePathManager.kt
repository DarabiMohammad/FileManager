package com.darabi.mohammad.filemanager.util.path

interface BasePathManager {

    fun getPath(): Triple<String, String, Int>

    fun savePath(currentPath: String, currentPosition: Int)
}