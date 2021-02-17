package com.darabi.mohammad.filemanager.util

class SingleEventWrapper <T> constructor(private val content: T) {

    private var hasBennHandled = false

    fun getContentIfNotHandled(): T? = if (hasBennHandled) null else {
        hasBennHandled = true
        content
    }
}