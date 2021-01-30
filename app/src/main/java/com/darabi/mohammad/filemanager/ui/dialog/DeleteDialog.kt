package com.darabi.mohammad.filemanager.ui.dialog

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.model.Result
import com.darabi.mohammad.filemanager.model.Status
import com.darabi.mohammad.filemanager.ui.fragment.base.BaseDialogFragment
import com.darabi.mohammad.filemanager.util.factory.ViewModelFactory
import com.darabi.mohammad.filemanager.util.fadeIn
import com.darabi.mohammad.filemanager.util.invisible
import com.darabi.mohammad.filemanager.vm.ContentViewModel
import kotlinx.android.synthetic.main.dialog_delete.*
import javax.inject.Inject

class DeleteDialog @Inject constructor(
    private val viewModelFactory: ViewModelFactory
) : BaseDialogFragment(), View.OnClickListener, Observer<Result<Boolean>> {

    override val dialogTag: String get() = this.javaClass.simpleName
    override val layoutRes: Int get() = R.layout.dialog_delete

    private val viewModel: ContentViewModel by viewModels { viewModelFactory }

    private var percentage: Int = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    override fun onResume() {
        super.onResume()
        delete_dialog_txt_items.text = "${getString(R.string.delete_files)} ${viewModel.selectedItemsCount} Files?"
    }

    override fun onClick(view: View?) =
        if(view?.id == R.id.delete_dialog_btn_delete) onDeleteClicked() else dismiss()

    fun show(manager: FragmentManager) = super.show(manager, dialogTag).also { isCancelable = true }

    private fun initViews() {
        delete_dialog_btn_delete.setOnClickListener(this)
        delete_dialog_btn_cancel.setOnClickListener(this)
    }

    private fun onDeleteClicked() {
        isCancelable = false
        delete_dialog_btn_delete.invisible()
        delete_dialog_btn_cancel.invisible()
        delete_dialog_prg_status.fadeIn()
        delete_dialog_prg_status.max = viewModel.selectedItemsCount
        viewModel.deleteFiles().observe(viewLifecycleOwner, this)
    }

    override fun onChanged(response: Result<Boolean>) {
        when (response.status) {
            Status.LOADING -> {}
            Status.SUCCESS -> {
                if (response.result!!) {
                    while (delete_dialog_prg_status.progress == viewModel.selectedItemsCount) {
                        delete_dialog_prg_status.progress.inc()
                    }
                    viewModel.onFilesDeleted.value = response.result
                }
            }
            Status.ERROR -> {}
        }
    }
}