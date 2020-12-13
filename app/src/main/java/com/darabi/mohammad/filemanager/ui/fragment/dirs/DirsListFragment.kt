package com.darabi.mohammad.filemanager.ui.fragment.dirs

import android.os.Bundle
import android.util.Log
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
import com.darabi.mohammad.filemanager.view.adapter.dirs.DirsRecyclerAdapter
import com.darabi.mohammad.filemanager.vm.DirsListViewModel
import com.darabi.mohammad.filemanager.vm.MainViewModel
import kotlinx.android.synthetic.main.fragment_dirs_list.*
import java.lang.IndexOutOfBoundsException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DirsListFragment @Inject constructor (
    private val newFileDialog: NewFileDialog,
    private val deleteDialog: DeleteDialog,
    private val dirsListViewModel: DirsListViewModel,
    private val adapter: DirsRecyclerAdapter
) : BaseFragment(R.layout.fragment_dirs_list), View.OnClickListener, DirsAdapterCallback<FileItem> {

    override val fragmentTag: String get() = this.javaClass.simpleName
    override val viewModel: MainViewModel by viewModels( { requireActivity() } )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        observeViewModel()
        initViews()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onClick(view: View?) {}

    override fun onAllSelected() {}

    override fun onClear() {}

    override fun onCheckStateChange(position: Int, isChecked: Boolean) {}

    override fun onRenameClick(item: FileItem) {}

    override fun onEncryptClick(item: FileItem) {}

    override fun onDetailsClick(item: FileItem) {}

    override fun onItemClick(item: FileItem) {
        if (item is Directory) dirsListViewModel.getFiles(item.treePosition)
    }

    override fun onBackPressed() {
        dirsListViewModel.upToPervious()
    }

    fun getPerimaryStorageFiles() = dirsListViewModel.getPrimaryStorageRootFiles()

    fun getSecondaryStorageFiles() = dirsListViewModel.getSecondaryStorageRootFiles()

    fun getFilesForCategory(category: Category) {}

    private fun initViews() {
        btn_fab.setOnClickListener(this)
        if(rcv_dirs.adapter == null) rcv_dirs.adapter = adapter.also { it.adapterCallback = this@DirsListFragment }
    }

    private fun observeViewModel() {

        dirsListViewModel.filesLiveData.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.LOADING -> {}
                Status.SUCCESS -> {
                    if(it.result!!.isNotEmpty()) adapter.setSource(it.result).also { rcv_dirs.fadeIn() } else rcv_dirs.fadeOut()
                }
                Status.ERROR -> {
                    if (it.throwable is IndexOutOfBoundsException) throw it.throwable
                    super.onBackPressed()
                }
            }
        })
    }
}
//) : BaseFragment(R.layout.fragment_dirs_list), View.OnClickListener, DirsAdapterCallback<BaseFile> {
//
//    override val fragmentTag: String get() = this.javaClass.simpleName
//    override val viewModel: MainViewModel by viewModels( { requireActivity() } )
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//
//        observeViewModel()
//        initViews()
//        super.onViewCreated(view, savedInstanceState)
//    }
//
//    override fun onBackPressed() =
//        if (adapter.hasCheckedItem())
//            viewModel.onSelectAllClick.value = false
//        else
//            dirsListViewModel.onBackPressed()
//
//    override fun onClick(view: View?) = when(view?.id) {
//        R.id.btn_fab -> onFabClick(true)
//        else -> {}
//    }
//
//    override fun onItemClick(item: BaseFile) {
////        viewModel.onItemClick.run {
////            dirsListViewModel.savePath(this.value!!.itemPath, getAdapterPosition())
////            this.value = item as FileItem
////        }
//    }
//
//    override fun onCheckStateChange(position: Int, isChecked: Boolean) {
////        viewModel.onActionModeChange.value = dirsListViewModel.onItemCheckedChange(position, isChecked)
//    }
//
//    override fun onRenameClick(item: BaseFile) {}
//    override fun onEncryptClick(item: BaseFile) {}
//    override fun onDetailsClick(item: BaseFile) {}
//
//    override fun onAllSelected() {
////        viewModel.onActionModeChange.value = dirsListViewModel.onAllItemsSelected()
//    }
//
//    override fun onClear() {
////        viewModel.onActionModeChange.value = dirsListViewModel.onAllItemsClear()
//    }
//
//    private fun initViews() {
//        btn_fab.setOnClickListener(this)
//        if(rcv_dirs.adapter == null) rcv_dirs.adapter = adapter.also { it.adapterCallback = this@DirsListFragment }
//    }
//
//    private fun observeViewModel() {
//
////        viewModel.onItemClick.observe(viewLifecycleOwner, { FileItem ->
////            FileItem.takeIf { it?.itemType != ItemType.DRAWER_ITEM_OTHER }?.let {
////                if(it.itemType == ItemType.DRAWER_ITEM_CATEGORY)
////                    dirsListViewModel.saveCurrentPath(getAdapterPosition())
////                getSubDirs(it)
////            }
////        })
//
//        viewModel.onSelectAllClick.observe(viewLifecycleOwner, { if(it) adapter.selectAll() else adapter.clearAll() })
//
//        viewModel.onDeleteClicked.observe(viewLifecycleOwner, { /*showDeleteDialog()*/ })
//
////        dirsListViewModel.fileOrFolderCreation.observe(viewLifecycleOwner, { adapter.updateSource(it) })
//    }
//
//    private fun getSubDirs(item: FileItem) = try {
//        dirsListViewModel.getSubFiles(item).also {
//            if(it.isNotEmpty())
//                adapter.setSource(it, dirsListViewModel.getMaxCheckableItemCount()).also {
//                    prepareRecyclerView(dirsListViewModel.getAdapterPosition())
//                }
//            else rcv_dirs.fadeOut()
//        }
//    } catch (exception: VolumeManager.VolumeManagerException) { viewModel.onItemClick.value = null }
//
//    private fun getAdapterPosition(): Int = (rcv_dirs.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
//
//    private fun prepareRecyclerView(visiblePosition: Int) = rcv_dirs.apply {
//        this.fadeIn()
//        (this.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(visiblePosition, 0)
//    }
//
//    private fun onFabClick(fileCreationDialog: Boolean) = newFileDialog.also {
//        if (fileCreationDialog) it.fileType() else it.folderType()
//    }.show(childFragmentManager, newFileDialog.dialogTAG)
//
//    private fun firstVisibleItemPosition(): Int = (rcv_dirs.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
//
//    private fun showDeleteDialog() = deleteDialog.show(childFragmentManager, newFileDialog.dialogTAG)
//}