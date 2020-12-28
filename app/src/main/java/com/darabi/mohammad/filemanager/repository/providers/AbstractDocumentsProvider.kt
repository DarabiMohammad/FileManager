package com.darabi.mohammad.filemanager.repository.providers

import android.provider.DocumentsProvider

abstract class AbstractDocumentsProvider : DocumentsProvider() {

    protected abstract val authority: String
    protected abstract val tag: String

    override fun onCreate(): Boolean = false
}