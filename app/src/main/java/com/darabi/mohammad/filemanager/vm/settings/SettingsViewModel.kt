package com.darabi.mohammad.filemanager.vm.settings

import android.app.Application
import com.darabi.mohammad.filemanager.util.PrefsManager
import com.darabi.mohammad.filemanager.vm.BaseViewModel
import javax.inject.Inject

open class SettingsViewModel @Inject constructor(
    private val app: Application,
    private val prefsManager: PrefsManager
) : BaseViewModel(app) {

    fun setShowHiddenFiles(shouldShow: Boolean) = prefsManager.setHiddenModeEnable(shouldShow)

    fun shouldShowHiddenFiles(): Boolean = prefsManager.isHiddenModeEnabled()

    fun setShowSplitViews(shouldShowSplitViews: Boolean) = prefsManager.setSplitModeEnable(shouldShowSplitViews)

    fun shouldShowSplitViews(): Boolean = prefsManager.isSplitModeEnabled()
}