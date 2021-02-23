package com.darabi.mohammad.filemanager.vm.base

import android.app.Application
import androidx.annotation.StringRes
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.darabi.mohammad.filemanager.model.Result
import com.darabi.mohammad.filemanager.util.PrefsManager
import javax.inject.Inject

abstract class BaseViewModel constructor(private val app: Application) : AndroidViewModel(app) {

    @Inject
    protected lateinit var prefsManager: PrefsManager

    val onFragmentBackPressed by lazy { MutableLiveData<String?>() }

    protected fun getString(@StringRes string: Int) = app.getString(string)

    protected inline fun <T> loadingLiveData(crossinline function: suspend () -> Result<T>): LiveData<Result<T>> = liveData {
        emit(Result.loading())
        emit(function())
    }
}