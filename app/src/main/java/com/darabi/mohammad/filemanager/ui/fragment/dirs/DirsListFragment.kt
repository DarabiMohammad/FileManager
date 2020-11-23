package com.darabi.mohammad.filemanager.ui.fragment.dirs

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.model.BaseItem
import com.darabi.mohammad.filemanager.model.DirItem
import com.darabi.mohammad.filemanager.model.ItemType
import com.darabi.mohammad.filemanager.ui.dialog.DeleteDialog
import com.darabi.mohammad.filemanager.ui.dialog.NewFileDialog
import com.darabi.mohammad.filemanager.ui.fragment.base.BaseFragment
import com.darabi.mohammad.filemanager.util.EMPTY_STRING
import com.darabi.mohammad.filemanager.util.fadeIn
import com.darabi.mohammad.filemanager.util.fadeOut
import com.darabi.mohammad.filemanager.util.storage.VolumeManager
import com.darabi.mohammad.filemanager.view.adapter.dirs.DirsAdapterCallback
import com.darabi.mohammad.filemanager.view.adapter.dirs.DirsRecyclerAdapter
import com.darabi.mohammad.filemanager.vm.DirsListViewModel
import com.darabi.mohammad.filemanager.vm.MainViewModel
import kotlinx.android.synthetic.main.fragment_dirs_list.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DirsListFragment @Inject constructor (
    private val newFileDialog: NewFileDialog,
    private val deleteDialog: DeleteDialog,
    private val dirsListViewModel: DirsListViewModel,
    private val adapter: DirsRecyclerAdapter
) : BaseFragment(R.layout.fragment_dirs_list), View.OnClickListener, DirsAdapterCallback<DirItem> {

    override val fragmentTag: String get() = this.javaClass.simpleName
    override val viewModel: MainViewModel by viewModels( { requireActivity() } )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        observeViewModel()
        initViews()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onBackPressed() {
        if (adapter.hasCheckedItem())
            adapter.clearAll()
        else
            viewModel.onItemClick.value = dirsListViewModel.previousPath()
    }

    override fun onClick(view: View?) = when(view?.id) {
        R.id.btn_fab -> onFabClick(true)
        else -> {}
    }

    override fun onItemClick(item: DirItem) {
        if (item is DirItem.Item) viewModel.onItemClick.value = item
    }

    override fun onCheckStateChange(position: Int, isChecked: Boolean) {
        viewModel.onActionModeChange.value = dirsListViewModel.onItemCheckedChange(position, isChecked)
    }

    override fun onRenameClick(item: DirItem) {
    }

    override fun onEncryptClick(item: DirItem) {
    }

    override fun onDetailsClick(item: DirItem) {
    }

    override fun onAllSelected() { viewModel.onActionModeChange.value = dirsListViewModel.onAllItemsSelected() }

    override fun onClear() { viewModel.onActionModeChange.value = dirsListViewModel.onAllItemsClear() }

    private fun initViews() {
        btn_fab.setOnClickListener(this)
        if(rcv_dirs.adapter == null) rcv_dirs.adapter = adapter.also { it.adapterCallback = this@DirsListFragment }
    }

    private fun observeViewModel() {

        viewModel.onItemClick.observe(viewLifecycleOwner, { baseItem ->
            baseItem?.let { if(it.itemType != ItemType.DRAWER_ITEM_OTHER) getSubDirs(it) }
        })

        viewModel.onSelectAllClick.observe(viewLifecycleOwner, { if(it) adapter.selectAll() else adapter.clearAll() })

        viewModel.onDeleteClicked.observe(viewLifecycleOwner, { /*showDeleteDialog()*/ })

        dirsListViewModel.fileOrFolderCreation.observe(viewLifecycleOwner, { adapter.updateSource(it) })
    }

    private fun firstVisibleItemPosition(): Int = (rcv_dirs.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()

    private fun getSubDirs(item: BaseItem) = try {
        dirsListViewModel.getSubFiles(item).takeIf { it.isNotEmpty() }.also {
            if(it != null) {
                adapter.setSource(it, dirsListViewModel.getMaxCheckableItemCount())
                rcv_dirs.fadeIn()
            } else rcv_dirs.fadeOut()
        }
    } catch (exception: VolumeManager.VolumeManagerException) {
        dirsListViewModel.currentPath = EMPTY_STRING
        viewModel.onItemClick.value = null
    }

    private fun onFabClick(fileCreationDialog: Boolean) = newFileDialog.also {
        if (fileCreationDialog) it.fileType() else it.folderType()
    }.show(childFragmentManager, newFileDialog.dialogTAG)

    private fun showDeleteDialog() = deleteDialog.show(childFragmentManager, newFileDialog.dialogTAG)
}