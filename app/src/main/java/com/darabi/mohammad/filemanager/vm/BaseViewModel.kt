package com.darabi.mohammad.filemanager.vm

import android.app.Application
import androidx.annotation.StringRes
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.darabi.mohammad.filemanager.model.StorageItem
import com.darabi.mohammad.filemanager.util.PrefsManager
import javax.inject.Inject

abstract class BaseViewModel constructor(private val app: Application) : AndroidViewModel(app) {

    val onFragmentBackPressed by lazy { MutableLiveData<String?>() }

    @Inject
    protected lateinit var prefsManager: PrefsManager

    protected fun getString(@StringRes string: Int) = app.getString(string)
}