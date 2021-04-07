package com.darabi.mohammad.filemanager.ui.fragment

import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.ui.fragment.base.BaseFragment
import com.darabi.mohammad.filemanager.vm.AppManagerViewModel
import javax.inject.Inject

class AppManagerFragment @Inject constructor(
    private val appManagerViewModel: AppManagerViewModel
) : BaseFragment(R.layout.fragment_app_manager) {
}