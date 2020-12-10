package com.darabi.mohammad.filemanager.vm

import android.app.Application
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.util.storage.StorageManager
import javax.inject.Inject

class DrawerViewModel @Inject constructor(
    private val app: Application,
    private val storageManager: StorageManager
) : HomeViewModel(app, storageManager) {

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

//    fun getStaticDrawerItems(): ArrayList<DrawerItem> = arrayListOf(
//        DrawerItem.Item(
//            getPrimaryExternalStorageVolume().itemName, getPrimaryExternalStorageVolume().itemPath,
//            itemImageRes = PRIMARY_EXTERNAL_STORAGE_ICON
//        ),
//        DrawerItem.Divider,
//        DrawerItem.Item(getString(DCIM.first), volumeManager.dcimPath, itemImageRes = DCIM.second),
//        DrawerItem.Item(getString(DOWNLOAD.first), volumeManager.downloadPath, itemImageRes = DOWNLOAD.second),
//        DrawerItem.Item(getString(MOVIES.first), volumeManager.moviesPath, itemImageRes = MOVIES.second),
//        DrawerItem.Item(getString(MUSICS.first), volumeManager.musicsPath, itemImageRes = MUSICS.second),
//        DrawerItem.Item(getString(PICTURES.first), volumeManager.picturesPath, itemImageRes = PICTURES.second),
//        DrawerItem.Item(getString(QUICK_ACCESS.first), "", itemImageRes = QUICK_ACCESS.second),
//        DrawerItem.Divider,
//        DrawerItem.Item(getString(RECENT_FILES.first), volumeManager.recentFilesPath, itemImageRes = RECENT_FILES.second),
//        DrawerItem.Item(getString(IMAGES.first), volumeManager.imagesPath, itemImageRes = IMAGES.second),
//        DrawerItem.Item(getString(VIDEOS.first), volumeManager.videosPath, itemImageRes = VIDEOS.second),
//        DrawerItem.Item(getString(AUDIO.first), volumeManager.audioPath, itemImageRes = AUDIO.second),
//        DrawerItem.Item(getString(DOCUMENTS.first), volumeManager.documentsPath, itemImageRes = DOCUMENTS.second),
//        DrawerItem.Item(getString(APKS.first), volumeManager.apksPath, itemImageRes = APKS.second),
//        DrawerItem.Divider,
//        DrawerItem.Item(getString(APP_MANAGER.first), EMPTY_STRING, ItemType.DRAWER_ITEM_OTHER, itemImageRes = APP_MANAGER.second),
//        DrawerItem.Item(getString(SETTINGS.first), EMPTY_STRING, ItemType.DRAWER_ITEM_OTHER, itemImageRes = SETTINGS.second),
//    )
}