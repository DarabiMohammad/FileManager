package com.darabi.mohammad.filemanager.vm.ccontent

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.model.*
import com.darabi.mohammad.filemanager.repository.storage.StorageManager
import com.darabi.mohammad.filemanager.repository.storage.Volumes
import com.darabi.mohammad.filemanager.util.EMPTY_STRING
import com.darabi.mohammad.filemanager.util.PathManager
import com.darabi.mohammad.filemanager.vm.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException
import java.util.*
import javax.inject.Inject
import javax.inject.Provider
import kotlin.collections.ArrayList

class CopyMoveViewModel @Inject constructor (
    private val app: Application,
    private val volumes: Volumes,
    private val pathManagerProvider: Provider<PathManager>,
    private val storageManager: StorageManager
) : BaseViewModel(app) {

    val onPathSelected by lazy { MutableLiveData<String?>() }
    val openNewFileDialog by lazy { MutableLiveData<FileType?>() }

    private val tempDirectories by lazy { mutableMapOf<String, ArrayList<Directory>>() }

    private lateinit var pathManager: PathManager

    fun onBackPressed(): LiveData<Result<ArrayList<out BaseItem>>> = loadingLiveData {
        pathManager.perviousPath().run {
            if (tempDirectories.containsKey(this)) getTemDirectories(this!!) else storageManager.getFolders(this)
        }
    }

    fun createFolder(fileName: String) = liveData {
        // todo optimize this lines of code for situations which in there is many folders, for example a thousand folders.
        pathManager.lastPath()!!.run {
            val directory = Directory(fileName, "$this${File.separator}$fileName", EMPTY_STRING)
            tempDirectories[this]!!.run {
                this.find { directory -> directory.name == fileName }?.let {
                    emit(Result.error<Pair<Directory, Int>>(IOException(getString(R.string.dir_allready_exist))))
                    return@liveData
                }
                tempDirectories[directory.path] = ArrayList()
                this.add(directory)
                this.sortedBy { directory -> directory.name.toLowerCase(Locale.getDefault()) }
                emit(Result.success(Pair(directory, this.indexOf(directory))))
            }
        }
    }

    fun getVolumes() = loadingLiveData {
        pathManager = pathManagerProvider.get()
        volumes.getVolumes()
    }

    fun clearTempDirectories() = tempDirectories.clear()

    fun getFolders(path: String) = loadingLiveData {

        pathManager.addPath(path)

        if (tempDirectories.containsKey(path))
            getTemDirectories(path)
        else storageManager.getFolders(path).apply {
            if (status == Status.SUCCESS) withContext(Dispatchers.Default) { tempDirectories[path] = result!! }
        }
    }

    private suspend fun getTemDirectories(path: String) = withContext(Dispatchers.Default) {
        Result.success(tempDirectories[path]!!)
    }
}