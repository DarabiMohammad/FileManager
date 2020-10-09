package com.darabi.mohammad.filemanager.ui.fragment.dirs

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.model.DirItem
import com.darabi.mohammad.filemanager.ui.dialog.NewFileDialog
import com.darabi.mohammad.filemanager.ui.fragment.base.BaseFragment
import com.darabi.mohammad.filemanager.util.fadeOut
import com.darabi.mohammad.filemanager.view.adapter.DirsRecyclerAdapter
import com.darabi.mohammad.filemanager.view.adapter.checkable.BaseCheckableAdapter
import com.darabi.mohammad.filemanager.vm.DirsListViewModel
import com.darabi.mohammad.filemanager.vm.MainViewModel
import kotlinx.android.synthetic.main.fragment_dirs_list.*
import javax.inject.Inject

class DirsListFragment @Inject constructor(
    private val newFileDialog: NewFileDialog,
    private val dirsListViewModel: DirsListViewModel,
    private val adapter: DirsRecyclerAdapter
) : BaseFragment(R.layout.fragment_dirs_list), View.OnClickListener, BaseCheckableAdapter.CheckableAdapterCallback<DirItem> {

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

        viewModel.onItemClicke.observe(viewLifecycleOwner, {
            adapter.clear()
            dirsListViewModel.getSubFiles(it.itemPath).apply {
                if(this.isEmpty()) rcv_dirs.fadeOut() else adapter.setSource(this)
            }
        })

        dirsListViewModel.fileOrFolderCreation.observe(viewLifecycleOwner, { adapter.updateSource(it) })
    }

    private fun onFabClick() {
        val isFile = false
        newFileDialog.also {
            if (isFile) it.fileType() else it.folderType()
        }.show(childFragmentManager, newFileDialog.TAG)
    }

    override fun onClick(view: View?) {
        when(view?.id) {
            R.id.btn_fab -> onFabClick()
        }
    }

    override fun onItemClick(model: DirItem) {
        if(model is DirItem.Item)
            viewModel.onItemClicke.value = model
    }

    override fun onMoreOptionClick(model: DirItem) {
        Log.d("test", "=========onMoreOptionClick")
    }

    override fun onCheckStateChange(models: List<DirItem>, checkedItemCount: Int) {
    }
}