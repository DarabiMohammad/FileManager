package com.darabi.mohammad.filemanager.ui.fragment.base

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.darabi.mohammad.filemanager.ui.MainActivity
import com.darabi.mohammad.filemanager.vm.BaseViewModel

abstract class BaseFragment constructor(layoutRes: Int) : Fragment(layoutRes) {

    abstract val fragmentTag: String

    abstract val viewModel: BaseViewModel

    open fun onBackPressed() { viewModel.onFragmentBackPressed.value = fragmentTag }
}