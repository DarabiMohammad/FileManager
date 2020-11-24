package com.darabi.mohammad.filemanager.ui.fragment.settings

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CompoundButton
import androidx.fragment.app.viewModels
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.ui.fragment.base.BaseFragment
import com.darabi.mohammad.filemanager.util.navigateTo
import com.darabi.mohammad.filemanager.vm.MainViewModel
import com.darabi.mohammad.filemanager.vm.settings.SettingsViewModel
import kotlinx.android.synthetic.main.fragment_settings.*
import javax.inject.Inject

class SettingsFragment @Inject constructor(
        private val settingsViewModel: SettingsViewModel,
        private val appearanceFragment: AppearanceFragment
) : BaseFragment(R.layout.fragment_settings), View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    override val fragmentTag: String get() = this.javaClass.simpleName
    override val viewModel: MainViewModel by viewModels ({ requireActivity() })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        observeViewModel()
        initViews()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d("test","==================onSaveInstanceState Settings Fragment")
    }

    override fun onBackPressed() {
        if(childFragmentManager.backStackEntryCount > 0)
            childFragmentManager.popBackStack()
        else super.onBackPressed()
    }

    private fun observeViewModel() {}

    private fun initViews() {
        txt_appearance.setOnClickListener(this)
        switch_show_hidden_files.setOnCheckedChangeListener(this)
        switch_split_contents.setOnCheckedChangeListener(this)
    }

    override fun onClick(view: View?) = when(view?.id) {
        R.id.txt_appearance -> navigateTo(R.id.container_settings, appearanceFragment, addToBackStack = true)
        else -> {}
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) = when(buttonView?.id) {
        R.id.switch_show_hidden_files -> {}
        R.id.switch_split_contents -> {}
        else -> {}
    }
}