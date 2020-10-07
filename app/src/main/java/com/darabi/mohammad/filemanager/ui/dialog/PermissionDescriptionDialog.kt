package com.darabi.mohammad.filemanager.ui.dialog

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.ui.fragment.base.BaseDialogFragment
import com.darabi.mohammad.filemanager.vm.HomeViewModel
import com.darabi.mohammad.filemanager.vm.MainViewModel
import com.darabi.mohammad.filemanager.vm.ViewModelFactory
import kotlinx.android.synthetic.main.dialog_permission_description.*
import javax.inject.Inject

class PermissionDescriptionDialog @Inject constructor(
    private val viewModelFactory: ViewModelFactory
) : BaseDialogFragment(), View.OnClickListener {

    override val TAG: String get() = this.javaClass.simpleName
    override val layoutRes: Int get() = R.layout.dialog_permission_description

    enum class DialogAction { ACTION_OK, ACTION_OPEN_SETTINGS, ACTION_EXIT }
    private enum class DialogType { COMMON_TYPE, DETAILED_TYPE, SETTINGS_TYPE }

    private val viewModel: MainViewModel by viewModels { viewModelFactory }
    private var type = DialogType.COMMON_TYPE

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    private fun initViews() {
        if (type == DialogType.DETAILED_TYPE) {
            txt_dialog_permission_desc.text = getString(R.string.permission_detailed_description)
            btn_dialog_permission_desc_exit.visibility = View.VISIBLE
        }
        else if (type == DialogType.SETTINGS_TYPE) {
            txt_dialog_permission_desc.text = getString(R.string.permission_final_description)
            btn_dialog_permission_desc_ok.text = getString(R.string.app_info)
            btn_dialog_permission_desc_exit.visibility = View.VISIBLE
        }
        btn_dialog_permission_desc_ok.setOnClickListener(this)
        btn_dialog_permission_desc_exit.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when(view?.id) {
            R.id.btn_dialog_permission_desc_ok -> onOkButtonClick()
            else -> onExitButtonClick()
        }
    }

    private fun onOkButtonClick() {
        this.dismiss()
        when(type) {
            DialogType.SETTINGS_TYPE -> viewModel.onPermissionDIalogDescButtonClick.value = DialogAction.ACTION_OPEN_SETTINGS
            else -> viewModel.onPermissionDIalogDescButtonClick.value = DialogAction.ACTION_OK
        }
    }

    private fun onExitButtonClick() {
        this.dismiss()
        viewModel.onPermissionDIalogDescButtonClick.value = DialogAction.ACTION_EXIT
    }

    fun detailedDialog(): PermissionDescriptionDialog {
        type = DialogType.DETAILED_TYPE
        return this
    }

    fun finalDialog(): PermissionDescriptionDialog {
        type = DialogType.SETTINGS_TYPE
        return this
    }
}