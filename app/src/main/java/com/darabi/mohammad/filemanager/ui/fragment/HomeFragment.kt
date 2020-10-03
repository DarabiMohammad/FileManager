package com.darabi.mohammad.filemanager.ui.fragment

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.ui.BaseFragment
import com.darabi.mohammad.filemanager.util.PermissionManager
import com.darabi.mohammad.filemanager.util.storage.VolumeManager
import com.darabi.mohammad.filemanager.view.adapter.RecyclerAdapter
import kotlinx.android.synthetic.main.fragment_home.*
import javax.inject.Inject

class HomeFragment @Inject constructor(
    private val adapter: RecyclerAdapter
) : BaseFragment() {

    override val layoutRes: Int get() = R.layout.fragment_home

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        adapter.setSource(volumeManager.getAvailableStorageNames())
//        rcv_storage.adapter = adapter
    }

//    override fun onFirstAskPermission(permissionGroup: PermissionManager.Permissions) {
//        permissionManager.requestPermissions(permissionGroup, this)
//        Log.d("test", "==========onFirstAskPermission")
//    }
//
//    override fun onPermissionGranted(permissionGroup: PermissionManager.Permissions) {
//        Log.d("test", "==========onPermissionGranted")
//    }
//
//    override fun onPermissionDenied(permissionGroup: PermissionManager.Permissions) {
//        permissionManager.requestPermissions(permissionGroup, this)
//        Log.d("test", "==========onPermissionDenied")
//    }
//
//    override fun onPermissionWasDeniedForever(permissionGroup: PermissionManager.Permissions) {
//        Log.d("test", "==========onPermissionWasDeniedForever")
//    }
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        if(permissionManager.isPermissionsGrant(grantResults)) {
//            val sdf = File(context?.getExternalFilesDir(null)?.parent?.split("/Andro")?.get(0)).listFiles()
//            Log.d("test", "=========== onRequestPermissionsResult ==== ok")
//        } else {
//            Log.d("test", "=========== onRequestPermissionsResult ==== no")
//        }
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//    }
}