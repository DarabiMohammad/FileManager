package com.darabi.mohammad.filemanager.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.darabi.mohammad.filemanager.util.storage.VolumeManager
import javax.inject.Inject

class MainViewModel @Inject constructor(
    application: Application,
    private val volumeManager: VolumeManager
) : AndroidViewModel(application) {

    val onDrawerItemClick = MutableLiveData<String>()
}