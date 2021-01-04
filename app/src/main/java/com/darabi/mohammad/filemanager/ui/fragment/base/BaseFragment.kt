package com.darabi.mohammad.filemanager.ui.fragment.base

import androidx.fragment.app.Fragment
import com.darabi.mohammad.filemanager.vm.base.BaseViewModel
import com.darabi.mohammad.filemanager.vm.base.MainViewModel

abstract class BaseFragment constructor(layoutRes: Int) : Fragment(layoutRes) {

    abstract val fragmentTag: String

    abstract val viewModel: MainViewModel

    open fun onBackPressed() { viewModel.onFragmentBackPressed.value = fragmentTag }
}