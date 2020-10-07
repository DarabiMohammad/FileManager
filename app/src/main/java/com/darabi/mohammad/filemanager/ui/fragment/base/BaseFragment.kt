package com.darabi.mohammad.filemanager.ui.fragment.base

import androidx.fragment.app.Fragment
import com.darabi.mohammad.filemanager.vm.BaseViewModel

abstract class BaseFragment constructor(layoutRes: Int) : Fragment(layoutRes) {

    abstract val TAG: String

    abstract val  viewModel: BaseViewModel
}