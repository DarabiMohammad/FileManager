package com.darabi.mohammad.filemanager.util.storage

import android.app.Application
import android.content.IntentFilter
import android.os.Build
import android.os.Environment
import android.os.Environment.*
import android.os.storage.StorageManager
import androidx.annotation.RequiresApi
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.util.EMPTY_STRING
import java.io.File
import javax.inject.Inject

class VolumeManager @Inject constructor (
    private val application: Application,
    private val otgReceiver: OtgReceiver,
    intentFilter: IntentFilter
) {

    var otgConnectionCallback: OnRemovableStorageAttachmentistener? = null

    private val internalStorage = application.getString(R.string.internal_storage)
    private val sdCardStorage = application.getString(R.string.sd_card)
    private val usbStorage = application.getString(R.string.usb_storage)

    val dcimPath: String by lazy { getExternalStoragePublicDirectory(DIRECTORY_DCIM).path }
    val downloadPath: String by lazy { getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS).path }
    val moviesPath: String by lazy { getExternalStoragePublicDirectory(DIRECTORY_MOVIES).path }
    val musicsPath: String by lazy { getExternalStoragePublicDirectory(DIRECTORY_MUSIC).path }
    val picturesPath: String by lazy { getExternalStoragePublicDirectory(DIRECTORY_PICTURES).path }
    val recentFilesPath: String by lazy { application.getString(R.string.category_recent_files) }
    val imagesPath: String by lazy { application.getString(R.string.category_images) }
    val videosPath: String by lazy { application.getString(R.string.category_videos) }
    val audioPath : String by lazy { application.getString(R.string.category_audio) }
    val documentsPath: String by lazy { application.getString(R.string.category_documents) }
    val apksPath: String by lazy { application.getString(R.string.category_apks) }

    init {
//        otgReceiver.callback = otgConnectionCallback
//        application.registerReceiver(otgReceiver, intentFilter)
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

                "Internal shared storage" ->
                    volumes.add(getVolume(internalStorage, "${it.directory?.path}"))

                "SDCARD" ->
                    volumes.add(getVolume(sdCardStorage, "${it.directory?.path}"))

                else -> volumes.add(getVolume("$usbStorage $storageDesc", "${it.directory?.path}"))
            }
        }
        return volumes
    }

    private fun getVolume(name: String, path: String) =
        Volume(name, path, isFile = false, isHidden = false)

    private fun getDirName(path: String): String =
        if(path.startsWith(File.separator))
            path.substring(path.lastIndexOf(File.separator) + 1, path.length)
        else EMPTY_STRING

    private fun getPrimaryExternalStorageDirs() = Environment.getExternalStorageDirectory()

    private fun getLegacyVolumes(): ArrayList<Volume> {
        //TODO fix volumes fo returning sd card and other available storages
        val volumes = arrayListOf(Volume("","", isFile = false, isHidden = false))
        return volumes
    }

    private fun getSubFilesAndDirs(path: String): Pair<ArrayList<Volume>, ArrayList<Volume>> {
        val files = arrayListOf<Volume>()
        val folders = arrayListOf<Volume>()
        if(!File(path).isAbsolute) throw VolumeManagerException("path must be absolute!")
        getFiles(path).forEach { files.add(Volume(getDirName(it.path), it.path, it.isFile, it.isHidden)) }
        getFolders(path).forEach { folders.add(Volume(getDirName(it.path), it.path, it.isFile, it.isHidden)) }
        return Pair(folders, files)
    }

    private fun getFilesList(path: String) = try {
        File(path).listFiles()!!.sorted().toTypedArray()
    } catch (exception: java.lang.NullPointerException) {
        throw VolumeManagerException("Permission Denied For Access To : ${exception.stackTrace[0].methodName}")
    }

    private fun getFolders(path: String):ArrayList<File> {
        val files = arrayListOf<File>()
        getFilesList(path).forEach {
            if(it.isDirectory) files.add(it)
        }
        return files
    }

    private fun getFiles(path: String): ArrayList<File> {
        val files = arrayListOf<File>()
        getFilesList(path).forEach {
            if(it.isFile) files.add(it)
        }
        return files
    }

    private fun getCategoryFiles(path: String): ArrayList<Volume> {
        return arrayListOf(getVolume("",""))
    }

    private fun purgeDirectory(path: String) {
        for (file in getFiles(path)) {
            if(file.isDirectory)
                purgeDirectory(file.path)
            file.delete()
        }
    }

    fun getPrimaryExternalStprageVolume() =
        Volume(internalStorage, getPrimaryExternalStorageDirs().path, isFile = false, isHidden = false)

    fun getRemovableVolumes(): ArrayList<Volume> =
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            getVolumes()
        else
            getLegacyVolumes()

    fun getSubDirectoriesPath(path: String): Pair<ArrayList<Volume>, ArrayList<Volume>> =
        try {
            getSubFilesAndDirs(path)
        } catch (exception: VolumeManagerException) {
            throw exception
        } catch (exception: Exception) {
            throw exception
//            getCategoryFiles(path)
        }

    fun createFileOrFolder(name: String, isFile: Boolean): Volume? {
        val newFile = File(name)
        var isCreated = false
        if(!newFile.exists())
            isCreated = if(isFile) newFile.createNewFile() else newFile.mkdir()
        return if (isCreated) Volume(getDirName(name), name, newFile.isFile, newFile.isHidden) else null
    }

    fun delete(path: String) = File(path).delete()

    fun onDestroy() {
//        application.unregisterReceiver(otgReceiver)
    }

    data class Volume internal constructor(val name: String, val path: String, val isFile: Boolean, val isHidden: Boolean)

    data class VolumeManagerException internal constructor(override val message: String) : Throwable()
}