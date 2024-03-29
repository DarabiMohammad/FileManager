package com.darabi.mohammad.filemanager.ui.fragment.drawer

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.model.*
import com.darabi.mohammad.filemanager.ui.fragment.base.BaseFragment
import com.darabi.mohammad.filemanager.view.adapter.drawer.DrawerRecyclerAdapter
import com.darabi.mohammad.filemanager.view.adapter.base.OnItemClickListener
import com.darabi.mohammad.filemanager.vm.DrawerViewModel
import kotlinx.android.synthetic.main.fragment_drawer.*
import javax.inject.Inject

class DrawerFragment @Inject constructor(
    private val drawerViewModel: DrawerViewModel,
    private val adapter: DrawerRecyclerAdapter
) : BaseFragment(R.layout.fragment_drawer), OnItemClickListener<BaseDrawerItem>,
    Observer<Result<ArrayList<StorageVolume>>> {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rcv_nav_items.adapter = adapter.apply { callback = this@DrawerFragment }
        drawerViewModel.getDrawerItems().observe(viewLifecycleOwner, this)
    }

    override fun onChanged(response: Result<ArrayList<StorageVolume>>?) {
        response?.let {
            when (it.status) {
                Status.LOADING -> {}
                Status.SUCCESS -> adapter.setSource(drawerViewModel.getStaticItems().apply { addAll(0, drawerViewModel.mapToDrawerItems(it.result!!)) })
                Status.ERROR -> onError(it.throwable!!)
            }
        }
    }

    override fun onItemClick(item: BaseDrawerItem) = (item as DrawerItem).run {
        mainViewModel.onDrawerItemClick(item).also { mainViewModel.updateToobarTitle.value = item.name }
    }

    private fun onError(throwable: Throwable): Unit = throw throwable
}