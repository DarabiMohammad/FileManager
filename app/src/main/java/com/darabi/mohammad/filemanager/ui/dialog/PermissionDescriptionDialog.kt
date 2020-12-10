package com.darabi.mohammad.filemanager.ui.dialog

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.ui.fragment.base.BaseDialogFragment
import com.darabi.mohammad.filemanager.vm.MainViewModel
import com.darabi.mohammad.filemanager.vm.ViewModelFactory
import kotlinx.android.synthetic.main.dialog_permission_description.*
import javax.inject.Inject

class PermissionDescriptionDialog @Inject constructor(
    private val viewModelFactory: ViewModelFactory
) : BaseDialogFragment(), View.OnClickListener {

    override val dialogTAG: String get() = this.javaClass.simpleName
    override val layoutRes: Int get() = R.layout.dialog_permission_description

    enum class DialogAction { ACTION_OK, ACTION_OPEN_SETTINGS, ACTION_EXIT }
    private enum class DialogType { COMMON_TYPE, DETAILED_TYPE, SETTINGS_TYPE }

    private val viewModel: MainViewModel by viewModels { viewModelFactory }
    private var type = DialogType.COMMON_TYPE

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    override fun onClick(view: View?) {
        when(view?.id) {
            R.id.btn_dialog_permission_desc_ok -> onOkButtonClick()
            else -> onExitButtonClick()
        }
    }

    private fun initViews() {
        when (type) {
            DialogType.COMMON_TYPE -> this.isCancelable = false
            DialogType.DETAILED_TYPE -> {
                this.isCancelable = true
                txt_dialog_permission_desc.text = getString(R.string.permission_detailed_description)
                btn_dialog_permission_desc_exit.visibility = View.VISIBLE
            }
            DialogType.SETTINGS_TYPE -> {
                this.isCancelable = true
                txt_dialog_permission_desc.text = getString(R.string.permission_final_description)
                btn_dialog_permission_desc_ok.text = getString(R.string.app_info)
                btn_dialog_permission_desc_exit.visibility = View.VISIBLE
            }
        }
        btn_dialog_permission_desc_ok.setOnClickListener(this)
        btn_dialog_permission_desc_exit.setOnClickListener(this)
    }

    private fun onOkButtonClick() {
        this.dismiss()
        val buttonAction = if (type == DialogType.SETTINGS_TYPE) DialogAction.ACTION_OPEN_SETTINGS else DialogAction.ACTION_OK
        viewModel.permissionDialoLiveData.value = buttonAction
    }

    private fun onExitButtonClick() {
        this.dismiss()
        viewModel.permissionDialoLiveData.value = DialogAction.ACTION_EXIT
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