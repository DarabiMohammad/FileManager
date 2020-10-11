package com.darabi.mohammad.filemanager.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.model.BaseItem
import com.darabi.mohammad.filemanager.model.ItemType
import com.darabi.mohammad.filemanager.ui.dialog.NewFileDialog
import com.darabi.mohammad.filemanager.ui.dialog.PermissionDescriptionDialog
import com.darabi.mohammad.filemanager.ui.fragment.AppManagerFragment
import com.darabi.mohammad.filemanager.ui.fragment.SettingsFragment
import com.darabi.mohammad.filemanager.ui.fragment.base.BaseFragment
import com.darabi.mohammad.filemanager.ui.fragment.dirs.DirsListFragment
import com.darabi.mohammad.filemanager.ui.fragment.home.HomeFragment
import com.darabi.mohammad.filemanager.util.PermissionManager
import com.darabi.mohammad.filemanager.util.fadeIn
import com.darabi.mohammad.filemanager.util.fadeOut
import com.darabi.mohammad.filemanager.util.navigateTo
import com.darabi.mohammad.filemanager.view.adapter.checkable.BaseCheckableAdapter.Companion.SELECTION_ACTION_MODE_DESTROYED
import com.darabi.mohammad.filemanager.vm.MainViewModel
import com.darabi.mohammad.filemanager.vm.ViewModelFactory
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import kotlinx.android.synthetic.main.layout_toolbar.view.*
import javax.inject.Inject
import kotlin.system.exitProcess

class MainActivity @Inject constructor() : AppCompatActivity(), HasAndroidInjector,
    View.OnClickListener, PermissionManager.PermissionCallback {

    @Inject
    lateinit var injector: DispatchingAndroidInjector<Any>

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: MainViewModel by viewModels { viewModelFactory }

    @Inject
    lateinit var fragmentFactory: InjectingFragmentFactory

    @Inject
    lateinit var homeFragment: HomeFragment

    @Inject
    lateinit var dirsListFragment: DirsListFragment

    @Inject
    lateinit var appManagerFragment: AppManagerFragment

    @Inject
    lateinit var settingsFragment: SettingsFragment

    @Inject
    lateinit var dialogPermissionDescription: PermissionDescriptionDialog

    @Inject
    lateinit var permissionManager: PermissionManager

    companion object {
        const val OPEN_APP_INFO_CODE = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        supportFragmentManager.fragmentFactory = fragmentFactory
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()

        observeViewModel()
    }

    override fun onStart() {
        super.onStart()

        navigateTo(fragment = homeFragment, isReplace = true)
    }

    private fun initView() {

        // initializing toolbar
        layout_toolbar.visibility = View.VISIBLE
        layout_toolbar.img_toggle.setOnClickListener(this)
    }

    private fun showSelectionActionMode(checkedItemCount: Int) {
        img_toggle.fadeOut()
        chb_select_all.fadeIn()
        container_more_options.fadeIn()
        txt_toolbar_title.text = checkedItemCount.toString()
    }

    private fun hideSelectionActionMode() {
        img_toggle.fadeIn()
        chb_select_all.fadeOut()
        container_more_options.fadeOut()
        txt_toolbar_title.text = viewModel.onItemClicke.value?.itemName
    }

    private fun observeViewModel() {

        viewModel.onItemClicke.observe(this, {
            //todo wrong flow for checking storage permission. we don't need permission when user taps on settings item in drawer view
            if(it.itemType == ItemType.DRAWER_ITEM) closeNavDrawer()
            permissionManager.checkPermissionsAndRun(this, this, PermissionManager.Permissions.Storage)
        })

        viewModel.onPermissionDIalogDescButtonClick.observe(this, {
            when(it) {
                PermissionDescriptionDialog.DialogAction.ACTION_OK ->
                    permissionManager.requestPermissions(PermissionManager.Permissions.Storage, this)
                PermissionDescriptionDialog.DialogAction.ACTION_OPEN_SETTINGS -> openAppInfoScreen()
                else -> closeApp()
            }
        })

        viewModel.onActionModeChange.observe(this, {
            if(it > 0)
                showSelectionActionMode(it)
            else hideSelectionActionMode()
        })
    }

    private fun performOnItemClick() {
        when(viewModel.onItemClicke.value!!.itemType) {
            ItemType.DRAWER_ITEM -> onDrawerItemClick()
            ItemType.LIST_FOLDER_ITEM -> onDirectoryClick()
            else -> {}
        }
    }

    private fun onDrawerItemClick() {
        val drawerItemName = viewModel.onItemClicke.value!!.itemName
        layout_toolbar.txt_toolbar_title.text = drawerItemName
        val destinationFragment: BaseFragment = when(drawerItemName) {
            getString(R.string.settings) -> settingsFragment
            getString(R.string.app_manager) -> appManagerFragment
            else -> dirsListFragment
        }
        navigateTo(fragment = destinationFragment, addToBackstack = true)
        layout_drawer.closeDrawer(GravityCompat.START)
    }

    private fun onDirectoryClick() = navigateTo(fragment = dirsListFragment, addToBackstack = true)

    private fun openNavDrawer() = layout_drawer.openDrawer(GravityCompat.START)

    private fun closeNavDrawer() = layout_drawer.closeDrawer(GravityCompat.START)

    private fun closeApp() {
        finish()
        exitProcess(0)
    }

    private fun openAppInfoScreen() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        intent.data = Uri.parse("package:$packageName")
        startActivityForResult(intent, MainActivity.OPEN_APP_INFO_CODE)
    }

    override fun androidInjector(): AndroidInjector<Any> = injector

    override fun onClick(view: View?) =
        when(view?.id) {
            R.id.img_toggle -> openNavDrawer()
            else -> {}
        }

    override fun onBackPressed() {
        viewModel.onActionModeChange.value?.let {
            if(it > 0) {
                viewModel.onActionModeChange.value = SELECTION_ACTION_MODE_DESTROYED
                return@onBackPressed
            }
        }
        if(layout_drawer.isDrawerOpen(GravityCompat.START))
            closeNavDrawer()
        else {
            supportFragmentManager.fragments.last().also {
                when(it) {
                    is DirsListFragment -> dirsListFragment.onBackPressed()
                    else -> super.onBackPressed()
                }
            }
        }
    }

    override fun onFirstAskPermission(permissionGroup: PermissionManager.Permissions) {
        dialogPermissionDescription.show(supportFragmentManager, dialogPermissionDescription.TAG)
    }

    override fun onPermissionGranted(permissionGroup: PermissionManager.Permissions) {
        if(permissionGroup is PermissionManager.Permissions.Storage)
            performOnItemClick()
    }

    override fun onPermissionDenied(permissionGroup: PermissionManager.Permissions) {
        dialogPermissionDescription.detailedDialog().show(supportFragmentManager, dialogPermissionDescription.TAG)
    }

    override fun onPermissionWasDeniedForever(permissionGroup: PermissionManager.Permissions) {
        dialogPermissionDescription.finalDialog().show(supportFragmentManager, dialogPermissionDescription.TAG)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        if(permissionManager.isPermissionsGrant(grantResults))
            performOnItemClick()
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == OPEN_APP_INFO_CODE) {
            permissionManager.checkPermissionsAndRun(this, this, PermissionManager.Permissions.Storage)
        }
    }
}