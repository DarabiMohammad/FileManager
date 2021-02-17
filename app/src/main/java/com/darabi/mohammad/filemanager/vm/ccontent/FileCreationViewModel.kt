package com.darabi.mohammad.filemanager.vm.ccontent

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.darabi.mohammad.filemanager.model.FileType
import com.darabi.mohammad.filemanager.util.SingleEventWrapper
import com.darabi.mohammad.filemanager.vm.base.BaseViewModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FileCreationViewModel @Inject constructor (
    private val app: Application
) : BaseViewModel(app) {

    val onCreateFile by lazy { MutableLiveData<SingleEventWrapper<Pair<String, FileType>>>() }
}