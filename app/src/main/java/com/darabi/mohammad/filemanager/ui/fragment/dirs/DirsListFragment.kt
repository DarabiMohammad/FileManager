package com.darabi.mohammad.filemanager.ui.fragment.dirs

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.model.DirItem
import com.darabi.mohammad.filemanager.model.ItemType
import com.darabi.mohammad.filemanager.ui.dialog.DeleteDialog
import com.darabi.mohammad.filemanager.ui.dialog.NewFileDialog
import com.darabi.mohammad.filemanager.ui.fragment.base.BaseFragment
import com.darabi.mohammad.filemanager.util.EMPTY_STRING
import com.darabi.mohammad.filemanager.util.fadeIn
import com.darabi.mohammad.filemanager.util.fadeOut
import com.darabi.mohammad.filemanager.util.storage.VolumeManager
import com.darabi.mohammad.filemanager.view.adapter.DirsRecyclerAdapter
import com.darabi.mohammad.filemanager.view.adapter.checkable.BaseCheckableAdapter
import com.darabi.mohammad.filemanager.view.adapter.checkable.BaseCheckableAdapter.Companion.DESTROY_SELECTION_ACTION_MODE
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
) : BaseFragment(R.layout.fragment_dirs_list), View.OnClickListener,
    BaseCheckableAdapter.CheckableAdapterCallback<DirItem> {

    override val fragmentTag: String get() = this.javaClass.simpleName
    override val viewModel: MainViewModel by viewModels( { requireActivity() } )

    private companion object {
        const val FIRST_COMPLETELY_VISIBLE_ITEM_POSITION = "first_completely_visible_item_position"
    }

    override fun saveUiState(bundle: Bundle) {
        bundle.putInt(FIRST_COMPLETELY_VISIBLE_ITEM_POSITION, firstVisibleItemPosition())

    }

    override fun retrieveUiState(bundle: Bundle) {
        rcv_dirs.layoutManager?.scrollToPosition(bundle.getInt(FIRST_COMPLETELY_VISIBLE_ITEM_POSITION))
        if(dirsListViewModel.currentPath.isNotEmpty()) {
            viewModel.onItemClick.removeObservers(viewLifecycleOwner)
            getSubDirs(dirsListViewModel.currentPath)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        observeViewModel()
        initViews()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initViews() {
        btn_fab.setOnClickListener(this)
        adapter.adapterCallback = this
        if(rcv_dirs.adapter == null) rcv_dirs.adapter = adapter
    }

    private fun observeViewModel() {

        viewModel.onItemClick.observe(viewLifecycleOwner, { baseItem ->
            baseItem?.let {
                if(it.itemType == ItemType.DRAWER_ITEM_OTHER) return@observe
                getSubDirs(it.itemPath)
            }
        })

        viewModel.onDeleteClicked.observe(viewLifecycleOwner, { /*showDeleteDialog()*/ })

        dirsListViewModel.fileOrFolderCreation.observe(viewLifecycleOwner, { adapter.updateSource(it) })
    }

    private fun firstVisibleItemPosition(): Int = (rcv_dirs.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()

    private fun getSubDirs(path: String) = try {
        dirsListViewModel.getSubFiles(path).apply {
            if(this.isEmpty()) rcv_dirs.fadeOut() else {
                rcv_dirs.fadeIn()
                adapter.setSource(this)
            }
        }
    } catch (exception: VolumeManager.VolumeManagerException) {
        dirsListViewModel.currentPath = EMPTY_STRING
        activity?.supportFragmentManager?.popBackStack()
        viewModel.onItemClick.value = null
    }

    private fun onFabClick(fileCreationDialog: Boolean) = newFileDialog.also {
        if (fileCreationDialog) it.fileType() else it.folderType()
    }.show(childFragmentManager, newFileDialog.TAG)

    private fun showDeleteDialog() = deleteDialog.show(childFragmentManager, newFileDialog.TAG)

    fun onBackPressed() {
        if(adapter.checkedItemCount > DESTROY_SELECTION_ACTION_MODE)
            adapter.clearSelections()
        else
            getSubDirs(dirsListViewModel.removeLastPath())
    }

    fun selectAll() = adapter.selectAll()

    fun deselectAll() = adapter.clearSelections()

    override fun onClick(view: View?) = when(view?.id) {
        R.id.btn_fab -> onFabClick(true)
        else -> {}
    }

    override fun onItemClick(model: DirItem) {
        if (model is DirItem.Item)
            getSubDirs(model.itemPath)
    }

    override fun onCheckStateChange(models: List<DirItem>, checkedItemCount: Int, isSelectedAll: Boolean) {
        dirsListViewModel.onCheckStateChange(models, checkedItemCount)
        viewModel.onActionModeChange.value = Pair(checkedItemCount, isSelectedAll)
    }

    override fun onRenameClick(model: DirItem) {
    }

    override fun onEncryptClick(model: DirItem) {
    }

    override fun onDetailsClick(model: DirItem) {
    }
}