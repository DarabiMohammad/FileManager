package com.darabi.mohammad.filemanager.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.CompoundButton
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.model.ItemType
import com.darabi.mohammad.filemanager.ui.dialog.PermissionDescriptionDialog
import com.darabi.mohammad.filemanager.ui.fragment.AppManagerFragment
import com.darabi.mohammad.filemanager.ui.fragment.SettingsFragment
import com.darabi.mohammad.filemanager.ui.fragment.dirs.DirsListFragment
import com.darabi.mohammad.filemanager.ui.fragment.home.HomeFragment
import com.darabi.mohammad.filemanager.util.PermissionManager
import com.darabi.mohammad.filemanager.util.fadeIn
import com.darabi.mohammad.filemanager.util.fadeOut
import com.darabi.mohammad.filemanager.util.navigateTo
import com.darabi.mohammad.filemanager.view.adapter.checkable.BaseCheckableAdapter.Companion.DESTROY_SELECTION_ACTION_MODE
import com.darabi.mohammad.filemanager.vm.MainViewModel
import com.darabi.mohammad.filemanager.vm.ViewModelFactory
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import kotlinx.android.synthetic.main.layout_toolbar.view.*
import javax.inject.Inject
import kotlin.system.exitProcess

class MainActivity @Inject constructor() : AppCompatActivity(), HasAndroidInjector,
    View.OnClickListener, PermissionManager.PermissionCallback,
    CompoundButton.OnCheckedChangeListener {

    @Inject
    lateinit var injector: DispatchingAndroidInjector<Any>

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: MainViewModel by viewModels { viewModelFactory }

    @Inject
    lateinit var fragmentFactory: InjectingFragmentFactory

    @Inject
    lateinit var homeFragment: HomeFragment

    @Inject
    lateinit var dirsListFragment: DirsListFragment

    @Inject
    lateinit var appManagerFragment: AppManagerFragment

    @Inject
    lateinit var settingsFragment: SettingsFragment

    @Inject
    lateinit var dialogPermissionDescription: PermissionDescriptionDialog

    @Inject
    lateinit var permissionManager: PermissionManager

    companion object {
        private const val OPEN_APP_INFO_CODE = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        supportFragmentManager.fragmentFactory = fragmentFactory
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
        observeViewModel()
        navigateTo(fragment = homeFragment, isReplace = true)
    }

    private fun initView() {

        // initializing toolbar
        layout_toolbar.visibility = View.VISIBLE
        layout_toolbar.img_toggle.setOnClickListener(this)
    }

    private fun showSelectionActionMode(checkedItemCount: Int, isSelectedAll: Boolean) {
        img_toggle.fadeOut()
        chb_select_all.fadeIn()
        chb_select_all.setOnCheckedChangeListener(null)
        chb_select_all.isChecked = isSelectedAll
        chb_select_all.setOnCheckedChangeListener(this)
        txt_toolbar_title.text = checkedItemCount.toString()
        container_more_options.fadeIn()
    }

    private fun hideSelectionActionMode() {
        img_toggle.fadeIn()
        chb_select_all.fadeOut()
        container_more_options.fadeOut()
        txt_toolbar_title.text = viewModel.onItemClicke.value?.itemName
    }

    private fun observeViewModel() {

        viewModel.onItemClicke.observe(this, { performOnItemClick() })

        viewModel.onPermissionDIalogDescButtonClick.observe(this, {
            when(it) {
                PermissionDescriptionDialog.DialogAction.ACTION_OK ->
                    permissionManager.requestPermissions(PermissionManager.Permissions.Storage, this)
                PermissionDescriptionDialog.DialogAction.ACTION_OPEN_SETTINGS -> openAppInfoScreen()
                else -> closeApp()
            }
        })

        viewModel.onActionModeChange.observe(this, {
            if(it.first > DESTROY_SELECTION_ACTION_MODE)
                showSelectionActionMode(it.first, it.second)
            else hideSelectionActionMode()
        })
    }

    private fun performOnItemClick() = when(viewModel.onItemClicke.value!!.itemType) {
        ItemType.DRAWER_ITEM_OTHER -> onDrawerOtherItemClick()
        ItemType.DRAWER_ITEM_STORAGE -> onDrawerCategoryItemClick()
        ItemType.LIST_FOLDER_ITEM -> onDirectoryClick()
        else -> {}
    }

    private fun onDrawerOtherItemClick() {
        val drawerItemName = viewModel.onItemClicke.value!!.itemName
        val destinationFragment = if (drawerItemName == getString(R.string.settings)) settingsFragment else appManagerFragment
        layout_toolbar.txt_toolbar_title.text = drawerItemName
        navigateTo(fragment = destinationFragment, addToBackstack = true)
        closeNavDrawer()
    }

    private fun onDrawerCategoryItemClick() {
        closeNavDrawer()
        permissionManager.checkPermissionsAndRun(this, this, PermissionManager.Permissions.Storage)
    }

    private fun onDirectoryClick() =
        permissionManager.checkPermissionsAndRun(this, this, PermissionManager.Permissions.Storage)

    private fun openNavDrawer() = layout_drawer.openDrawer(GravityCompat.START)

    private fun closeNavDrawer() = layout_drawer.closeDrawer(GravityCompat.START)

    private fun closeApp() {
        finish()
        exitProcess(0)
    }

    private fun openAppInfoScreen() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        intent.data = Uri.parse("package:$packageName")
        startActivityForResult(intent, OPEN_APP_INFO_CODE)
    }

    override fun androidInjector(): AndroidInjector<Any> = injector

    override fun onClick(view: View?) =
        when(view?.id) {
            R.id.img_toggle -> openNavDrawer()
            else -> {}
        }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) =
        if(isChecked) dirsListFragment.selectAll() else dirsListFragment.deselectAll()

    override fun onBackPressed() {
        if(layout_drawer.isDrawerOpen(GravityCompat.START))
            closeNavDrawer()
        else {
            supportFragmentManager.fragments.last().also {
                when(it) {
                    is DirsListFragment -> dirsListFragment.onBackPressed()
                    else -> super.onBackPressed()
                }
            }
        }
    }

    override fun onFirstAskPermission(permissionGroup: PermissionManager.Permissions) {
        dialogPermissionDescription.show(supportFragmentManager, dialogPermissionDescription.TAG)
    }

    override fun onPermissionGranted(permissionGroup: PermissionManager.Permissions) {
        if(permissionGroup is PermissionManager.Permissions.Storage)
            navigateTo(fragment = dirsListFragment, addToBackstack = true)
    }

    override fun onPermissionDenied(permissionGroup: PermissionManager.Permissions) =
        dialogPermissionDescription.detailedDialog().show(supportFragmentManager, dialogPermissionDescription.TAG)

    override fun onPermissionWasDeniedForever(permissionGroup: PermissionManager.Permissions) =
        dialogPermissionDescription.finalDialog().show(supportFragmentManager, dialogPermissionDescription.TAG)

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if(permissionManager.isPermissionsGrant(grantResults))
            navigateTo(fragment = dirsListFragment, addToBackstack = true)
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == OPEN_APP_INFO_CODE)
            permissionManager.checkPermissionsAndRun(this, this, PermissionManager.Permissions.Storage)
    }
}