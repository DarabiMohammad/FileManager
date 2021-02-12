package com.darabi.mohammad.filemanager.ui.fragment.base

import android.widget.Toast
import androidx.fragment.app.Fragment
import com.darabi.mohammad.filemanager.vm.base.MainViewModel

abstract class BaseFragment constructor(layoutRes: Int) : Fragment(layoutRes) {

    abstract val fragmentTag: String

    abstract val viewModel: MainViewModel

    // todo avoid passing fragmentTag if is not needed.
    open fun onBackPressed() { viewModel.onFragmentBackPressed.value = fragmentTag }

    protected fun makeToast(message: String) = Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
}