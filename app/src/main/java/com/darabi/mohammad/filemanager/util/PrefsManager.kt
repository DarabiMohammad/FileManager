package com.darabi.mohammad.filemanager.util

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject

class PrefsManager @Inject constructor(private val application: Application) {

    companion object {
        const val PREFS_NAME = "file_manager_prefs"
    }

    private val showHiddenFilesKey = "show_hidden_files_key"
    private val showSplitViewsKey = "show_split_views_key"

    private val prefs: SharedPreferences by lazy { application.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE) }

    private val editablePrefs: SharedPreferences.Editor by lazy { prefs.edit() }

    private fun putBoolean(key: String, value: Boolean) = editablePrefs.putBoolean(key, value).apply()

    fun setFirstAskPermissionFlag(permissionName: String) = putBoolean(permissionName, false)

    fun isAskedPermissionBefore(permissionName: String): Boolean = prefs.getBoolean(permissionName, true)

    fun setHiddenModeEnable(shouldShowHiddenFiles: Boolean) = putBoolean(showHiddenFilesKey, shouldShowHiddenFiles)

    fun isHiddenModeEnabled() = prefs.getBoolean(showHiddenFilesKey, false)

    fun setSplitModeEnable(shouldShowSplitViews: Boolean) = putBoolean(showSplitViewsKey, shouldShowSplitViews)

    fun isSplitModeEnabled(): Boolean = prefs.getBoolean(showSplitViewsKey, true)
}