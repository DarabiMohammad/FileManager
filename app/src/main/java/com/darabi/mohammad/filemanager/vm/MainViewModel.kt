package com.darabi.mohammad.filemanager.vm

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.darabi.mohammad.filemanager.model.*
import com.darabi.mohammad.filemanager.ui.dialog.PermissionDescriptionDialog
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainViewModel @Inject constructor (
    private val app: Application,
) : BaseViewModel(app) {

    val primaryStorageLiveData by lazy { MutableLiveData<PrimaryExternalStorage>() }
    val secondaryStorageLiveData by lazy { MutableLiveData<SecondaryExternalStorage>() }
    val drawerPrimaryStorageLiveData by lazy { MutableLiveData<PrimaryStorage>() }
    val drawerSecondaryStorageLiveData by lazy { MutableLiveData<SecondaryStorage>() }
    val drawerCategoryLiveData by lazy { MutableLiveData<Category>() }
    val drawerInstalledAppsLiveData by lazy { MutableLiveData<InstalledApps>() }
    val drawerSettingsLiveData by lazy { MutableLiveData<Settings>() }

    val permissionDialoLiveData = MutableLiveData<PermissionDescriptionDialog.Action>()
    val onActionModeChange = MutableLiveData<Pair<Int, Boolean>>()
    val onSelectAllClick = MutableLiveData<Boolean>()
    val onDeleteClicked = MutableLiveData<Boolean>()

    fun onStorageClick(storage: StorageItem) = when(storage) {
        is PrimaryExternalStorage -> primaryStorageLiveData.value = storage
        else -> secondaryStorageLiveData.value = storage as SecondaryExternalStorage
    }

    fun onDrawerItemClick(drawerItem: DrawerItem) = when(drawerItem) {
        is PrimaryStorage -> drawerPrimaryStorageLiveData.value = drawerItem
        is SecondaryStorage -> drawerSecondaryStorageLiveData.value = drawerItem
        is Category -> drawerCategoryLiveData.value = drawerItem
        is InstalledApps -> drawerInstalledAppsLiveData.value = drawerItem
        else -> drawerSettingsLiveData.value = drawerItem as Settings
    }
}