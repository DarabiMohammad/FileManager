package com.darabi.mohammad.filemanager.ui.fragment.drawer

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.model.DrawerItem
import com.darabi.mohammad.filemanager.ui.fragment.base.BaseFragment
import com.darabi.mohammad.filemanager.view.adapter.DrawerRecyclerAdapter
import com.darabi.mohammad.filemanager.view.adapter.base.BaseAdapterCallback
import com.darabi.mohammad.filemanager.vm.DrawerViewModel
import com.darabi.mohammad.filemanager.vm.MainViewModel
import kotlinx.android.synthetic.main.fragment_drawer.*
import javax.inject.Inject

class DrawerFragment @Inject constructor(
    private val drawerViewModel: DrawerViewModel,
    private val adapter: DrawerRecyclerAdapter
) : BaseFragment(R.layout.fragment_drawer), BaseAdapterCallback<DrawerItem> {

    override val fragmentTag: String get() = this.javaClass.simpleName
    override val viewModel: MainViewModel by viewModels ({ requireActivity() })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter.apply {
            callback = this@DrawerFragment
            setSource(drawerViewModel.getDrawerItems())
        }
        rcv_nav_items.adapter = adapter
    }

    override fun onItemClick(item: DrawerItem) = viewModel.onDrawerItemClick(item)
}
//) : BaseFragment(R.layout.fragment_drawer), BaseAdapterCallback<DrawerItem.Item> {
//
//    override val fragmentTag: String get() = this.javaClass.simpleName
//    override val viewModel: MainViewModel by viewModels ({ requireActivity() })
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        adapter.apply {
//            callback = this@DrawerFragment
//            setSource(drawerViewModel.getStaticDrawerItems())
//        }
//        rcv_nav_items.adapter = adapter
//        observeAndSetItems()
//    }
//
//    private fun observeAndSetItems() {
//        viewModel.removableVolumes.observe(viewLifecycleOwner, {
////            adapter.addSource(it, FIRST_POSITION)
//        })
//    }
//
//    override fun onItemClick(item: DrawerItem.Item) {
//        viewModel.onItemClick.value = item
//    }
//}