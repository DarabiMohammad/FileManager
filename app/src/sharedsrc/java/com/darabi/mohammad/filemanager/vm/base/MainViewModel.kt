package com.darabi.mohammad.filemanager.vm.base

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.darabi.mohammad.filemanager.ui.dialog.PermissionDescriptionDialog
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainViewModel @Inject constructor(app: Application) : AbstractMainViewModel(app) {


    val permissionDialoLiveData = MutableLiveData<PermissionDescriptionDialog.Action>()
    val onSelectAllClick = MutableLiveData<Boolean>()
    val onDeleteClicked = MutableLiveData<Boolean>()
}