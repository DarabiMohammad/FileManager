package com.darabi.mohammad.filemanager.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
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
import com.darabi.mohammad.filemanager.util.navigateTo
import com.darabi.mohammad.filemanager.vm.MainViewModel
import com.darabi.mohammad.filemanager.vm.ViewModelFactory
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import kotlinx.android.synthetic.main.activity_main.*
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

    private lateinit var selectedItem: BaseItem

    companion object {
        const val OPEN_APP_INFO_CODE = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        supportFragmentManager.fragmentFactory = fragmentFactory
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigateTo(fragment = homeFragment, isReplace = true)

        initView()

        observeViewModel()
    }

    private fun initView() {

        // initializing toolbar
        layout_toolbar.visibility = View.VISIBLE
        layout_toolbar.img_toggle.setOnClickListener(this)
    }

    private fun observeViewModel() {

        viewModel.onItemClicke.observe(this, {
            //todo wrong flow for checking storage permission. we don't need permission when user taps on settings item in drawer view
            selectedItem = it
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
    }

    private fun performOnItemClick() {
        when(selectedItem.itemType) {
            ItemType.DRAWER_ITEM -> onDrawerItemClick(selectedItem)
            ItemType.LIST_FOLDER_ITEM -> onDirectoryClick()
            else -> {}
        }
    }

    private fun onDrawerItemClick(item: BaseItem) {
        layout_toolbar.txt_toolbar_title.text = item.itemName
        val destinationFragment: BaseFragment = when(item.itemName) {
            getString(R.string.settings) -> settingsFragment
            getString(R.string.app_manager) -> appManagerFragment
            else -> dirsListFragment
        }
        navigateTo(fragment = destinationFragment, addToBackstack = true)
        layout_drawer.closeDrawer(GravityCompat.START)
    }

    private fun onDirectoryClick() =
        if(!dirsListFragment.isAdded)
            navigateTo(fragment = dirsListFragment, addToBackstack = true)
        else
            //todo there is issues with field injection, we need new instance of dirsListFragment for passing in this method
            dirsListFragment.navigateTo(R.id.container_home, dirsListFragment, addToBackstack = true)

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
        if(layout_drawer.isDrawerOpen(GravityCompat.START))
            closeNavDrawer()
        else {
            //todo there is issues with back button in recursive fragments like dirsListFragment
            val lastChildFragmentManager = supportFragmentManager.fragments.last().childFragmentManager
            if(lastChildFragmentManager.backStackEntryCount >= 1) lastChildFragmentManager.popBackStack()
            else super.onBackPressed()
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