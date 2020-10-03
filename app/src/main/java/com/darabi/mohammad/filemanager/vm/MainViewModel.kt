package com.darabi.mohammad.filemanager.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.darabi.mohammad.filemanager.util.makeToast
import com.darabi.mohammad.filemanager.util.storage.OnUsbConnectionListener
import com.darabi.mohammad.filemanager.util.storage.VolumeManager
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val app: Application,
    private val volumeManager: VolumeManager
) : AndroidViewModel(app), OnUsbConnectionListener {

    init { volumeManager.otgConnectionCallback = this }

    val onDrawerItemClick = MutableLiveData<String>()
    val availableStorageDevices = MutableLiveData<ArrayList<String>>()

    fun getAvailableStorageDevices() {
        availableStorageDevices.value = volumeManager.getAvailableStorageNames()
    }

    override fun onOtgConnectionChange(msg: String) {
        //todo fix OtgReceiver
        app.makeToast(msg)
    }

    override fun onCleared() {
        volumeManager.onDestroy()
        super.onCleared()
    }
}