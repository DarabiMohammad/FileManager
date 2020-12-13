package com.darabi.mohammad.filemanager.vm

import android.app.Application
import androidx.appcompat.widget.LinearLayoutCompat
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.model.*
import com.darabi.mohammad.filemanager.util.storage.StorageManager
import javax.inject.Inject

class DrawerViewModel @Inject constructor(
    private val app: Application,
    private val storageManager: StorageManager
) : HomeViewModel(app, storageManager) {

    fun getDrawerItems(): ArrayList<BaseDrawerItem> = arrayListOf(
        PrimaryStorage(getString(R.string.internal_storage), R.drawable.ic_folder_black),
        Divider,
        Category(getString(R.string.dcim), R.drawable.ic_settings_black),
        Category(getString(R.string.download), R.drawable.ic_settings_black),
        Category(getString(R.string.movies), R.drawable.ic_settings_black),
        Category(getString(R.string.musics), R.drawable.ic_settings_black),
        Category(getString(R.string.pictures), R.drawable.ic_settings_black),
        Divider,
        Category(getString(R.string.quick_access), R.drawable.ic_settings_black),
        Category(getString(R.string.recent_files), R.drawable.ic_settings_black),
        Category(getString(R.string.images), R.drawable.ic_settings_black),
        Category(getString(R.string.videos), R.drawable.ic_settings_black),
        Category(getString(R.string.audio), R.drawable.ic_settings_black),
        Category(getString(R.string.documents), R.drawable.ic_settings_black),
        Category(getString(R.string.apks), R.drawable.ic_settings_black),
        Divider,
        InstalledApps(getString(R.string.app_manager), R.drawable.ic_settings_black),
        Settings(getString(R.string.settings), R.drawable.ic_settings_black)
    )
}