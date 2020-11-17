package com.darabi.mohammad.filemanager.vm.settings

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.darabi.mohammad.filemanager.ui.dialog.ThemeSelectionDialog
import javax.inject.Inject

class AppearanceViewModel @Inject constructor(
        private val app: Application
) : SettingsViewModel(app) {

    val onThemeChange = MutableLiveData<ThemeSelectionDialog.SelectedTheme>()
}