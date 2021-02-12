package com.darabi.mohammad.filemanager.vm.base

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.darabi.mohammad.filemanager.model.*
import com.darabi.mohammad.filemanager.util.NonDuplicativeLiveData

abstract class AbstractMainViewModel constructor (
    private val app: Application,
) : BaseViewModel(app) {

    val volumeClickLiveData by lazy { NonDuplicativeLiveData<StorageItem?>() }
    val drawerCategoryFolderLiveData by lazy { NonDuplicativeLiveData<Document?>() }
    val drawerCategoryLiveData by lazy { NonDuplicativeLiveData<Category?>() }
    val drawerInstalledAppsLiveData by lazy { MutableLiveData<InstalledApps?>() }
    val drawerSettingsLiveData by lazy { MutableLiveData<Settings?>() }

    val updateToobarTitle by lazy { MutableLiveData<String>() }
    val onActionModeChanged by lazy { MutableLiveData<Pair<Int, Boolean>>() }

    val onThemeChanged by lazy { MutableLiveData<String>() }

    fun onDrawerItemClick(drawerItem: DrawerItem) = when(drawerItem) {
        is StorageItem -> volumeClickLiveData.setValueOrNull(drawerItem)
        is Document -> drawerCategoryFolderLiveData.setValueOrNull(drawerItem)
        is Category -> drawerCategoryLiveData.setValueOrNull(drawerItem)
        is InstalledApps -> drawerInstalledAppsLiveData.value = drawerItem
        else -> drawerSettingsLiveData.value = drawerItem as Settings
    }
}