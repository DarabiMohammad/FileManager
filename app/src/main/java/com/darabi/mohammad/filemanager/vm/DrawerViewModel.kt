package com.darabi.mohammad.filemanager.vm

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.model.*
import com.darabi.mohammad.filemanager.util.launchInViewModelScope
import javax.inject.Inject

class DrawerViewModel @Inject constructor(
    private val app: Application
) : StoragesViewModel(app) {

    val drawerItems by lazy { MutableLiveData<Result<ArrayList<StorageVolume>>>() }

    fun getDrawerItems() = launchInViewModelScope(drawerItems) {
        volumes.getVolumes()
    }

    fun getStaticItems() = arrayListOf (
        Category(getString(R.string.dcim), R.drawable.ic_settings_black, CategoryType.DCIM),
        Category(getString(R.string.download), R.drawable.ic_settings_black, CategoryType.DOWNLOAD),
        Category(getString(R.string.movies), R.drawable.ic_settings_black, CategoryType.MOVIES),
        Category(getString(R.string.musics), R.drawable.ic_settings_black, CategoryType.MUSICS),
        Category(getString(R.string.pictures), R.drawable.ic_settings_black, CategoryType.PICTURES),
        Divider,
        Category(getString(R.string.quick_access), R.drawable.ic_settings_black, CategoryType.QUICK_ACCESS),
        Category(getString(R.string.recent_files), R.drawable.ic_settings_black, CategoryType.RECENT_FILES),
        Category(getString(R.string.images), R.drawable.ic_settings_black, CategoryType.IMAGES),
        Category(getString(R.string.videos), R.drawable.ic_settings_black, CategoryType.VIDEOS),
        Category(getString(R.string.audio), R.drawable.ic_settings_black, CategoryType.AUDIO),
        Category(getString(R.string.documents), R.drawable.ic_settings_black, CategoryType.DOCUMENTS),
        Category(getString(R.string.apks), R.drawable.ic_settings_black, CategoryType.APKS),
        Divider,
        InstalledApps(getString(R.string.app_manager), R.drawable.ic_settings_black),
        Settings(getString(R.string.settings), R.drawable.ic_settings_black)
    )

    fun mapToDrawerItems(volume: ArrayList<StorageVolume>) = arrayListOf<BaseDrawerItem>().run {
        volume.forEach {
            this.add(PrimaryExtStorage(it.name, it.path, it.icon))
        }
        if (this.isNotEmpty()) this.add(Divider)
        this
    }
}