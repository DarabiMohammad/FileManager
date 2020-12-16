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

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
    }

    fun getPerimaryStorageFiles() = dirsListViewModel.getPrimaryStorageRootFiles()

    fun getSecondaryStorageFiles() = dirsListViewModel.getSecondaryStorageRootFiles()

    fun getFilesForCategory(categoryType: CategoryType) = dirsListViewModel.getFilesForCategory(categoryType)

    private fun initViews() {
        btn_fab.setOnClickListener(this)
        if(rcv_dirs.adapter == null) rcv_dirs.adapter = adapter.also { it.adapterCallback = this@DirsListFragment }
    }

    private fun observeViewModel() {

        dirsListViewModel.filesLiveData.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.LOADING -> {}
                Status.SUCCESS -> if(it.result!!.isNotEmpty()) adapter.setSource(it.result).also { rcv_dirs.fadeIn() } else rcv_dirs.fadeOut()
                Status.ERROR -> super.onBackPressed()
            }
        })
    }
}