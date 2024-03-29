package com.darabi.mohammad.filemanager.ui.fragment.home

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.model.Result
import com.darabi.mohammad.filemanager.model.Status
import com.darabi.mohammad.filemanager.model.StorageVolume
import com.darabi.mohammad.filemanager.ui.fragment.base.BaseFragment
import com.darabi.mohammad.filemanager.view.adapter.home.HomeRecyclerAdapter
import com.darabi.mohammad.filemanager.view.adapter.base.OnItemClickListener
import com.darabi.mohammad.filemanager.vm.HomeViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import javax.inject.Inject

class HomeFragment @Inject constructor(
    private val homeViewModel: HomeViewModel,
    private val adapter: HomeRecyclerAdapter
) : BaseFragment(R.layout.fragment_home), OnItemClickListener<StorageVolume>,
    Observer<Result<ArrayList<StorageVolume>>> {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rcv_storage.adapter = adapter.apply { callback = this@HomeFragment }
        homeViewModel.getVolumes().observe(viewLifecycleOwner, this)
    }

    override fun onItemClick(item: StorageVolume) {
        mainViewModel.volumeClickLiveData.setValueOrNull(item)
        mainViewModel.updateToobarTitle.value = item.name
    }

    override fun onChanged(response: Result<ArrayList<StorageVolume>>?) {
        response?.let {
            when (it.status) {
                Status.LOADING -> {}
                Status.SUCCESS -> adapter.setSource(it.result!!)
                Status.ERROR -> throw it.throwable!! //onError(it.throwable!!)
            }
        }
    }

    private fun onError(throwable: Throwable) {}
}