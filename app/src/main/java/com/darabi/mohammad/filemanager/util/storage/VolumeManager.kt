package com.darabi.mohammad.filemanager.util.storage

import android.app.Application
import android.content.IntentFilter
import android.os.Build
import java.io.File
import javax.inject.Inject
import android.os.storage.StorageManager
import androidx.annotation.RequiresApi
import com.darabi.mohammad.filemanager.R

class VolumeManager @Inject constructor (
    private val application: Application,
    private val otgReceiver: OtgReceiver,
    intentFilter: IntentFilter
) {

    var otgConnectionCallback: OnUsbConnectionListener? = null

    private val internalStorage = application.getString(R.string.internal_storage)
    private val sdCardStorage = application.getString(R.string.sd_card)
    private val usbStorage = application.getString(R.string.usb_storage)

    init {
        otgReceiver.callback = otgConnectionCallback
        application.registerReceiver(otgReceiver, intentFilter)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun getStorageManager() = application.getSystemService(StorageManager::class.java)

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getStorageVolumes() = getStorageManager().storageVolumes

    //todo fix this method for returning names for devices run on api 23 and older
    private fun getStoragesNameLegacy(): ArrayList<String> = arrayListOf("", "")

    fun getAvailableStorageNames(): ArrayList<String> {
        val storageNames = arrayListOf<String>()
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            getStorageVolumes().forEach {
                val storageDesc = it.getDescription(application)
                if(storageDesc == "Internal shared storage")
                    storageNames.add(internalStorage)
                else if (storageDesc == "SDCARD")
                    storageNames.add(sdCardStorage)
                else
                    storageNames.add("$usbStorage $storageDesc")
            }
        else
            storageNames.addAll(getStoragesNameLegacy())
        return storageNames
    }

    fun onDestroy() = application.unregisterReceiver(otgReceiver)

    data class StoragManagerException internal constructor(override val message: String) : Throwable()
}