package com.darabi.mohammad.filemanager.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.ui.dialog.PermissionDescriptionDialog
import com.darabi.mohammad.filemanager.ui.fragment.dirs.DirsListFragment
import com.darabi.mohammad.filemanager.util.PermissionManager
import com.darabi.mohammad.filemanager.util.fadeIn
import com.darabi.mohammad.filemanager.util.navigateTo
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import javax.inject.Inject
import kotlin.system.exitProcess

class MainActivity @Inject constructor() : BaseActivity(),
    PermissionManager.PermissionManagerCallback {

    private val openAppInfoScreenFlag = 0

    override val layoutId: Int get() = R.layout.activity_main

    @Inject
    internal lateinit var permissionManager: PermissionManager

    @Inject
    internal lateinit var permissionDescDialog: PermissionDescriptionDialog

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

        viewModel.externalStorageLiveData.observe(this, {
            checkPermissionAndDoWithDirsFragment { getPerimaryStorageFiles(it) }
        })

        viewModel.drawerPrimaryStorageLiveData.observe(this, {
            checkPermissionAndDoWithDirsFragment { getPerimaryStorageFiles(it) }
        })

        viewModel.drawerCategoryLiveData.observe(this, {
            checkPermissionAndDoWithDirsFragment { getFilesForCategory(it.type) }
        })

        viewModel.drawerInstalledAppsLiveData.observe(this, {

        })

        viewModel.drawerSettingsLiveData.observe(this, {
            navigateTo(fragment = settingsFragment, addToBackStack = true).also { closeNavDrawer() }
        })

        viewModel.permissionDialoLiveData.observe(this, {
            when (it) {
                PermissionDescriptionDialog.Action.ACTION_OK -> permissionManager.requestPermissions( this)
                PermissionDescriptionDialog.Action.ACTION_OPEN_SETTINGS -> openAppInfoScreen()
                else -> closeApp()
            }
        })
    }

    override fun onFirstAskPermission(permissionGroup: PermissionManager.Permissions) =
            permissionDescDialog.show(supportFragmentManager)

    override fun onPermissionGranted(function: () -> Unit) = function.invoke()

    override fun onPermissionDenied(permissionGroup: PermissionManager.Permissions) =
            permissionDescDialog.detailedDialog().show(supportFragmentManager)

    override fun onPermissionWasDeniedForever(permissionGroup: PermissionManager.Permissions) =
            permissionDescDialog.finalDialog().show(supportFragmentManager)

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) =
        permissionManager.invokeIfPermissionIsGranted(grantResults).run {
            if (!this) super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == openAppInfoScreenFlag)
            permissionManager.invokeIfPermissionIsGranted(this)
        else super.onActivityResult(requestCode, resultCode, data)
    }

    private inline infix fun PermissionManager.checkAndDo(crossinline function: () -> Unit) =
        checkPermissionsAndRun(this@MainActivity, this@MainActivity, PermissionManager.Permissions.Storage) {
            function.invoke()
        }.also { closeNavDrawer() }

    private inline fun checkPermissionAndDoWithDirsFragment(crossinline function: DirsListFragment.() -> Unit) = permissionManager checkAndDo {
        navigateTo(fragment = dirsListFragment, addToBackStack = true).also { dirsListFragment.function() }
    }

    private fun closeApp() {
        finish()
        exitProcess(0)
    }

    private fun openAppInfoScreen() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        intent.data = Uri.parse("package:$packageName")
        startActivityForResult(intent, openAppInfoScreenFlag)
    }
}