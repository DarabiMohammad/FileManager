package com.darabi.mohammad.filemanager.ui.fragment.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.darabi.mohammad.filemanager.vm.BaseViewModel

abstract class BaseFragment : Fragment() {

    abstract val TAG: String

    abstract val  viewModel: BaseViewModel

    @get:LayoutRes
    abstract val layoutRes: Int

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(layoutRes, container, false)
}