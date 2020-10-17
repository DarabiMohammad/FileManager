package com.darabi.mohammad.filemanager.ui.fragment.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.darabi.mohammad.filemanager.vm.BaseViewModel

abstract class BaseFragment constructor(layoutRes: Int) : Fragment(layoutRes) {

    abstract val TAG: String

    abstract val  viewModel: BaseViewModel

    abstract fun saveUiState(bundle: Bundle)

    abstract fun retrieveUiState(bundle: Bundle)

    private val uiState by lazy { Bundle() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        retrieveUiState(uiState)
    }

    override fun onPause() {
        super.onPause()

        saveUiState(uiState)
    }
}