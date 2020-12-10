package com.darabi.mohammad.filemanager.ui

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.CompoundButton
import androidx.activity.viewModels
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.ui.dialog.PermissionDescriptionDialog
import com.darabi.mohammad.filemanager.ui.fragment.AppManagerFragment
import com.darabi.mohammad.filemanager.ui.fragment.base.BaseFragment
import com.darabi.mohammad.filemanager.ui.fragment.dirs.DirsListFragment
import com.darabi.mohammad.filemanager.ui.fragment.home.HomeFragment
import com.darabi.mohammad.filemanager.ui.fragment.settings.SettingsFragment
import com.darabi.mohammad.filemanager.util.PermissionManager
import com.darabi.mohammad.filemanager.util.fadeIn
import com.darabi.mohammad.filemanager.util.fadeOut
import com.darabi.mohammad.filemanager.util.navigateTo
import com.darabi.mohammad.filemanager.view.adapter.checkable.BaseCheckableAdapter.Companion.DESTROY_SELECTION_ACTION_MODE
import com.darabi.mohammad.filemanager.vm.MainViewModel
import com.darabi.mohammad.filemanager.vm.ViewModelFactory
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import javax.inject.Inject

class MainActivity @Inject constructor() : BaseActivity(), HasAndroidInjector,
    View.OnClickListener, PermissionManager.PermissionManagerCallback,
    CompoundButton.OnCheckedChangeListener, PopupMenu.OnMenuItemClickListener {

    @Inject
    internal lateinit var injector: DispatchingAndroidInjector<Any>

    @Inject
    internal lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: MainViewModel by viewModels { viewModelFactory }

    @Inject
    internal lateinit var fragmentFactory: InjectingFragmentFactory

    @Inject
    internal lateinit var homeFragment: HomeFragment

    @Inject
    internal lateinit var dirsListFragment: DirsListFragment

    @Inject
    internal lateinit var appManagerFragment: AppManagerFragment

    @Inject
    internal lateinit var settingsFragment: SettingsFragment

    @Inject
    internal lateinit var permissionDescDialog: PermissionDescriptionDialog

    @Inject
    internal lateinit var permissionManager: PermissionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        supportFragmentManager.fragmentFactory = fragmentFactory
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
        observeViewModel()
        navigateTo(fragment = homeFragment, isReplace = true)
    }

    override fun androidInjector(): AndroidInjector<Any> = injector

    override fun onClick(view: View?) = when(view?.id) {
        R.id.img_toggle -> openNavDrawer()
        R.id.img_back -> onBackPressed()
        R.id.img_options -> onOptionsClick()
        R.id.txt_toolbar_delete -> delete()
        R.id.txt_toolbar_share -> {}
        else -> {}
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        viewModel.onSelectAllClick.value = isChecked
    }

    override fun onBackPressed() {
        if(layout_drawer.isDrawerOpen(GravityCompat.START))
            closeNavDrawer()
        else
            (supportFragmentManager.fragments.last().takeIf { it is BaseFragment } as BaseFragment?)
                    ?.onBackPressed() ?: super.onBackPressed()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) =
            if(permissionManager.isPermissionsGrant(grantResults))
                navigateTo(fragment = dirsListFragment, addToBackStack = true)
            else super.onRequestPermissionsResult(requestCode, permissions, grantResults)

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == OPEN_APP_INFO_CODE)
            permissionManager.checkPermissionsAndRun(this, this, PermissionManager.Permissions.Storage) {
                navigateTo(fragment = dirsListFragment, addToBackStack = true)
            }
    }

    override fun onFirstAskPermission(permissionGroup: PermissionManager.Permissions) =
            permissionDescDialog.show(supportFragmentManager, permissionDescDialog.dialogTAG)

    override fun onPermissionDenied(permissionGroup: PermissionManager.Permissions) =
            permissionDescDialog.detailedDialog().show(supportFragmentManager, permissionDescDialog.dialogTAG)

    override fun onPermissionWasDeniedForever(permissionGroup: PermissionManager.Permissions) =
            permissionDescDialog.finalDialog().show(supportFragmentManager, permissionDescDialog.dialogTAG)

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.add_short_cut -> {}
            R.id.hide -> {}
            else -> {}
        }
        return true
    }

    private fun initView() {

        // initializing toolbar
        layout_toolbar.fadeIn()
        img_toggle.setOnClickListener(this)
        img_back.setOnClickListener(this)
        img_options.setOnClickListener(this)
        txt_toolbar_delete.setOnClickListener(this)
        txt_toolbar_share.setOnClickListener(this)
    }

    private fun observeViewModel() {

        viewModel.primaryStorageLiveData.observe(this, {
            checkPermissionAndRun {
                navigateTo(fragment = dirsListFragment, addToBackStack = true).also { dirsListFragment.getPerimaryStorageFiles() }
            }
        })

        viewModel.secondaryStorageLiveData.observe(this, {
            checkPermissionAndRun {
                navigateTo(fragment = dirsListFragment, addToBackStack = true).also { dirsListFragment.getSecondaryStorageFiles() }
            }
        })

        viewModel.drawerPrimaryStorageLiveData.observe(this, {

        })

        viewModel.drawerSecondaryStorageLiveData.observe(this, {

        })

        viewModel.drawerCategoryLiveData.observe(this, {

        })

        viewModel.drawerInstalledAppsLiveData.observe(this, {

        })

        viewModel.drawerSettingsLiveData.observe(this, {

        })

        viewModel.permissionDialoLiveData.observe(this, {
            when(it) {
                PermissionDescriptionDialog.DialogAction.ACTION_OK ->
                    permissionManager.requestPermissions(PermissionManager.Permissions.Storage, this)
                PermissionDescriptionDialog.DialogAction.ACTION_OPEN_SETTINGS -> openAppInfoScreen()
                else -> closeApp()
            }
        })

        viewModel.onActionModeChange.observe(this, {
            if(it.first > DESTROY_SELECTION_ACTION_MODE)
                showSelectionActionMode(it.first, it.second)
            else hideSelectionActionMode()
        })
    }

    private inline fun checkPermissionAndRun(crossinline function: () -> Unit) =
        permissionManager.checkPermissionsAndRun(this, this, PermissionManager.Permissions.Storage) {
            function.invoke()
        }

    private fun showSelectionActionMode(checkedItemCount: Int, isSelectedAll: Boolean) {
        img_toggle.fadeOut()
        chb_select_all.fadeIn()
        chb_select_all.setOnCheckedChangeListener(null)
        chb_select_all.isChecked = isSelectedAll
        chb_select_all.setOnCheckedChangeListener(this)
        txt_toolbar_title.text = checkedItemCount.toString()
        lockNavDrawer()
        container_more_options.fadeIn()
    }

    private fun hideSelectionActionMode() {
        img_toggle.fadeIn()
        chb_select_all.fadeOut()
        container_more_options.fadeOut()
        container_more_options.fadeOut()
        unlockNavDrawer()
//        txt_toolbar_title.text = viewModel.onItemClick.value?.itemName
    }

    private fun showBackButton() {
        img_toggle.fadeOut()
        img_back.fadeIn()
        lockNavDrawer()
    }

    private fun hideBackButton() {
        img_toggle.fadeIn()
        img_back.fadeOut()
        unlockNavDrawer()
    }

