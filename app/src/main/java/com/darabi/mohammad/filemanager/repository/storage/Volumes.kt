
package com.darabi.mohammad.filemanager.repository.storage

import com.darabi.mohammad.filemanager.model.Result
import com.darabi.mohammad.filemanager.model.StorageVolume

abstract class Volumes {

    abstract suspend fun getVolumes(): Result<ArrayList<StorageVolume>>
}