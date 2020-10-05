package com.darabi.mohammad.filemanager.ui.fragment

import android.os.Bundle
import android.view.View
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.ui.fragment.base.BaseFragment
import com.darabi.mohammad.filemanager.view.adapter.HomwRecyclerAdapter
import com.darabi.mohammad.filemanager.vm.BaseViewModel
import com.darabi.mohammad.filemanager.vm.HomeViewModel
import com.darabi.mohammad.filemanager.vm.MainViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import javax.inject.Inject

class HomeFragment @Inject constructor(
    private val howeViewModel: HomeViewModel,
    private val mainViewModel: MainViewModel,
    private val adapter: HomwRecyclerAdapter
) : BaseFragment() {

    override val TAG: String get() = this.javaClass.simpleName
    override val layoutRes: Int get() = R.layout.fragment_home
    override val viewModel: HomeViewModel get() = howeViewModel

    private val volumes by lazy { listOf(viewModel.getPrimaryExternalStorageVolume()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter.setSource(volumes)
        rcv_storage.adapter = adapter
        observeViewModel()
    }

    private fun observeViewModel() {

        mainViewModel.removableStorages.observe(viewLifecycleOwner, {
            adapter.addSource(it, adapter.itemCount)
        })
    }
}