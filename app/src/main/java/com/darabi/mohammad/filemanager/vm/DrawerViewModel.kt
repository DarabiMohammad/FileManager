package com.darabi.mohammad.filemanager.vm

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.model.*
import com.darabi.mohammad.filemanager.repository.storage.DrawerItemProvider
import kotlinx.coroutines.launch
import javax.inject.Inject

class DrawerViewModel @Inject constructor(
    private val app: Application,
    private val drawerItemProvider: DrawerItemProvider
) : StoragesViewModel(app) {

    val drawerItems by lazy { MutableLiveData<Result<ArrayList<StorageVolume>>>() }

    fun getDrawerItems() = launchInViewModelScope(drawerItems) { volumes.getVolumes() }

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

    fun mapToDrawerItems(volume: ArrayList<StorageVolume>) = arrayListOf<BaseDrawerItem>().run {
        volume.forEach { this.add(PrimaryExtStorage(it.name, it.path, it.type, it.icon)) }
        if (this.isNotEmpty()) this.add(Divider)
        this
    }
}