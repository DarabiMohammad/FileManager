package com.darabi.mohammad.filemanager.ui.fragment.contents

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.model.*
import com.darabi.mohammad.filemanager.repository.storage.OnProgressChanged
import com.darabi.mohammad.filemanager.repository.storage.StorageManager
import com.darabi.mohammad.filemanager.ui.dialog.DeleteDialog
import com.darabi.mohammad.filemanager.ui.fragment.base.BaseFragment
import com.darabi.mohammad.filemanager.util.SingleEventWrapper
import com.darabi.mohammad.filemanager.util.fadeIn
import com.darabi.mohammad.filemanager.util.fadeOut
import com.darabi.mohammad.filemanager.view.adapter.content.ContentAdapterCallback
import com.darabi.mohammad.filemanager.view.adapter.content.ContentRecyclerAdapter
import com.darabi.mohammad.filemanager.vm.ccontent.ContentViewModel
import kotlinx.android.synthetic.main.fragment_content.*
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContentFragment @Inject constructor (
    private val deleteDialog: DeleteDialog,
    private val contentViewModel: ContentViewModel,
    private val adapter: ContentRecyclerAdapter
) : BaseFragment (R.layout.fragment_content), View.OnClickListener, ContentAdapterCallback<BaseItem>,
    Observer<Result<ArrayList<out BaseItem>>?>, OnProgressChanged {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        observeViewModel()
        initViews()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onClick(view: View?) = when (view?.id) {
        R.id.btn_fab -> mainViewModel.openNewFileDialog.value = FileType.Directory
        else -> {}
    }

    override fun onChanged(response: Result<ArrayList<out BaseItem>>?) {
        response?.let { result ->
            when (result.status) {
                Status.LOADING -> {}
                Status.SUCCESS -> adapter.setSource(result.result!!).also {
//                    viewModel.updateToobarTitle.value = dirsListViewModel.getCurrentDirectoryName()
                    if(result.result.isNotEmpty()) rcv_content.fadeIn() else rcv_content.fadeOut()
                }
                Status.ERROR -> onError(result.throwable!!)
            }
        }
    }

    override fun onSelectionChanged(isAllSelected: Boolean, item: BaseItem) =
        contentViewModel.onSelectionChanged(item).observe(this, {
            mainViewModel.onActionModeChanged.value = Pair(it, isAllSelected)
        })

    override fun onSelectAll(items: ArrayList<BaseItem>) =
        contentViewModel.onSelectAll(items).observe(this, {
            mainViewModel.onActionModeChanged.value = it
        })

    override fun onRenameClick(item: BaseItem) {}

    override fun onEncryptClick(item: BaseItem) {}

    override fun onDetailsClick(item: BaseItem) {}

    override fun onItemClick(item: BaseItem) {
        if (item is Directory) contentViewModel.getFiles(item.path).observe(this, this)
    }

    override fun onChanged(progress: Int) {
    }

    override fun onBackPressed() = when {
        contentViewModel.getSelectedItemsCount() > 0 -> adapter.unselectAll()
        else -> contentViewModel.onBackPressed().observe(this, this)
    }

    fun getFilesForPath(path: String?) = contentViewModel.getFiles(path).observe(this, this)

    fun getFilesForCategory(categoryType: CategoryType?) = contentViewModel.getFilesForCategory(categoryType).observe(this, this)

    fun onAllSelectionClick(isAllSelected: Boolean) = if (isAllSelected) adapter.selectAll() else adapter.unselectAll()

    fun onDeleteClicked() = deleteDialog.show(childFragmentManager)

    fun onNewFileCreated(fileName: String, fileType: FileType) =
        contentViewModel.createFile(fileName, fileType).observe(viewLifecycleOwner, {
            when (it.status) {
                Status.LOADING -> {}
                Status.SUCCESS -> adapter.addSource(it.result!!.first, it.result.second).also {
                    rcv_content.fadeIn()
                    mainViewModel.openNewFileDialog.value = null
                }
                Status.ERROR -> mainViewModel.onCreateFileError.value = SingleEventWrapper(it.throwable!!.message!!)
            }
        })

    private fun initViews() {
        btn_fab.setOnClickListener(this)
        if(rcv_content.adapter == null) rcv_content.adapter = adapter.also { it.adapterCallback = this@ContentFragment }
    }

    private fun observeViewModel() {

        contentViewModel.onFilesDeleted.observe(viewLifecycleOwner, {
            adapter.apply {
                removeSource(it)
                if (itemCount == 0) rcv_content.fadeOut()
                unselectAll()
                deleteDialog.dismiss()
            }
        })

        mainViewModel.onPathSelected.observe(viewLifecycleOwner, {
            it?.let { singleEventWrapper ->
                singleEventWrapper.getContentIfNotHandled()?.let { destinationData ->
                    contentViewModel.copyOrMove(destinationData.first, destinationData.second, this)
                        .observe(viewLifecycleOwner, CopyMoveHandler())
                }
            }
        })
    }

    private fun onError(throwable: Throwable) = when (throwable) {
        is NullPointerException -> super.onBackPressed()
        is IllegalArgumentException -> {} // todo handle refresh content list here.
        else -> throw throwable
    }

    internal inner class CopyMoveHandler : Observer<Result<Unit>> {
        override fun onChanged(result: Result<Unit>) {
            when (result.status) {
                Status.LOADING -> {}
                Status.SUCCESS -> {}
                Status.ERROR -> makeToast("${result.throwable!!.message}")
            }
        }
    }
}