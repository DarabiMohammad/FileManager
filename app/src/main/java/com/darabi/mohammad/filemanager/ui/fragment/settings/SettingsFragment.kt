package com.darabi.mohammad.filemanager.ui.fragment.settings

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.ui.fragment.base.BaseFragment
import com.darabi.mohammad.filemanager.util.navigateTo
import com.darabi.mohammad.filemanager.vm.base.MainViewModel
import com.darabi.mohammad.filemanager.vm.settings.SettingsViewModel
import kotlinx.android.synthetic.main.fragment_settings.*
import javax.inject.Inject

class SettingsFragment @Inject constructor(
        private val settingsViewModel: SettingsViewModel,
        private val appearanceFragment: AppearanceFragment
) : BaseFragment(R.layout.fragment_settings), View.OnClickListener {

    override val fragmentTag: String get() = this.javaClass.simpleName
    override val viewModel: MainViewModel by viewModels( { requireActivity() } )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        observeViewModel()
        initViews()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        Log.d("test", "============onResume")
    }

    override fun onBackPressed() =
            if (childFragmentManager.backStackEntryCount > 0)
                childFragmentManager.popBackStack()
            else super.onBackPressed()

    private fun observeViewModel() {}

    private fun initViews() {
        txt_appearance.setOnClickListener(this)
        switch_show_hidden_files.isChecked = settingsViewModel.isHiddenFilesEnabled
        switch_split_contents.isChecked = settingsViewModel.isSplitModeEnabled
    }

    override fun onClick(view: View?) = when(view?.id) {
        R.id.txt_appearance -> navigateTo(R.id.container_settings, appearanceFragment, addToBackStack = true)
        else -> {}
    }

    override fun onPause() {
        settingsViewModel.apply {
            setShowHiddenFiles(switch_show_hidden_files.isChecked)
            setShowSplitViews(switch_split_contents.isChecked)
        }
        super.onPause()
    }
}