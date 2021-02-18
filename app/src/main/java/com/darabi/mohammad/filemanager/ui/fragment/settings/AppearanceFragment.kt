package com.darabi.mohammad.filemanager.ui.fragment.settings

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.ui.dialog.ThemeSelectionDialog
import com.darabi.mohammad.filemanager.ui.fragment.base.BaseFragment
import com.darabi.mohammad.filemanager.util.factory.ViewModelFactory
import com.darabi.mohammad.filemanager.vm.base.MainViewModel
import com.darabi.mohammad.filemanager.vm.settings.AppearanceViewModel
import kotlinx.android.synthetic.main.fragment_appearance.*
import javax.inject.Inject

class AppearanceFragment @Inject constructor(
    private val viewModelFactory: ViewModelFactory,
    private val themeSelectionDialog: ThemeSelectionDialog
) : BaseFragment(R.layout.fragment_appearance), View.OnClickListener {

    override val fragmentTag: String get() = this.javaClass.simpleName
    override val mainViewModel: MainViewModel by viewModels( { requireActivity() } )

    private val appearanceViewModel: AppearanceViewModel by viewModels { viewModelFactory }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        observeViewModel()
        initViews()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onClick(view: View?) = when(view?.id) {
        R.id.txt_theme -> themeSelectionDialog.show(childFragmentManager, themeSelectionDialog.dialogTag)
        else -> {}
    }

    private fun observeViewModel() {

        appearanceViewModel.onThemeChange.observe(viewLifecycleOwner, {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            when(it) {
                ThemeSelectionDialog.SelectedTheme.DARK -> onDarkThemeClick()
                else -> onLightThemeClick()
            }
        })
    }

    private fun initViews() {
        txt_theme.setOnClickListener(this)
    }

    private fun onLightThemeClick() {
        fragment_appearance.isActivated = false
//        container_appearance_scr_childs.children.forEach {
//            it.isActivated = false
//        }
    }

    private fun onDarkThemeClick() {
        fragment_appearance.isActivated = true
//        container_appearance_scr_childs.children.forEach {
//            it.isActivated = true
//        }
    }

    override fun onPause() {
        appearanceViewModel.apply {
            onThemeChange.value?.let {
                if (it.theme != appearanceViewModel.theme) {
                    setTheme(it.theme)
                    mainViewModel.onThemeChanged.value = it.theme
                }
            }
        }
        super.onPause()
    }
}