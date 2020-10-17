package com.darabi.mohammad.filemanager.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import dagger.android.DispatchingAndroidInjector
import kotlin.system.exitProcess

abstract class BaseActivity : AppCompatActivity() {

    protected companion object {
        const val OPEN_APP_INFO_CODE = 0
    }

    private val bundle by lazy { Bundle() }

    abstract fun saveUiState(uiState: Bundle)

    abstract fun retrieveUiState(uiState: Bundle)

    override fun onResume() {
        super.onResume()

//        retrieveUiState(bundle)
    }

    override fun onPause() {
        super.onPause()

//        saveUiState(bundle)
    }

    protected fun closeApp() {
        finish()
        exitProcess(0)
    }

    protected fun openAppInfoScreen() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        intent.data = Uri.parse("package:$packageName")
        startActivityForResult(intent, OPEN_APP_INFO_CODE)
    }
}