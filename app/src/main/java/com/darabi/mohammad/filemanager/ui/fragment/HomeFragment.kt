package com.darabi.mohammad.filemanager.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.ui.BaseFragment
import com.darabi.mohammad.filemanager.util.PermissionManager
import com.darabi.mohammad.filemanager.util.StorageManager
import kotlinx.android.synthetic.main.home_fragment.*
import java.io.File
import javax.inject.Inject

class HomeFragment @Inject constructor(
    private val storageManager: StorageManager,
    private val permissionManager: PermissionManager
) : BaseFragment(), PermissionManager.PermissionCallback, View.OnClickListener {

    override val layoutRes: Int get() = R.layout.home_fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_req.setOnClickListener(this)
        val sdf = File(context?.getExternalFilesDir(null)?.parent?.split("/Andro")?.get(0))
        val ssdf = storageManager.getPrimaryExternalStorageDirs()
        val d = 3
    }

    override fun onFirstAskPermission(permissionGroup: PermissionManager.Permissions) {
        permissionManager.requestPermissions(permissionGroup, this)
        Log.d("test", "==========onFirstAskPermission")
    }

    override fun onPermissionGranted(permissionGroup: PermissionManager.Permissions) {
        Log.d("test", "==========onPermissionGranted")
    }

    override fun onPermissionDenied(permissionGroup: PermissionManager.Permissions) {
        permissionManager.requestPermissions(permissionGroup, this)
        Log.d("test", "==========onPermissionDenied")
    }

    override fun onPermissionWasDeniedForever(permissionGroup: PermissionManager.Permissions) {
        Log.d("test", "==========onPermissionWasDeniedForever")
    }

    override fun onClick(v: View?) {
        activity?.let { permissionManager.checkPermissionsAndRun(it, this, PermissionManager.Permissions.Storage) }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if(permissionManager.isPermissionsGrant(grantResults)) {
            val sdf = File(context?.getExternalFilesDir(null)?.parent?.split("/Andro")?.get(0)).listFiles()
            Log.d("test", "=========== onRequestPermissionsResult ==== ok")
        } else {
            Log.d("test", "=========== onRequestPermissionsResult ==== no")
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}