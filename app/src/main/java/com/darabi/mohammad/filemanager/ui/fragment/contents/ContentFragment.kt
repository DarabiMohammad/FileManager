package com.darabi.mohammad.filemanager.ui.fragment.contents

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.model.*
import com.darabi.mohammad.filemanager.ui.dialog.DeleteDialog
import com.darabi.mohammad.filemanager.ui.dialog.NewFileDialog
import com.darabi.mohammad.filemanager.ui.fragment.base.BaseFragment
import com.darabi.mohammad.filemanager.util.fadeIn
import com.darabi.mohammad.filemanager.util.fadeOut
import com.darabi.mohammad.filemanager.view.adapter.dirs.DirsAdapterCallback
import com.darabi.mohammad.filemanager.view.adapter.dirs.DirsRecyclerAdapter
import com.darabi.mohammad.filemanager.vm.DirsListViewModel
import com.darabi.mohammad.filemanager.vm.base.MainViewModel
import kotlinx.android.synthetic.main.fragment_dirs_list.*
import kotlinx.coroutines.isActive
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContentFragment @Inject constructor (
    private val newFileDialog: NewFileDialog,
    private val deleteDialog: DeleteDialog,
    private val dirsListViewModel: DirsListViewModel,
    private val adapter: DirsRecyclerAdapter
) : BaseFragment(R.layout.fragment_dirs_list), View.OnClickListener, DirsAdapterCallback<BaseItem> {

    override val fragmentTag: String get() = this.javaClass.simpleName
    override val viewModel: MainViewModel by viewModels( { requireActivity() } )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        observeViewModel()
        initViews()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onClick(view: View?) = when (view?.id) {
        R.id.btn_fab -> onFabClicked()
        else -> {}
    }

    override fun onSelectionChanged(selectedItemCount: Int, isAllSelected: Boolean, item: BaseItem) {
        viewModel.onActionModeChanged.value = Pair(selectedItemCount, isAllSelected).also {
            dirsListViewModel.selectedItemsCount = selectedItemCount
            if (selectedItemCount == 1) dirsListViewModel.selectedItems = arrayListOf()
            if (item.isSelected) dirsListViewModel.selectedItems!!.add(item) else dirsListViewModel.selectedItems!!.remove(item)
        }
    }

    override fun onSelectAll(selectedItemCount: Int, items: List<BaseItem>) {
        viewModel.onActionModeChanged.value = Pair(selectedItemCount, true)
        dirsListViewModel.selectedItems = items as ArrayList<BaseItem>
    }

    override fun onUnselectAll() {
        viewModel.onActionModeChanged.value = Pair(0, false)
    }

    override fun onRenameClick(item: BaseItem) {}

    override fun onEncryptClick(item: BaseItem) {}

    override fun onDetailsClick(item: BaseItem) {}

    override fun onItemClick(item: BaseItem) {
        if (item is Directory) dirsListViewModel.getFiles(item.path)
    }

    override fun onBackPressed() {
        if (adapter.hasSelection()) adapter.unselectAll() else dirsListViewModel.onBackPressed()
    }

    fun getFilesForPath(path: String) = dirsListViewModel.getFiles(path)

    fun getFilesForCategory(categoryType: CategoryType) = dirsListViewModel.getFilesForCategory(categoryType)

    fun onAllSelectionClick(isAllSelected: Boolean) = if (isAllSelected) adapter.selectAll() else adapter.unselectAll()

    fun onDeleteClicked() = deleteDialog.show(childFragmentManager)

    private fun initViews() {
        btn_fab.setOnClickListener(this)
        if(rcv_dirs.adapter == null) rcv_dirs.adapter = adapter.also { it.adapterCallback = this@ContentFragment }
    }

    private fun observeViewModel() {

        dirsListViewModel.filesLiveData.observe(viewLifecycleOwner, { response ->
            when (response.status) {
                Status.LOADING -> {}
                Status.SUCCESS -> adapter.setSource(response.result!!).also {
//                    viewModel.updateToobarTitle.value = dirsListViewModel.getCurrentDirectoryName()
                    if(response.result.isNotEmpty()) rcv_dirs.fadeIn() else rcv_dirs.fadeOut()
                }
                Status.ERROR -> onError(response.throwable!!)
            }
        })

        dirsListViewModel.onFileCreated.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.LOADING -> {}
                Status.SUCCESS -> adapter.addSource(it.result!!.first, it.result.second).also {
                    newFileDialog.dismiss()
                    rcv_dirs.fadeIn()
                }
                Status.ERROR -> onError(it.throwable!!)
            }
        })
    }

    private fun onError(throwable: Throwable) = when (throwable) {
        is NullPointerException -> super.onBackPressed()
        is IOException -> makeToast("${throwable.message}")
        else -> {}
    }

    private fun onFabClicked() = newFileDialog.forFolder().show(childFragmentManager, newFileDialog.dialogTag)
}