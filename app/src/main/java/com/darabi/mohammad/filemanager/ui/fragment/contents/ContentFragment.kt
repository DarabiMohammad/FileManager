package com.darabi.mohammad.filemanager.ui.fragment.contents

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.model.*
import com.darabi.mohammad.filemanager.ui.dialog.DeleteDialog
import com.darabi.mohammad.filemanager.ui.dialog.NewFileDialog
import com.darabi.mohammad.filemanager.ui.fragment.base.BaseFragment
import com.darabi.mohammad.filemanager.util.fadeIn
import com.darabi.mohammad.filemanager.util.fadeOut
import com.darabi.mohammad.filemanager.view.adapter.dirs.DirsAdapterCallback
import com.darabi.mohammad.filemanager.view.adapter.dirs.ContentRecyclerAdapter
import com.darabi.mohammad.filemanager.vm.ContentViewModel
import com.darabi.mohammad.filemanager.vm.base.MainViewModel
import kotlinx.android.synthetic.main.fragment_dirs_list.*
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContentFragment @Inject constructor (
    private val newFileDialog: NewFileDialog,
    private val deleteDialog: DeleteDialog,
    private val contentViewModel: ContentViewModel,
    private val adapter: ContentRecyclerAdapter
) : BaseFragment(R.layout.fragment_dirs_list), View.OnClickListener, DirsAdapterCallback<BaseItem> {

    override val fragmentTag: String get() = this.javaClass.simpleName
    override val viewModel: MainViewModel by viewModels( { requireActivity() } )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        observeViewModel()
        initViews()
        super.onViewCreated(view, savedInstanceState)
        resources.configuration.uiMode
    }

    override fun onClick(view: View?) = when (view?.id) {
        R.id.btn_fab -> onFabClicked()
        else -> {}
    }

    override fun onSelectionChanged(selectedItemCount: Int, isAllSelected: Boolean, item: BaseItem) {
        if (item.isSelected) contentViewModel.selectedItems.add(item) else contentViewModel.selectedItems.remove(item)
        contentViewModel.selectedItemsCount = selectedItemCount
        viewModel.onActionModeChanged.value = Pair(selectedItemCount, isAllSelected)
    }

    override fun onSelectAll(selectedItemCount: Int, items: List<BaseItem>) {
        contentViewModel.selectedItemsCount = selectedItemCount
        contentViewModel.selectedItems.clear().also { contentViewModel.selectedItems.addAll(items) }
        viewModel.onActionModeChanged.value = Pair(selectedItemCount, true)
    }

    override fun onUnselectAll() {
        contentViewModel.selectedItemsCount = 0
        contentViewModel.selectedItems.clear()
        viewModel.onActionModeChanged.value = Pair(0, false)
    }

    override fun onRenameClick(item: BaseItem) {}

    override fun onEncryptClick(item: BaseItem) {}

    override fun onDetailsClick(item: BaseItem) {}

    override fun onItemClick(item: BaseItem) {
        if (item is Directory)
            contentViewModel.getFiles(item.path)
    }

    override fun onBackPressed() {
        if (adapter.hasSelection()) adapter.unselectAll() else contentViewModel.onBackPressed()
    }

    fun getFilesForPath(path: String) = contentViewModel.getFiles(path)

    fun getFilesForCategory(categoryType: CategoryType) = contentViewModel.getFilesForCategory(categoryType)

    fun onAllSelectionClick(isAllSelected: Boolean) = if (isAllSelected) adapter.selectAll() else adapter.unselectAll()

    fun onDeleteClicked() = deleteDialog.show(childFragmentManager)

    fun onCopyClicked() = contentViewModel.copy()

    fun onMoveClicked() = contentViewModel.move()

    private fun initViews() {
        btn_fab.setOnClickListener(this)
        if(rcv_dirs.adapter == null) rcv_dirs.adapter = adapter.also { it.adapterCallback = this@ContentFragment }
    }

    private fun observeViewModel() {

        contentViewModel.filesLiveData.observe(viewLifecycleOwner, { response ->
            when (response.status) {
                Status.LOADING -> {}
                Status.SUCCESS -> adapter.setSource(response.result!!).also {
//                    viewModel.updateToobarTitle.value = dirsListViewModel.getCurrentDirectoryName()
                    if(response.result.isNotEmpty()) rcv_dirs.fadeIn() else rcv_dirs.fadeOut()
                }
                Status.ERROR -> onError(response.throwable!!)
            }
        })

        contentViewModel.onFileCreated.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.LOADING -> {}
                Status.SUCCESS -> adapter.addSource(it.result!!.first, it.result.second).also {
                    newFileDialog.dismiss()
                    rcv_dirs.fadeIn()
                }
                Status.ERROR -> onError(it.throwable!!)
            }
        })

        contentViewModel.onFilesDeleted.observe(viewLifecycleOwner, {
            if (it) adapter.apply {
                removeSource(contentViewModel.selectedItems)
                if (itemCount == 0) rcv_dirs.fadeOut()
                unselectAll()
                deleteDialog.dismiss()
            } else throw Exception("$fragmentTag delete files failed")
        })
    }

    private fun onError(throwable: Throwable) = when (throwable) {
        is NullPointerException -> super.onBackPressed()
        is IOException -> makeToast("${throwable.message}")
        else -> throw throwable
    }

    private fun onFabClicked() = newFileDialog.forFolder().show(childFragmentManager, newFileDialog.dialogTag)
}