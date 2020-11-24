package com.darabi.mohammad.filemanager.ui.fragment.settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.ui.dialog.ThemeSelectionDialog
import com.darabi.mohammad.filemanager.ui.fragment.base.BaseFragment
import com.darabi.mohammad.filemanager.vm.MainViewModel
import com.darabi.mohammad.filemanager.vm.settings.AppearanceViewModel
import kotlinx.android.synthetic.main.fragment_appearance.*
import javax.inject.Inject

class AppearanceFragment @Inject constructor(
        private val appearanceViewModel: AppearanceViewModel,
        private val themeSelectionDialog: ThemeSelectionDialog
) : BaseFragment(R.layout.fragment_appearance), View.OnClickListener {

    override val fragmentTag: String get() = this.javaClass.simpleName
    override val viewModel: MainViewModel  by viewModels ({ requireActivity() })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        observeViewModel()
        initViews()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onClick(view: View?) = when(view?.id) {
        R.id.txt_theme -> themeSelectionDialog.show(childFragmentManager, themeSelectionDialog.dialogTAG)
        else -> {}
    }

    private fun observeViewModel() {

        appearanceViewModel.onThemeChange.observe(viewLifecycleOwner, {
            when(it) {
                ThemeSelectionDialog.SelectedTheme.DARK -> onDarkThemeClick()
                else -> onLightThemeClick()
            }
        })
    }

    private fun initViews() {
        txt_theme.setOnClickListener(this)
    }

    private fun onLightThemeClick() {}

    private fun onDarkThemeClick() {}
}