package com.darabi.mohammad.filemanager.ui.fragment.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.model.DirItem
import com.darabi.mohammad.filemanager.ui.fragment.base.BaseFragment
import com.darabi.mohammad.filemanager.view.adapter.HomwRecyclerAdapter
import com.darabi.mohammad.filemanager.view.vh.home.OnHoneVolumeItemsClickListener
import com.darabi.mohammad.filemanager.vm.HomeViewModel
import com.darabi.mohammad.filemanager.vm.MainViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import javax.inject.Inject

class HomeFragment @Inject constructor(
    internal val howeViewModel: HomeViewModel,
    private val adapter: HomwRecyclerAdapter
) : BaseFragment(R.layout.fragment_home), OnHoneVolumeItemsClickListener {

    override val TAG: String get() = this.javaClass.simpleName
    override val viewModel: MainViewModel by viewModels ({ requireActivity() })

    private val volumes by lazy {
        listOf(howeViewModel.convertToDirItem(howeViewModel.getPrimaryExternalStorageVolume()))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter.apply {
            callback = this@HomeFragment
            setSource(volumes)
        }
        rcv_storage.adapter = adapter
        observeViewModel()
    }

    private fun observeViewModel() {

        viewModel.removableStorages.observe(viewLifecycleOwner, {
//            adapter.addSource(it, adapter.itemCount)
        })
    }

    override fun onVolumeClick(volume: DirItem.Item) {
        viewModel.onItemClicke.value = volume
    }
}