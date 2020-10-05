package com.darabi.mohammad.filemanager.ui.fragment

import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.ui.fragment.base.BaseFragment
import com.darabi.mohammad.filemanager.vm.AppManagerViewModel
import com.darabi.mohammad.filemanager.vm.BaseViewModel
import javax.inject.Inject

class AppManagerFragment @Inject constructor(
    private val appManagerViewModel: AppManagerViewModel
) : BaseFragment() {

    override val TAG: String get() = this.javaClass.simpleName
    override val layoutRes: Int get() = R.layout.fragment_app_manager
    override val viewModel: AppManagerViewModel get() = appManagerViewModel
}