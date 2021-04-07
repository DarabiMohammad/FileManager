package com.darabi.mohammad.filemanager.ui.fragment.base

import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.darabi.mohammad.filemanager.vm.base.MainViewModel

abstract class BaseFragment constructor(layoutRes: Int) : Fragment(layoutRes) {

    val fragmentTag: String get() = this.javaClass.simpleName
    protected val mainViewModel: MainViewModel by viewModels( { requireActivity() } )

    // todo avoid passing fragmentTag if is not needed.
    open fun onBackPressed() { mainViewModel.onFragmentBackPressed.value = fragmentTag }

    protected fun makeToast(message: String) = Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
}