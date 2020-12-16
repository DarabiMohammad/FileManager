package com.darabi.mohammad.filemanager.vm.settings

import android.app.Application
import com.darabi.mohammad.filemanager.util.PrefsManager
import com.darabi.mohammad.filemanager.vm.BaseViewModel
import javax.inject.Inject

open class SettingsViewModel @Inject constructor(
    private val app: Application,
) : BaseViewModel(app) {

    val isHiddenFilesEnabled by lazy { prefsManager.isHiddenModeEnabled() }
    val isSplitModeEnabled by lazy { prefsManager.isSplitModeEnabled() }

    fun setShowHiddenFiles(isEnabled: Boolean) = prefsManager.setHiddenModeEnable(isEnabled)

    fun setShowSplitViews(isEnabled: Boolean) = prefsManager.setSplitModeEnable(isEnabled)
}