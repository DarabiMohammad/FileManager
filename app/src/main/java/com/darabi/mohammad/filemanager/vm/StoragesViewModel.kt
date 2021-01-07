package com.darabi.mohammad.filemanager.vm

import android.app.Application
import com.darabi.mohammad.filemanager.repository.storage.Volumes
import com.darabi.mohammad.filemanager.vm.base.BaseViewModel
import javax.inject.Inject

abstract class StoragesViewModel constructor(private val app: Application) : BaseViewModel(app) {

    @Inject
    protected lateinit var volumes: Volumes
}