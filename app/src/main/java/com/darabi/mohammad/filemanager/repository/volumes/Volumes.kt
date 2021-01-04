
package com.darabi.mohammad.filemanager.repository.volumes

import com.darabi.mohammad.filemanager.model.Result
import com.darabi.mohammad.filemanager.model.StorageVolume

abstract class Volumes {

    abstract suspend fun getVolumes(): Result<ArrayList<StorageVolume>>
}