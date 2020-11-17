package com.darabi.mohammad.filemanager.ui.fragment.settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.ui.fragment.base.BaseFragment
import com.darabi.mohammad.filemanager.util.navigateTo
import com.darabi.mohammad.filemanager.vm.MainViewModel
import com.darabi.mohammad.filemanager.vm.SettingsViewModel
import kotlinx.android.synthetic.main.fragment_settings.*
import javax.inject.Inject

class SettingsFragment @Inject constructor(
    private val settingsViewModel: SettingsViewModel,
    private val appearanceFragment: AppearanceFragment
) : BaseFragment(R.layout.fragment_settings), View.OnClickListener {

    override val fragmentTag: String get() = this.javaClass.simpleName
    override val viewModel: MainViewModel by viewModels ({ requireActivity() })

    override fun saveUiState(bundle: Bundle) {}

    override fun retrieveUiState(bundle: Bundle) {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        observeViewModel()
        initViews()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onBackPressed() {
        if(childFragmentManager.backStackEntryCount > 0)
            childFragmentManager.popBackStack()
        else super.onBackPressed()
    }

    private fun observeViewModel() {}

    private fun initViews() {
        container_appearance.setOnClickListener(this)
    }

    override fun onClick(view: View?) = when(view?.id) {
        R.id.container_appearance -> navigateTo(R.id.container_settings, appearanceFragment, addToBackStack = true)
        else -> {}
    }
}