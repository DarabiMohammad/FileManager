package com.darabi.mohammad.filemanager.ui.fragment

import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.ui.fragment.base.BaseFragment
import com.darabi.mohammad.filemanager.vm.BaseViewModel
import com.darabi.mohammad.filemanager.vm.SettingsViewModel
import javax.inject.Inject

class SettingsFragment @Inject constructor(
    private val settingsViewModel: SettingsViewModel
) : BaseFragment() {

    override val TAG: String get() = this.javaClass.simpleName
    override val layoutRes: Int get() = R.layout.fragment_settings
    override val viewModel: SettingsViewModel get() = settingsViewModel
}