package com.darabi.mohammad.filemanager.util

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject

class PrefsManager @Inject constructor(private val application: Application) {

    companion object {
        const val PReFS_NAME = "file_manager_prefs"
    }

    private val prefs: SharedPreferences by lazy { application.getSharedPreferences(PReFS_NAME, Context.MODE_PRIVATE) }

    private val editablePrefs: SharedPreferences.Editor by lazy { prefs.edit() }

    fun setFirstAskFlagForPermission(permissionName: String) =
            editablePrefs.putBoolean(permissionName, false).apply()

    fun isAskedPermissionBefore(permissionName: String): Boolean = prefs.getBoolean(permissionName, true)
}