package com.darabi.mohammad.filemanager.util

import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import javax.inject.Inject

class PermissionManager @Inject constructor(private val prefsManager: PrefsManager) {

    private lateinit var callback: PermissionManagerCallback
    private lateinit var function: () -> Unit
    private lateinit var currentGroup: Permissions

    sealed class Permissions {

        abstract val permissions: List<String>
        abstract val code: Int

        object Storage : Permissions() {
            override val permissions: List<String> get() = listOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            override val code: Int get() = 1
        }
    }

    fun checkPermissionsAndRun(activity: FragmentActivity, callback: PermissionManagerCallback, group: Permissions, function: () -> Unit) =
        group.permissions.forEach {
            when {
                isFirstTime(it) -> callback.onFirstAskPermission(group)
                isGranted(it, activity) -> function()
                shouldShowRational(activity, it) -> callback.onPermissionDenied(group)
                else -> callback.onPermissionWasDeniedForever(group)
            }
        }.also {
            this.callback = callback
            this.function = function
            currentGroup = group
        }

    fun requestPermissions(activity: FragmentActivity) =
        ActivityCompat.requestPermissions(activity, currentGroup.permissions.toTypedArray(), currentGroup.code)

    fun requestPermissions(group: Permissions, fragment: Fragment) =
        fragment.requestPermissions(group.permissions.toTypedArray(), group.code)

    fun isPermissionsGrant(grantResults: IntArray): Boolean =
        (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED).also {
            if (it) callback.onPermissionGranted(function)
        }.also { setAskedFlagForPermission(currentGroup) }

    /**
     * This method returns true if the permission asked before but the user denied without checking ‘Never ask again’.
     * But it will return false in two cases.
     *      1. If the permission is requested first time.
     *      2. If the permission asked before but the user denied with checking ‘Never ask again’.
     */
    private fun shouldShowRational(activity: FragmentActivity, permission: String) =
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M)
            activity.shouldShowRequestPermissionRationale(permission)
        else true

    private fun isGranted(permission: String, activity: FragmentActivity) =
        ActivityCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED

    private fun isFirstTime(permission: String) = prefsManager.isAskedPermissionBefore(permission)

    private fun setAskedFlagForPermission(group: Permissions) = group.permissions.forEach {
        if (isFirstTime(it)) prefsManager.setFirstAskPermissionFlag(it)
    }

    interface PermissionManagerCallback {

        fun onFirstAskPermission(permissionGroup: Permissions)

        fun onPermissionGranted(function: () -> Unit)

        fun onPermissionDenied(permissionGroup: Permissions)

        fun onPermissionWasDeniedForever(permissionGroup: Permissions)
    }
}