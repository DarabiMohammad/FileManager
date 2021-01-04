package com.darabi.mohammad.filemanager.ui.fragment.drawer

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.model.DrawerItem
import com.darabi.mohammad.filemanager.model.Status
import com.darabi.mohammad.filemanager.ui.fragment.base.BaseFragment
import com.darabi.mohammad.filemanager.view.adapter.DrawerRecyclerAdapter
import com.darabi.mohammad.filemanager.view.adapter.base.BaseAdapterCallback
import com.darabi.mohammad.filemanager.vm.DrawerViewModel
import com.darabi.mohammad.filemanager.vm.base.AbstractMainViewModel
import com.darabi.mohammad.filemanager.vm.base.MainViewModel
import kotlinx.android.synthetic.main.fragment_drawer.*
import javax.inject.Inject

class DrawerFragment @Inject constructor(
    private val drawerViewModel: DrawerViewModel,
    private val adapter: DrawerRecyclerAdapter
) : BaseFragment(R.layout.fragment_drawer), BaseAdapterCallback<DrawerItem> {

    override val fragmentTag: String get() = this.javaClass.simpleName
    override val viewModel: MainViewModel by viewModels( { requireActivity() } )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rcv_nav_items.adapter = adapter.apply {
            callback = this@DrawerFragment
            setSource(drawerViewModel.getStaticItems())
        }
        observeViewModel()
        drawerViewModel.getDrawerItems()
    }

    override fun onItemClick(item: DrawerItem) = viewModel.onDrawerItemClick(item)

    private fun observeViewModel() {
        drawerViewModel.drawerItems.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.LOADING -> {}
                Status.SUCCESS -> {
                    adapter.addSource(drawerViewModel.mapToDrawerItems(it.result!!), 0)
                }
                Status.ERROR -> onError(it.throwable!!)
            }
        })
    }

    private fun onError(throwable: Throwable) {}
}