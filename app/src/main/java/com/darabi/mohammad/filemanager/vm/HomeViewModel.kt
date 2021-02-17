package com.darabi.mohammad.filemanager.vm

import android.app.Application
import javax.inject.Inject

open class HomeViewModel @Inject constructor (
    private val app: Application
) : StoragesViewModel(app) {

    fun getVolumes() = loadingLiveData { volumes.getVolumes() }
}