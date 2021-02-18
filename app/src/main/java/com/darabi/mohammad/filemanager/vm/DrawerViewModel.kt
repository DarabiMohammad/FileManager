package com.darabi.mohammad.filemanager.vm

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.darabi.mohammad.filemanager.model.*
import com.darabi.mohammad.filemanager.repository.storage.DrawerItemProvider
import javax.inject.Inject

class DrawerViewModel @Inject constructor(
    private val app: Application,
    private val drawerItemProvider: DrawerItemProvider
) : StoragesViewModel(app) {

    fun getDrawerItems() = loadingLiveData { volumes.getVolumes() }

    fun getStaticItems() = arrayListOf(
        drawerItemProvider.dcimDirectory(),
        drawerItemProvider.downloadDirectory(),
        drawerItemProvider.moviesDirectory(),
        drawerItemProvider.musicsDirectory(),
        drawerItemProvider.picturesDirectory(),
        drawerItemProvider.divider(),
        drawerItemProvider.quickAccess(),
        drawerItemProvider.recentFiles(),
        drawerItemProvider.images(),
        drawerItemProvider.videos(),
        drawerItemProvider.audio(),
        drawerItemProvider.ducuments(),
        drawerItemProvider.apks(),
        drawerItemProvider.divider(),
        drawerItemProvider.appManager(),
        drawerItemProvider.settings()
    )

    fun mapToDrawerItems(volume: ArrayList<StorageVolume>) = arrayListOf<BaseDrawerItem>().apply {
        this.addAll(volume.map { PrimaryExtStorage(it.name, it.path, it.type, it.icon) })
        this.add(Divider)
    }
}