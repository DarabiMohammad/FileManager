package com.darabi.mohammad.filemanager.ui.fragment.base

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.darabi.mohammad.filemanager.vm.base.MainViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseBottomSheetFragment : BottomSheetDialogFragment() {

    val fragmentTag: String = this.javaClass.simpleName

    @get:LayoutRes
    abstract val layoutRes: Int

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(layoutRes, container, false)
}