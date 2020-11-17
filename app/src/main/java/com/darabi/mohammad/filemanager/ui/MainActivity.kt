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
import com.darabi.mohammad.filemanager.model.ItemType
import com.darabi.mohammad.filemanager.ui.dialog.PermissionDescriptionDialog
import com.darabi.mohammad.filemanager.ui.fragment.AppManagerFragment
import com.darabi.mohammad.filemanager.ui.fragment.base.BaseFragment
import com.darabi.mohammad.filemanager.ui.fragment.settings.SettingsFragment
import com.darabi.mohammad.filemanager.ui.fragment.dirs.DirsListFragment
import com.darabi.mohammad.filemanager.ui.fragment.home.HomeFragment
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
    View.OnClickListener, PermissionManager.PermissionCallback,
    CompoundButton.OnCheckedChangeListener, PopupMenu.OnMenuItemClickListener {

    private companion object {
        const val TOOLBAR_TITLE = "toolbar_title"
    }

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
    internal lateinit var dialogPermissionDescription: PermissionDescriptionDialog

    @Inject
    internal lateinit var permissionManager: PermissionManager

    override fun saveUiState(uiState: Bundle) {
        uiState.putString(TOOLBAR_TITLE, txt_toolbar_title.text.toString())
    }

    override fun retrieveUiState(uiState: Bundle) {
        txt_toolbar_title.text = uiState.getString(TOOLBAR_TITLE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        supportFragmentManager.fragmentFactory = fragmentFactory
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
        observeViewModel()
        navigateTo(fragment = homeFragment, isReplace = true)
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
        txt_toolbar_title.text = viewModel.onItemClick.value?.itemName
    }

    private fun showBackButton() {
        img_toggle.fadeOut()
        img_back.fadeIn()
    }

    private fun hideBackButton() {
        img_toggle.fadeIn()
        img_back.fadeOut()
    }

    private fun observeViewModel() {
        viewModel.onItemClick.observe(this, { performOnItemClick() })

        viewModel.onPermissionDialogDescButtonClick.observe(this, {
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

    private fun performOnItemClick() = when(viewModel.onItemClick.value?.itemType) {
        ItemType.DRAWER_ITEM_OTHER -> onOtherDrawerItemClick()
        ItemType.DRAWER_ITEM_CATEGORY -> onDrawerCategoryItemClick()
        ItemType.LIST_FOLDER_ITEM -> onDirectoryClick()
        else -> {}
    }

    private fun onOtherDrawerItemClick() {
        showBackButton()
        val drawerItemName = viewModel.onItemClick.value!!.itemName
        val destinationFragment = if (drawerItemName == getString(R.string.settings)) settingsFragment else appManagerFragment
        txt_toolbar_title.text = drawerItemName
        navigateTo(fragment = destinationFragment, addToBackStack = true)
        closeNavDrawer()
    }

    private fun onDrawerCategoryItemClick() {
        closeNavDrawer()
        permissionManager.checkPermissionsAndRun(this, this, PermissionManager.Permissions.Storage)
    }

    private fun onDirectoryClick() =
        permissionManager.checkPermissionsAndRun(this, this, PermissionManager.Permissions.Storage)

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

    override fun androidInjector(): AndroidInjector<Any> = injector

    override fun onClick(view: View?) = when(view?.id) {
            R.id.img_toggle -> openNavDrawer()
            R.id.img_back -> onBackPressed()
            R.id.img_options -> onOptionsClick()
            R.id.txt_toolbar_delete -> delete()
            R.id.txt_toolbar_share -> {}
            else -> {}
        }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) =
        if(isChecked) dirsListFragment.selectAll() else dirsListFragment.deselectAll()

    override fun onBackPressed() {
        if(layout_drawer.isDrawerOpen(GravityCompat.START))
            closeNavDrawer()
        else
            (supportFragmentManager.fragments.last().takeIf { it is BaseFragment } as BaseFragment?)
                    ?.onBackPressed() ?: super.onBackPressed()
    }

    override fun onFirstAskPermission(permissionGroup: PermissionManager.Permissions) =
        dialogPermissionDescription.show(supportFragmentManager, dialogPermissionDescription.TAG)

    override fun onPermissionGranted(permissionGroup: PermissionManager.Permissions) {
        if(permissionGroup is PermissionManager.Permissions.Storage)
            navigateTo(fragment = dirsListFragment, addToBackStack = true)
    }

    override fun onPermissionDenied(permissionGroup: PermissionManager.Permissions) =
        dialogPermissionDescription.detailedDialog().show(supportFragmentManager, dialogPermissionDescription.TAG)

    override fun onPermissionWasDeniedForever(permissionGroup: PermissionManager.Permissions) =
        dialogPermissionDescription.finalDialog().show(supportFragmentManager, dialogPermissionDescription.TAG)

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) =
        if(permissionManager.isPermissionsGrant(grantResults))
            navigateTo(fragment = dirsListFragment, addToBackStack = true)
        else super.onRequestPermissionsResult(requestCode, permissions, grantResults)

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == OPEN_APP_INFO_CODE)
            permissionManager.checkPermissionsAndRun(this, this, PermissionManager.Permissions.Storage)
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.add_short_cut -> {}
            R.id.hide -> {}
            else -> {}
        }
        return true
    }
}