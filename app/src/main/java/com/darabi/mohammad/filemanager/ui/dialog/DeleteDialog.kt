package com.darabi.mohammad.filemanager.ui.dialog

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.ui.fragment.base.BaseDialogFragment
import com.darabi.mohammad.filemanager.util.fadeIn
import com.darabi.mohammad.filemanager.vm.DirsListViewModel
import com.darabi.mohammad.filemanager.vm.ViewModelFactory
import kotlinx.android.synthetic.main.dialog_delete.*
import javax.inject.Inject

class DeleteDialog @Inject constructor(
    private val viewModelFactory: ViewModelFactory
) : BaseDialogFragment(), View.OnClickListener {

    override val dialogTAG: String get() = this.javaClass.simpleName
    override val layoutRes: Int get() = R.layout.dialog_delete

    private val viewModel: DirsListViewModel by viewModels { viewModelFactory }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        observeViewModel()
    }

    private fun initViews() {
        delete_dialog_txt_items.text = "caution"
//        delete_dialog_txt_items.text = getSelectedItemNames()
        delete_dialog_btn_delete.setOnClickListener(this)
        delete_dialog_btn_cancel.setOnClickListener(this)
    }

    private fun observeViewModel() {

//        viewModel.deletePercentage.observe(viewLifecycleOwner, {
//            if(it == viewModel.checkedItemCount) dismiss()
//            delete_dialog_prg_status.progress = it
//        })
    }

    private fun getSelectedItemNames(): String {
        var result = ""
//        viewModel.getSelectedItemNames().forEach {
//            result = "$result\n$it"
//        }
        return result
    }

    private fun onDeleteClicked() {
//        delete_dialog_txt_items.text = "0/${viewModel.checkedItemCount}"
        delete_dialog_prg_status.fadeIn()
//        delete_dialog_prg_status.max = viewModel.checkedItemCount
//        viewModel.delete()
    }

    override fun onClick(view: View?) =
        if(view?.id == R.id.delete_dialog_btn_delete) onDeleteClicked() else dismiss()
}