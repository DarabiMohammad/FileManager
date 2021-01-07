package com.darabi.mohammad.filemanager.ui

import android.os.Bundle
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.ui.fragment.base.BaseFragment
import com.darabi.mohammad.filemanager.util.fadeIn
import com.darabi.mohammad.filemanager.util.navigateTo
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import javax.inject.Inject

class MainActivity @Inject constructor() : BaseActivity() {

    override val layoutId: Int get() = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        navigateTo(fragment = homeFragment, isReplace = true)
    }

    override fun initStartupViews() {
        // initializing toolbar
        layout_toolbar.fadeIn()
        img_toggle.setOnClickListener(this)
        img_back.setOnClickListener(this)
        img_options.setOnClickListener(this)
        txt_toolbar_delete.setOnClickListener(this)
        txt_toolbar_share.setOnClickListener(this)
    }

    override fun observeViewModel() {
        super.observeViewModel()

        viewModel.volumeClickLiveData.observe(this, {
            doWithFragmentAndCloseDrawer(dirsListFragment) {
                dirsListFragment.getFilesForPath(it.path)
            }
        })

        viewModel.drawerCategoryFolderLiveData.observe(this, {
            doWithFragmentAndCloseDrawer(dirsListFragment) {
                dirsListFragment.getFilesForPath(it.path)
            }
        })

        viewModel.drawerCategoryLiveData.observe(this, {
            doWithFragmentAndCloseDrawer(dirsListFragment) {
                dirsListFragment.getFilesForCategory(it.type)
            }
        })

        viewModel.drawerInstalledAppsLiveData.observe(this, {
        })

        viewModel.drawerSettingsLiveData.observe(this, {
            navigateTo(fragment = settingsFragment, addToBackStack = true).also { closeNavDrawer() }
        })
    }

    private inline fun doWithFragmentAndCloseDrawer(fragment: BaseFragment, crossinline function: BaseFragment.() -> Unit): Unit =
        navigateTo(fragment = fragment, addToBackStack = true).also { fragment.function() }.also { closeNavDrawer() }
}