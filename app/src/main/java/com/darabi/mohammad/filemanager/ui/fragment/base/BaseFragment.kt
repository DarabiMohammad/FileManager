package com.darabi.mohammad.filemanager.ui.fragment.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.darabi.mohammad.filemanager.vm.BaseViewModel

abstract class BaseFragment constructor(layoutRes: Int) : Fragment(layoutRes) {

    abstract val fragmentTag: String

    abstract val  viewModel: BaseViewModel

    private val uiState by lazy { Bundle() }

    abstract fun saveUiState(bundle: Bundle)

    abstract fun retrieveUiState(bundle: Bundle)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        retrieveUiState(uiState)
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onPause() {

        saveUiState(uiState)
        super.onPause()
    }

    open fun onBackPressed() {
        activity?.supportFragmentManager?.popBackStack()
    }
}