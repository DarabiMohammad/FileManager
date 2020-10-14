package com.darabi.mohammad.filemanager.ui.fragment.dirs

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.model.DirItem
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

class DirsListFragment @Inject constructor (
    private val newFileDialog: NewFileDialog,
    private val deleteDialog: DeleteDialog,
    private val dirsListViewModel: DirsListViewModel,
    private val adapter: DirsRecyclerAdapter
) : BaseFragment(R.layout.fragment_dirs_list), View.OnClickListener,
    BaseCheckableAdapter.CheckableAdapterCallback<DirItem> {

    override val TAG: String get() = this.javaClass.simpleName
    override val viewModel: MainViewModel by viewModels( { requireActivity() } )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()
        initViews()
    }

    private fun initViews() {
        btn_fab.setOnClickListener(this)
        adapter.adapterCallback = this
        rcv_dirs.adapter = adapter
    }

    private fun observeViewModel() {

        viewModel.onItemClicke.observe(viewLifecycleOwner, { getSubDirs(it.itemPath) })

        viewModel.onDeleteClicked.observe(viewLifecycleOwner, { /*showDeleteDialog()*/ })

        dirsListViewModel.fileOrFolderCreation.observe(viewLifecycleOwner, { adapter.updateSource(it) })
    }

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

    override fun onMoreOptionClick(model: DirItem) {
        Log.d("test", "=========onMoreOptionClick")
    }

    override fun onCheckStateChange(models: List<DirItem>, checkedItemCount: Int, isSelectedAll: Boolean) {
        dirsListViewModel.onCheckStateChange(models, checkedItemCount)
        viewModel.onActionModeChange.value = Pair(checkedItemCount, isSelectedAll)
    }
}