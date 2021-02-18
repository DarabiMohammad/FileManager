package com.darabi.mohammad.filemanager.ui.fragment

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.ui.fragment.base.BaseFragment
import com.darabi.mohammad.filemanager.vm.AppManagerViewModel
import com.darabi.mohammad.filemanager.vm.base.MainViewModel
import javax.inject.Inject

class AppManagerFragment @Inject constructor(
    private val appManagerViewModel: AppManagerViewModel
) : BaseFragment(R.layout.fragment_app_manager) {

    override val fragmentTag: String get() = this.javaClass.simpleName
    override val mainViewModel: MainViewModel by viewModels( { requireActivity() } )
}