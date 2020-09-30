package com.darabi.mohammad.filemanager.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.darabi.mohammad.filemanager.util.PrefsManager.Companion.PERFS_NAME
import javax.inject.Inject

class PermissionManager @Inject constructor() {

    enum class Group { CAMERA, EXT_STORAGE }

    private lateinit var perfs: SharedPreferences

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

    fun checkPermissionsAndRun(activity: Activity, callback: PermissionCallback, group: Permissions) {
        perfs = activity.getSharedPreferences(PERFS_NAME, Context.MODE_PRIVATE)
        for(permission in group.permissions) {
            when {
                isGranted(permission, activity) -> callback.onPermissionGranted(group)
                else -> {
                    when {
                        shouldShowRational(activity, permission) -> callback.onPermissionDenied(group)
                        else -> {
                            if(isFirstTime(permission)) {
                                perfs.edit().putBoolean(permission, false).apply()
                                callback.onFirstAskPermission(group)
                            } else
                                callback.onPermissionWasDeniedForever(group)
                        }
                    }
                }
            }
        }
    }

    fun requestPermissions(group: Permissions, activity: Activity) =
        ActivityCompat.requestPermissions(activity, group.permissions.toTypedArray(), group.code)

    fun requestPermissions(group: Permissions, fragment: Fragment) =
        fragment.requestPermissions(group.permissions.toTypedArray(), group.code)

    fun isPermissionsGrant(grantResults: IntArray): Boolean =
        grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED

    /**
     * This method returns true if the permission asked before but the user denied without checking ‘Never ask again’.
     * But it will return false in two cases.
     *      1. If the permission is requested first time.
     *      2. If the permission is requested first time.
     */
    private fun shouldShowRational(activity: Activity, permission: String) =
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M)
            activity.shouldShowRequestPermissionRationale(permission)
        else true

    private fun isGranted(permission: String, activity: Activity) =
        ActivityCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED

    private fun isFirstTime(permission: String) = perfs.getBoolean(permission, true)

    interface PermissionCallback {

        fun onFirstAskPermission(permissionGroup: Permissions)

        fun onPermissionGranted(permissionGroup: Permissions)

        fun onPermissionDenied(permissionGroup: Permissions)

        fun onPermissionWasDeniedForever(permissionGroup: Permissions)
    }
}