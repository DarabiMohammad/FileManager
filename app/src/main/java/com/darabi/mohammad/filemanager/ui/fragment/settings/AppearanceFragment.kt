package com.darabi.mohammad.filemanager.ui.fragment.settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.ui.dialog.ThemeSelectionDialog
import com.darabi.mohammad.filemanager.ui.fragment.base.BaseFragment
import com.darabi.mohammad.filemanager.vm.MainViewModel
import kotlinx.android.synthetic.main.fragment_appearance.*
import javax.inject.Inject

class AppearanceFragment @Inject constructor(
        private val themeSelectionDialog: ThemeSelectionDialog
) : BaseFragment(R.layout.fragment_appearance), View.OnClickListener {

    override val fragmentTag: String get() = this.javaClass.simpleName
    override val viewModel: MainViewModel  by viewModels ({ requireActivity() })

    override fun saveUiState(bundle: Bundle) {}

    override fun retrieveUiState(bundle: Bundle) {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        observeViewModel()
        initViews()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun observeViewModel() {

        viewModel.onBackPressed.observe(viewLifecycleOwner, {
            if(it) parentFragment?.childFragmentManager?.popBackStack()
        })
    }

    private fun initViews() {
        container_theme.setOnClickListener(this)
    }

    override fun onClick(view: View?) = when(view?.id) {
        R.id.container_theme -> themeSelectionDialog.show(childFragmentManager, themeSelectionDialog.TAG)
        else -> {}
    }
}