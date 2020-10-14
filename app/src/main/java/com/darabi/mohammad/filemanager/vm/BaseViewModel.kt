package com.darabi.mohammad.filemanager.vm

import android.app.Application
import androidx.annotation.StringRes
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.darabi.mohammad.filemanager.model.BaseItem

abstract class BaseViewModel constructor(private val app: Application) : AndroidViewModel(app) {

    val onItemClicke = MutableLiveData<BaseItem>()

    protected fun getString(@StringRes string: Int) = app.getString(string)
}