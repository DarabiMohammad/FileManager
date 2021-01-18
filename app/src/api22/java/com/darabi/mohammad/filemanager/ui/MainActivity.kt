package com.darabi.mohammad.filemanager.ui

import com.darabi.mohammad.filemanager.ui.fragment.base.BaseFragment
import com.darabi.mohammad.filemanager.util.navigateTo
import javax.inject.Inject

class MainActivity @Inject constructor() : BaseActivity() {

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
    }

    private inline fun doWithFragmentAndCloseDrawer(fragment: BaseFragment, crossinline function: BaseFragment.() -> Unit): Unit =
        navigateTo(fragment = fragment, addToBackStack = true).also { fragment.function() }.also { closeNavDrawer() }
}