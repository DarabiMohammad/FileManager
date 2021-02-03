package com.darabi.mohammad.filemanager.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import com.darabi.mohammad.filemanager.ui.dialog.PermissionDescriptionDialog
import com.darabi.mohammad.filemanager.ui.fragment.contents.ContentFragment
import com.darabi.mohammad.filemanager.util.PermissionManager
import com.darabi.mohammad.filemanager.util.navigateTo
import javax.inject.Inject
import kotlin.system.exitProcess

class MainActivity @Inject constructor() : BaseActivity(), PermissionManager.PermissionManagerCallback {

    @Inject
    internal lateinit var permissionManager: PermissionManager

    @Inject
    internal lateinit var permissionDescDialog: PermissionDescriptionDialog

    private val openAppInfoScreenFlag = 0

    override fun observeViewModel() {
        super.observeViewModel()

        viewModel.volumeClickLiveData.observe(this, {
            checkPermissionAndDoWithContentFragment { getFilesForPath(it.path) }
        })

        viewModel.drawerCategoryFolderLiveData.observe(this, {
            checkPermissionAndDoWithContentFragment { getFilesForPath(it.path) }
        })

        viewModel.drawerCategoryLiveData.observe(this, {
            checkPermissionAndDoWithContentFragment {
                getFilesForCategory(it.type)
            }
        })

        viewModel.drawerInstalledAppsLiveData.observe(this, {

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
        if (requestCode == openAppInfoScreenFlag)
            // todo : app crashes at this line because of not granting permission in the app info screen, in clear word, because of activity recreation
            permissionManager.invokeIfPermissionIsGranted(this)
        else super.onActivityResult(requestCode, resultCode, data)
    }

    private inline fun checkPermissionAndDoWithContentFragment(crossinline function: ContentFragment.() -> Unit) =
        permissionManager.checkPermissionsAndRun(this@MainActivity, this@MainActivity, PermissionManager.Permissions.Storage) {
            navigateTo(fragment = contentFragment, addToBackStack = true).also { contentFragment.function() }
        }.also { closeNavDrawer() }

    private fun openAppInfoScreen() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        intent.data = Uri.parse("package:$packageName")
        startActivityForResult(intent, openAppInfoScreenFlag)
    }

    private fun closeApp() = finish().also { exitProcess(0) }
}