//    private fun performOnItemClick(item: BaseItem?) = when(item?.itemType) {
//        ItemType.DRAWER_ITEM_OTHER -> onOtherDrawerItemClick(item)
//        ItemType.DRAWER_ITEM_CATEGORY -> onDrawerCategoryItemClick(item)
//        ItemType.LIST_FOLDER_ITEM -> onDirectoryClick(item)
//        null -> supportFragmentManager.popBackStack()
//        else -> {}
//    }
//
//    private fun onOtherDrawerItemClick(item: BaseItem?) {
//        showBackButton()
//        val destinationFragment = if (item!!.itemName == getString(R.string.settings)) settingsFragment else appManagerFragment
//        txt_toolbar_title.text = item.itemName
//        navigateTo(fragment = destinationFragment, addToBackStack = true)
//        closeNavDrawer()
//    }
//
//    private fun onDrawerCategoryItemClick(item: BaseItem?) {
//        closeNavDrawer()
//        permissionManager.checkPermissionsAndRun(this, this, PermissionManager.Permissions.Storage) {
//            navigateToDirsFragment(item!!.itemName)
//        }
//    }
//
//    private fun onDirectoryClick(item: BaseItem?) =
//        permissionManager.checkPermissionsAndRun(this, this, PermissionManager.Permissions.Storage) {
//            val toolbarTitle = if(item!!.itemName == "0") getString(R.string.internal_storage) else item.itemName
//            navigateToDirsFragment(toolbarTitle)
//        }

    private fun navigateToDirsFragment(dirName: String) {
        txt_toolbar_title.text = dirName
        navigateTo(fragment = dirsListFragment, addToBackStack = true)
    }

    private fun onOptionsClick() {
        val popupMenu = PopupMenu(this, img_options, GravityCompat.END)
        popupMenu.setOnMenuItemClickListener(this)
        popupMenu.inflate(R.menu.menu_selected_items)
        popupMenu.show()
    }

    private fun openNavDrawer() = layout_drawer.openDrawer(GravityCompat.START)

    private fun closeNavDrawer() = layout_drawer.closeDrawer(GravityCompat.START)

    private fun lockNavDrawer() = layout_drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

    private fun unlockNavDrawer() = layout_drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)

    private fun delete() { viewModel.onDeleteClicked.value = true }

    fun onFragmentBackPressed() {
        supportFragmentManager.fragments.last().also {
            if(it is SettingsFragment) hideBackButton()
        }
        supportFragmentManager.popBackStack()
    }
}