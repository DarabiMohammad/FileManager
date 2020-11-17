package com.darabi.mohammad.filemanager.vm.settings

import android.app.Application
import com.darabi.mohammad.filemanager.vm.BaseViewModel
import javax.inject.Inject

open class SettingsViewModel @Inject constructor(
    private val app: Application
) : BaseViewModel(app) {
}