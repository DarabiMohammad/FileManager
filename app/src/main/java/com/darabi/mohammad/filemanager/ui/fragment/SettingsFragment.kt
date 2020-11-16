package com.darabi.mohammad.filemanager.ui.fragment

import android.os.Bundle
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.ui.fragment.base.BaseFragment
import com.darabi.mohammad.filemanager.vm.SettingsViewModel
import javax.inject.Inject

class SettingsFragment @Inject constructor(
    private val settingsViewModel: SettingsViewModel
) : BaseFragment(R.layout.fragment_settings) {

    override val fragmentTag: String get() = this.javaClass.simpleName
    override val viewModel: SettingsViewModel get() = settingsViewModel

    override fun saveUiState(bundle: Bundle) {

    }

    override fun retrieveUiState(bundle: Bundle) {
    }
}