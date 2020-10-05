package com.darabi.mohammad.filemanager.vm

import android.app.Application
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    private val app: Application
) : BaseViewModel(app) {
}