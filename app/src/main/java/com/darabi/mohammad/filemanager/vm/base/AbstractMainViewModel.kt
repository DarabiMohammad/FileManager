package com.darabi.mohammad.filemanager.vm.base

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.darabi.mohammad.filemanager.model.*

abstract class AbstractMainViewModel constructor (
    private val app: Application,
) : BaseViewModel(app) {

    val volumeClickLiveData by lazy { MutableLiveData<StorageItem>() }
    val drawerCategoryFolderLiveData by lazy { MutableLiveData<Document>() }
    val drawerCategoryLiveData by lazy { MutableLiveData<Category>() }
    val drawerInstalledAppsLiveData by lazy { MutableLiveData<InstalledApps>() }
    val drawerSettingsLiveData by lazy { MutableLiveData<Settings>() }

    val updateToobarTitle by lazy { MutableLiveData<String>() }
    val onActionModeChanged by lazy { MutableLiveData<Pair<Int, Boolean>>() }

    val onThemeChanged by lazy { MutableLiveData<String>() }

    fun onDrawerItemClick(drawerItem: DrawerItem) = when(drawerItem) {
        is StorageItem -> volumeClickLiveData.value = drawerItem
        is Document -> drawerCategoryFolderLiveData.value = drawerItem
        is Category -> drawerCategoryLiveData.value = drawerItem
        is InstalledApps -> drawerInstalledAppsLiveData.value = drawerItem
        else -> drawerSettingsLiveData.value = drawerItem as Settings
    }
}