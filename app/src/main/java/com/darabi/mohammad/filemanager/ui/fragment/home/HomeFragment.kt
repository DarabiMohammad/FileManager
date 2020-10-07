package com.darabi.mohammad.filemanager.ui.fragment.home

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.model.DirItem
import com.darabi.mohammad.filemanager.ui.fragment.base.BaseFragment
import com.darabi.mohammad.filemanager.util.PermissionManager
import com.darabi.mohammad.filemanager.util.openAppInfoScreen
import com.darabi.mohammad.filemanager.view.adapter.HomwRecyclerAdapter
import com.darabi.mohammad.filemanager.view.vh.home.OnHoneVolumeItemsClickListener
import com.darabi.mohammad.filemanager.vm.HomeViewModel
import com.darabi.mohammad.filemanager.vm.MainViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import javax.inject.Inject
import kotlin.system.exitProcess

class HomeFragment @Inject constructor(
    internal val howeViewModel: HomeViewModel,
    private val adapter: HomwRecyclerAdapter,
    private val permissionManager: PermissionManager
) : BaseFragment(), OnHoneVolumeItemsClickListener, PermissionManager.PermissionCallback {

    override val TAG: String get() = this.javaClass.simpleName
    override val layoutRes: Int get() = R.layout.fragment_home
    override val viewModel: MainViewModel by viewModels ({ requireActivity() })

    @Inject
    lateinit var dialogPermissionDescription: PermissionDescriptionDialog

    private lateinit var selectedVolume: DirItem.Item

    private val volumes by lazy {
        listOf(howeViewModel.convertToDirItem(howeViewModel.getPrimaryExternalStorageVolume()))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter.apply {
            callback = this@HomeFragment
            setSource(volumes)
        }
        rcv_storage.adapter = adapter
        observeViewModel()
    }

    private fun observeViewModel() {

        viewModel.removableStorages.observe(viewLifecycleOwner, {
//            adapter.addSource(it, adapter.itemCount)
        })

        howeViewModel.onPermissionDIalogDescButtonClick.observe(viewLifecycleOwner, {
            when(it) {
                PermissionDescriptionDialog.DialogAction.ACTION_OK ->
                    permissionManager.requestPermissions(PermissionManager.Permissions.Storage, this)
                PermissionDescriptionDialog.DialogAction.ACTION_OPEN_SETTINGS -> openAppInfoScreen()
                else -> closeApp()
            }
        })
    }

    private fun closeApp() {
        activity?.finish()
        exitProcess(0)
    }

    override fun onVolumeClick(volume: DirItem.Item) {
        activity?.let { permissionManager.checkPermissionsAndRun(it, this, PermissionManager.Permissions.Storage) }
        selectedVolume = volume
    }

    override fun onFirstAskPermission(permissionGroup: PermissionManager.Permissions) {
        dialogPermissionDescription.show(childFragmentManager, dialogPermissionDescription.TAG)
    }

    override fun onPermissionGranted(permissionGroup: PermissionManager.Permissions) {
        if(permissionGroup is PermissionManager.Permissions.Storage)
            viewModel.onItemClicke.value = selectedVolume
    }

    override fun onPermissionDenied(permissionGroup: PermissionManager.Permissions) {
        dialogPermissionDescription.detailedDialog().show(childFragmentManager, dialogPermissionDescription.TAG)
    }

    override fun onPermissionWasDeniedForever(permissionGroup: PermissionManager.Permissions) {
        dialogPermissionDescription.finalDialog().show(childFragmentManager, dialogPermissionDescription.TAG)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        if(permissionManager.isPermissionsGrant(grantResults)) {
            viewModel.onItemClicke.value = selectedVolume
        } else {
            Log.d("test", "=========== onRequestPermissionsResult ==== no")
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}