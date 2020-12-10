package com.darabi.mohammad.filemanager.vm.settings

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.darabi.mohammad.filemanager.ui.dialog.ThemeSelectionDialog
import com.darabi.mohammad.filemanager.util.PrefsManager
import javax.inject.Inject

class AppearanceViewModel @Inject constructor(
        private val app: Application,
        private val prefsManager: PrefsManager
) : SettingsViewModel(app, prefsManager) {

    val onThemeChange = MutableLiveData<ThemeSelectionDialog.SelectedTheme>()
}