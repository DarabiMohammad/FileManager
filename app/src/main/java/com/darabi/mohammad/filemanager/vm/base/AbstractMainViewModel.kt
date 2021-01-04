package com.darabi.mohammad.filemanager.vm.base

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.darabi.mohammad.filemanager.model.*

abstract class AbstractMainViewModel constructor (
    private val app: Application,
) : BaseViewModel(app) {

    val externalStorageLiveData by lazy { MutableLiveData<String>() }
    val drawerPrimaryStorageLiveData by lazy { MutableLiveData<String>() }
    val drawerCategoryLiveData by lazy { MutableLiveData<Category>() }
    val drawerInstalledAppsLiveData by lazy { MutableLiveData<InstalledApps>() }
    val drawerSettingsLiveData by lazy { MutableLiveData<Settings>() }

    val onThemeChanged by lazy { MutableLiveData<String>() }

    fun onDrawerItemClick(drawerItem: DrawerItem) = when(drawerItem) {
        is PrimaryExtStorage -> drawerPrimaryStorageLiveData.value = drawerItem.path
        is Category -> drawerCategoryLiveData.value = drawerItem
        is InstalledApps -> drawerInstalledAppsLiveData.value = drawerItem
        else -> drawerSettingsLiveData.value = drawerItem as Settings
    }
}