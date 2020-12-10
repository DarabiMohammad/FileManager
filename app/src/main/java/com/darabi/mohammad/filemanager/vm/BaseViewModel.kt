package com.darabi.mohammad.filemanager.vm

import android.app.Application
import androidx.annotation.StringRes
import androidx.lifecycle.AndroidViewModel
import com.darabi.mohammad.filemanager.model.StorageItem

abstract class BaseViewModel constructor(private val app: Application) : AndroidViewModel(app) {

    protected fun getString(@StringRes string: Int) = app.getString(string)
}