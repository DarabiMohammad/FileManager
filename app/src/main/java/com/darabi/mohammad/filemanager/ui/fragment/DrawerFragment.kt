package com.darabi.mohammad.filemanager.ui.fragment

import android.os.Bundle
import android.view.View
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.ui.BaseFragment
import com.darabi.mohammad.filemanager.view.DrawerItem
import com.darabi.mohammad.filemanager.view.adapter.DrawerRecyclerAdapter
import kotlinx.android.synthetic.main.fragment_drawer.*
import kotlinx.android.synthetic.main.rcv_item_nav_drawer.*
import javax.inject.Inject

class DrawerFragment @Inject constructor(
    private val adapter: DrawerRecyclerAdapter
) : BaseFragment() {

    override val layoutRes: Int get() = R.layout.fragment_drawer

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val items = arrayListOf(
            DrawerItem.Divider,
            DrawerItem.Item("Settings", R.drawable.ic_settings_black)
        )

        adapter.setSource(items)
        rcv_nav_items.adapter = adapter
    }
}