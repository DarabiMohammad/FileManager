package com.darabi.mohammad.filemanager.ui.fragment

import android.os.Bundle
import android.view.View
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.ui.BaseFragment
import com.darabi.mohammad.filemanager.util.navigateTo
import com.darabi.mohammad.filemanager.view.DrawerItem
import com.darabi.mohammad.filemanager.view.adapter.DrawerRecyclerAdapter
import com.darabi.mohammad.filemanager.view.vh.drawer.OnDrawerItemClickListener
import kotlinx.android.synthetic.main.fragment_drawer.*
import javax.inject.Inject

class DrawerFragment @Inject constructor(
    private val adapter: DrawerRecyclerAdapter,
    private val settingsFragment: SettingsFragment,
    private val appManagerFragment: AppManagerFragment
) : BaseFragment(), OnDrawerItemClickListener {

    override val layoutRes: Int get() = R.layout.fragment_drawer

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter.callback = this
        adapter.setSource(getItems())
        rcv_nav_items.adapter = adapter
    }

    private fun getItems(): ArrayList<DrawerItem> = arrayListOf(
        DrawerItem.Item(getString(R.string.dcim), R.drawable.ic_settings_black),
        DrawerItem.Item(getString(R.string.download), R.drawable.ic_settings_black),
        DrawerItem.Item(getString(R.string.movies), R.drawable.ic_settings_black),
        DrawerItem.Item(getString(R.string.musics), R.drawable.ic_settings_black),
        DrawerItem.Item(getString(R.string.pictures), R.drawable.ic_settings_black),
        DrawerItem.Item(getString(R.string.quick_access), R.drawable.ic_settings_black),
        DrawerItem.Divider,
        DrawerItem.Item(getString(R.string.recent_files), R.drawable.ic_settings_black),
        DrawerItem.Item(getString(R.string.images), R.drawable.ic_settings_black),
        DrawerItem.Item(getString(R.string.videos), R.drawable.ic_settings_black),
        DrawerItem.Item(getString(R.string.audio), R.drawable.ic_settings_black),
        DrawerItem.Item(getString(R.string.documents), R.drawable.ic_settings_black),
        DrawerItem.Item(getString(R.string.apks), R.drawable.ic_settings_black),
        DrawerItem.Divider,
        DrawerItem.Item(getString(R.string.app_manager), R.drawable.ic_settings_black),
        DrawerItem.Item(getString(R.string.settings), R.drawable.ic_settings_black)
    )

    private fun navigateTo(fragment: BaseFragment, addToBackstack: Boolean = true) =
        activity!!.navigateTo(fragment = fragment, addToBackstack = addToBackstack)

    override fun onDrawerItemClick(title: String) {
        when(title) {
            getString(R.string.settings) -> navigateTo(settingsFragment)
            getString(R.string.app_manager) -> navigateTo(appManagerFragment)
            else -> {}
        }
        viewModel.onDrawerItemClick.value = title
    }
}