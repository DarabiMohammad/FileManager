package com.darabi.mohammad.filemanager.ui.fragment.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.model.StorageItem
import com.darabi.mohammad.filemanager.ui.fragment.base.BaseFragment
import com.darabi.mohammad.filemanager.view.adapter.HomeRecyclerAdapter
import com.darabi.mohammad.filemanager.view.adapter.base.BaseAdapterCallback
import com.darabi.mohammad.filemanager.vm.HomeViewModel
import com.darabi.mohammad.filemanager.vm.MainViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import javax.inject.Inject

class HomeFragment @Inject constructor(
        private val howeViewModel: HomeViewModel,
        private val adapter: HomeRecyclerAdapter
) : BaseFragment(R.layout.fragment_home), BaseAdapterCallback<StorageItem> {

    override val fragmentTag: String get() = this.javaClass.simpleName
    override val viewModel: MainViewModel by viewModels ({ requireActivity() })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter.apply {
            callback = this@HomeFragment
            setSource(howeViewModel.getStorages())
        }
        rcv_storage.adapter = adapter
    }

    override fun onItemClick(item: StorageItem) = viewModel.onStorageClick(item)
}