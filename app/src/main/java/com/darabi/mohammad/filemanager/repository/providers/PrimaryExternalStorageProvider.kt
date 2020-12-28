package com.darabi.mohammad.filemanager.repository.providers

import android.database.Cursor
import android.database.MatrixCursor
import android.os.CancellationSignal
import android.os.ParcelFileDescriptor
import com.darabi.mohammad.filemanager.BuildConfig

class PrimaryExternalStorageProvider : StoragesProvider() {

    override val authority: String by lazy { "BuildConfig.PRIMARY_EXTERNAL_STPRAGE_PROVIDER" }
    override val tag: String by lazy { this.javaClass.simpleName }

    override fun onCreate(): Boolean {
        return true
    }

    override fun queryRoots(projection: Array<out String>?): Cursor {
        val cuesor = MatrixCursor(projection)
        val pro = projection
        return cuesor
    }

    override fun queryDocument(documentId: String?, projection: Array<out String>?): Cursor {
        throw UnsupportedOperationException("E")
    }

    override fun queryChildDocuments(
        parentDocumentId: String?, projection: Array<out String>?, sortOrder: String?
    ): Cursor {
        throw UnsupportedOperationException("E")
    }

    override fun openDocument(
        documentId: String?, mode: String?, signal: CancellationSignal?
    ): ParcelFileDescriptor {
        throw UnsupportedOperationException("E")
    }
}