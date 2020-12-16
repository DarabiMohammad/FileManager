package com.darabi.mohammad.filemanager.util

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PrefsManager @Inject constructor(private val application: Application) {

    companion object {
        const val PREFS_NAME = "file_manager_prefs"
    }

    private val showHiddenFilesKey = "show_hidden_files_key"
    private val showSplitViewsKey = "show_split_views_key"

    private val prefs: SharedPreferences

    init { prefs = application.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE) }

    private fun putBoolean(key: String, value: Boolean) = prefs.edit().putBoolean(key, value).commit()

    fun setPermissionAskedFlag(permissionName: String) = putBoolean(permissionName, false)

    fun isFirstAskForThisPermission(permissionName: String): Boolean = prefs.getBoolean(permissionName, true)

    fun setHiddenModeEnable(shouldShowHiddenFiles: Boolean) = putBoolean(showHiddenFilesKey, shouldShowHiddenFiles)

    fun isHiddenModeEnabled() = prefs.getBoolean(showHiddenFilesKey, false)

    fun setSplitModeEnable(shouldShowSplitViews: Boolean) = putBoolean(showSplitViewsKey, shouldShowSplitViews)

    fun isSplitModeEnabled(): Boolean = prefs.getBoolean(showSplitViewsKey, true)
}