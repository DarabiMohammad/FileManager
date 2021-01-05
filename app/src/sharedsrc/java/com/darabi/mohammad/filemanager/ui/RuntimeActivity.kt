package com.darabi.mohammad.filemanager.ui

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import com.darabi.mohammad.filemanager.ui.dialog.PermissionDescriptionDialog
import com.darabi.mohammad.filemanager.ui.fragment.dirs.DirsListFragment
import com.darabi.mohammad.filemanager.util.PermissionManager
import com.darabi.mohammad.filemanager.util.navigateTo
import javax.inject.Inject
import kotlin.system.exitProcess

abstract class RuntimeActivity : BaseActivity(), PermissionManager.PermissionManagerCallback {

    @Inject
    protected lateinit var permissionManager: PermissionManager

    @Inject
    internal lateinit var permissionDescDialog: PermissionDescriptionDialog

    private val openAppInfoScreenFlag = 0

    override fun observeViewModel() {
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

    protected inline infix fun PermissionManager.checkAndDo(crossinline function: () -> Unit) =
        checkPermissionsAndRun(this@RuntimeActivity, this@RuntimeActivity, PermissionManager.Permissions.Storage) {
            function.invoke()
        }.also { closeNavDrawer() }

    protected inline fun checkPermissionAndDoWithDirsFragment(crossinline function: DirsListFragment.() -> Unit) = permissionManager checkAndDo {
        navigateTo(fragment = dirsListFragment, addToBackStack = true).also { dirsListFragment.function() }
    }

    private fun openAppInfoScreen() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        intent.data = Uri.parse("package:$packageName")
        startActivityForResult(intent, openAppInfoScreenFlag)
    }

    private fun closeApp() = finish().also { exitProcess(0) }
}