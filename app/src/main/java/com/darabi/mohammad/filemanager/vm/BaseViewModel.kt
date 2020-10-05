package com.darabi.mohammad.filemanager.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.darabi.mohammad.filemanager.model.BaseItem

abstract class BaseViewModel constructor(app: Application) : AndroidViewModel(app) {

    val onItemClicke = MutableLiveData<BaseItem>()
}