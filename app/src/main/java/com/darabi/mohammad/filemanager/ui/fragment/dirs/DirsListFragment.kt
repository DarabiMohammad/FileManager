package com.darabi.mohammad.filemanager.ui.fragment.dirs

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.model.DirItem
import com.darabi.mohammad.filemanager.ui.fragment.base.BaseFragment
import com.darabi.mohammad.filemanager.util.navigateTo
import com.darabi.mohammad.filemanager.util.storage.VolumeManager
import com.darabi.mohammad.filemanager.view.adapter.DirsRecyclerAdapter
import com.darabi.mohammad.filemanager.view.vh.dir.OnDirClickListener
import com.darabi.mohammad.filemanager.vm.DirsListViewModel
import com.darabi.mohammad.filemanager.vm.MainViewModel
import kotlinx.android.synthetic.main.fragment_dirs_list.*
import javax.inject.Inject

class DirsListFragment @Inject constructor(
    private val dirsListViewModel: DirsListViewModel,
    private val adapter: DirsRecyclerAdapter
) : BaseFragment(), OnDirClickListener {

    override val TAG: String get() = this.javaClass.simpleName
    override val layoutRes: Int get() = R.layout.fragment_dirs_list
    override val viewModel: MainViewModel by viewModels( { requireActivity() } )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter.callback = this
        rcv_dirs.adapter = adapter
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.onItemClicke.observe(viewLifecycleOwner, {
            adapter.clear()
            adapter.setSource(dirsListViewModel.getSubFiles(it.itemPath))
        })
    }

    override fun onMoreClick(item: DirItem.Item) {

    }

    override fun onLongClick(item: DirItem.Item): Boolean {
        return true
    }

    fun getInstance() = DirsListFragment(dirsListViewModel, adapter)

    override fun onClick(item: DirItem.Item) {
//        navigateTo(R.id.container_home, getInstance(), addToBackstack = true)
        viewModel.onItemClicke.value = item
    }
}