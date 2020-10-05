package com.darabi.mohammad.filemanager.util.storage

import android.app.Application
import android.content.IntentFilter
import android.os.Build
import android.os.Environment
import android.os.Environment.*
import javax.inject.Inject
import android.os.storage.StorageManager
import androidx.annotation.RequiresApi
import com.darabi.mohammad.filemanager.R
import java.io.File

class VolumeManager @Inject constructor (
    private val application: Application,
    private val otgReceiver: OtgReceiver,
    intentFilter: IntentFilter
) {

    var otgConnectionCallback: OnRemovableStorageAttachmentistener? = null

    private val internalStorage = application.getString(R.string.internal_storage)
    private val sdCardStorage = application.getString(R.string.sd_card)
    private val usbStorage = application.getString(R.string.usb_storage)

    val dcimPath by lazy { getExternalStoragePublicDirectory(DIRECTORY_DCIM).path }
    val downloadPath by lazy { getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS).path }
    val moviesPath by lazy { getExternalStoragePublicDirectory(DIRECTORY_MOVIES).path }
    val musicsPath by lazy { getExternalStoragePublicDirectory(DIRECTORY_MUSIC).path }
    val picturesPath by lazy { getExternalStoragePublicDirectory(DIRECTORY_PICTURES).path }

    init {
        otgReceiver.callback = otgConnectionCallback
        application.registerReceiver(otgReceiver, intentFilter)
    }

    companion object {
        const val EMPTY_STRING = ""
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun getStorageManager() = application.getSystemService(StorageManager::class.java)

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getStorageVolumes() = getStorageManager().storageVolumes

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getVolumes(): ArrayList<Volume> {
        val volumes = arrayListOf<Volume>()
        getStorageVolumes().forEach {
            when (val storageDesc = it.getDescription(application)) {
                "Internal shared storage" -> volumes.add(Volume(internalStorage, "${it.directory?.path}"))
                "SDCARD" -> volumes.add(Volume(sdCardStorage, "${it.directory?.path}"))
                else -> volumes.add(Volume("$usbStorage $storageDesc", "${it.directory?.path}"))
            }
        }
        return volumes
    }

    private fun getDirName(path: String): String =
        if(!path.startsWith(File.separator))
            path.substring(path.lastIndexOf(File.separator), path.length - 1)
        else EMPTY_STRING

    private fun getPrimaryExternalStorageDirs() = Environment.getExternalStorageDirectory()

    private fun getLegacyVolumes(): ArrayList<Volume> {
        //TODO fix volumes fo returning sd card and other available storages
        val volumes = arrayListOf(Volume("",""))
        return volumes
    }

    fun getPrimaryExternalStprageVolume() = Volume(internalStorage, getPrimaryExternalStorageDirs().path)

    fun getRemovableVolumes(): ArrayList<Volume> =
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            getVolumes()
        else
            getLegacyVolumes()

    fun getSubDirectoriesPath(path: String): ArrayList<String> {
        val paths = arrayListOf<String>()
        for(file in File(path).listFiles()!!) {
            paths.add(file.path)
        }
        return paths
    }

    fun getPrimaryExtStorageSubDirsPath() =
        getSubDirectoriesPath(getPrimaryExternalStorageDirs().path)

    fun onDestroy() = application.unregisterReceiver(otgReceiver)

    data class Volume internal constructor(val name: String, val path: String)

    data class VolumeManagerException internal constructor(override val message: String) : Throwable()
}