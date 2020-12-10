package com.darabi.mohammad.filemanager.vm

import android.app.Application
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.model.Directory
import com.darabi.mohammad.filemanager.model.StorageItem
import com.darabi.mohammad.filemanager.util.storage.StorageManager
import com.darabi.mohammad.filemanager.util.storage.VolumeManager
import javax.inject.Inject

open class HomeViewModel @Inject constructor (
    private val app: Application,
    private val storageManager: StorageManager
) : BaseViewModel(app) {

    protected companion object {
        const val PRIMARY_EXTERNAL_STORAGE_ICON = R.drawable.ic_settings_black
        const val SECONDARY_EXTERNAL_STORAGE_ICON = R.drawable.ic_settings_black
        const val USB_OTG = R.drawable.ic_settings_black
        val DCIM = R.string.dcim to R.drawable.ic_settings_black
        val DOWNLOAD = R.string.download to R.drawable.ic_settings_black
        val MOVIES = R.string.movies to R.drawable.ic_settings_black
        val MUSICS = R.string.musics to R.drawable.ic_settings_black
        val PICTURES = R.string.pictures to R.drawable.ic_settings_black
    }

    fun getStorages(): List<StorageItem> = storageManager.storages()
}