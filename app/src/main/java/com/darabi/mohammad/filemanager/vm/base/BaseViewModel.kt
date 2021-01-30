package com.darabi.mohammad.filemanager.vm.base

import android.app.Application
import androidx.annotation.StringRes
import androidx.lifecycle.*
import com.darabi.mohammad.filemanager.model.Result
import com.darabi.mohammad.filemanager.util.PrefsManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

abstract class BaseViewModel constructor(private val app: Application) : AndroidViewModel(app) {

    @Inject
    protected lateinit var prefsManager: PrefsManager

    val onFragmentBackPressed by lazy { MutableLiveData<String?>() }

    protected fun getString(@StringRes string: Int) = app.getString(string)

    protected inline fun <T> launchSupervisorJob(
        liveData: MutableLiveData<Result<T>>, crossinline function: suspend () -> Result<T>
    ) = viewModelScope.launch(SupervisorJob() + Dispatchers.Main) {
        liveData.value = Result.loading()
        liveData.value = function()
    }

    protected inline fun <T> launchInViewModelScope(
        liveData: MutableLiveData<Result<T>>, crossinline function: suspend () -> Result<T>
    ) = viewModelScope.launch(Dispatchers.Main) {
        liveData.value = Result.loading()
        liveData.value = function()
    }
}