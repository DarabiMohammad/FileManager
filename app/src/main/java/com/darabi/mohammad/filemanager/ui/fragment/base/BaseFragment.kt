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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d("test","================onSaveInstanceState BaseFragment")
    }

    open fun onBackPressed() = activity?.let { if(it is MainActivity) it.onFragmentBackPressed() }
}