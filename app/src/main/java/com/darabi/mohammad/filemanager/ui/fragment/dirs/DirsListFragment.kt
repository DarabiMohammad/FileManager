package com.darabi.mohammad.filemanager.ui.fragment.dirs

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.ui.fragment.base.BaseFragment
import com.darabi.mohammad.filemanager.view.adapter.DirsRecyclerAdapter
import com.darabi.mohammad.filemanager.view.vh.dir.OnDirClickListener
import com.darabi.mohammad.filemanager.vm.BaseViewModel
import com.darabi.mohammad.filemanager.vm.DirsListViewModel
import com.darabi.mohammad.filemanager.vm.MainViewModel
import javax.inject.Inject

class DirsListFragment @Inject constructor(
    private val dirsListViewModel: DirsListViewModel,
    private val adapter: DirsRecyclerAdapter
) : BaseFragment(), OnDirClickListener {

    override val TAG: String get() = this.javaClass.simpleName
    override val layoutRes: Int get() = R.layout.fragment_dirs_list
    override val viewModel: MainViewModel by viewModels({ requireActivity() })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter.callback = this
        observeViewModel()
    }

    private fun observeViewModel() {
        val s = viewModel.onItemClicke.value
        val ss = viewModel.onItemClicke.value
    }

    override fun onMoreClick(position: Int) {
    }

    override fun onLongClick(positon: Int): Boolean {
        return true
    }

    override fun onClick(position: Int) {
    }
}