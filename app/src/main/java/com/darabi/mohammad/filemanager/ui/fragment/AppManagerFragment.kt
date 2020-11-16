package com.darabi.mohammad.filemanager.ui.fragment

import android.os.Bundle
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.ui.fragment.base.BaseFragment
import com.darabi.mohammad.filemanager.vm.AppManagerViewModel
import javax.inject.Inject

class AppManagerFragment @Inject constructor(
    private val appManagerViewModel: AppManagerViewModel
) : BaseFragment(R.layout.fragment_app_manager) {

    override val fragmentTag: String get() = this.javaClass.simpleName
    override val viewModel: AppManagerViewModel get() = appManagerViewModel

    override fun saveUiState(bundle: Bundle) {

    }

    override fun retrieveUiState(bundle: Bundle) {
    }
}