package com.darabi.mohammad.filemanager.ui.dialog

import android.os.Bundle
import android.view.View
import android.widget.RadioGroup
import androidx.fragment.app.viewModels
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.ui.fragment.base.BaseDialogFragment
import com.darabi.mohammad.filemanager.vm.ViewModelFactory
import com.darabi.mohammad.filemanager.vm.settings.AppearanceViewModel
import kotlinx.android.synthetic.main.dialog_theme_selection.*
import javax.inject.Inject

class ThemeSelectionDialog @Inject constructor(
        private val viewModelFactory: ViewModelFactory
) : BaseDialogFragment(), View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    override val TAG: String get() = this.javaClass.simpleName
    override val layoutRes: Int get() = R.layout.dialog_theme_selection

    enum class SelectedTheme { LIGHT, DARK }

    private val viewModel: AppearanceViewModel by viewModels { viewModelFactory }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    override fun onClick(view: View?) { dialog?.dismiss() }

    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        when(checkedId) {
            R.id.rdb_light_theme -> { viewModel.onThemeChange.value = SelectedTheme.LIGHT }
            R.id.rdb_dark_theme -> { viewModel.onThemeChange.value = SelectedTheme.DARK }
        }
        dialog?.dismiss()
    }

    private fun initViews() {
        btn_cancel.setOnClickListener(this)
        container_radio_group.setOnCheckedChangeListener(this)
    }
}