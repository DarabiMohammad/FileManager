package com.darabi.mohammad.filemanager.vm

import android.app.Application
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.model.DrawerItem
import com.darabi.mohammad.filemanager.util.storage.VolumeManager
import javax.inject.Inject

class DrawerViewModel @Inject constructor(
    private val app: Application,
    private val volumeManager: VolumeManager
) : HomeViewModel(app, volumeManager) {

    private companion object {
        val QUICK_ACCESS = R.string.quick_access to R.drawable.ic_settings_black
        val RECENT_FILES = R.string.recent_files to R.drawable.ic_settings_black
        val IMAGES = R.string.images to R.drawable.ic_settings_black
        val VIDEOS = R.string.videos to R.drawable.ic_settings_black
        val AUDIO = R.string.audio to R.drawable.ic_settings_black
        val DOCUMENTS = R.string.documents to R.drawable.ic_settings_black
        val APKS = R.string.apks to R.drawable.ic_settings_black
        val APP_MANAGER = R.string.app_manager to R.drawable.ic_settings_black
        val SETTINGS = R.string.settings to R.drawable.ic_settings_black
    }

    fun getStaticDrawerItems(): ArrayList<DrawerItem> = arrayListOf(
        DrawerItem.Item(
            getPrimaryExternalStorageVolume().name,
            getPrimaryExternalStorageVolume().path,
            PRIMARY_EXTERNAL_STORAGE_ICON
        ),
        DrawerItem.Divider,
        DrawerItem.Item(getString(DCIM.first), volumeManager.dcimPath, DCIM.second),
        DrawerItem.Item(getString(DOWNLOAD.first), volumeManager.downloadPath, DOWNLOAD.second),
        DrawerItem.Item(getString(MOVIES.first), volumeManager.moviesPath, MOVIES.second),
        DrawerItem.Item(getString(MUSICS.first), volumeManager.musicsPath, MUSICS.second),
        DrawerItem.Item(getString(PICTURES.first), volumeManager.picturesPath, PICTURES.second),
        DrawerItem.Item(getString(QUICK_ACCESS.first), "", QUICK_ACCESS.second),
        DrawerItem.Divider,
        DrawerItem.Item(getString(RECENT_FILES.first), "", RECENT_FILES.second),
        DrawerItem.Item(getString(IMAGES.first), "", IMAGES.second),
        DrawerItem.Item(getString(VIDEOS.first), "", VIDEOS.second),
        DrawerItem.Item(getString(AUDIO.first), "", AUDIO.second),
        DrawerItem.Item(getString(DOCUMENTS.first), "", DOCUMENTS.second),
        DrawerItem.Item(getString(APKS.first), "", APKS.second),
        DrawerItem.Divider,
        DrawerItem.Item(getString(APP_MANAGER.first), "", APP_MANAGER.second),
        DrawerItem.Item(getString(SETTINGS.first), "", SETTINGS.second),
    )
}