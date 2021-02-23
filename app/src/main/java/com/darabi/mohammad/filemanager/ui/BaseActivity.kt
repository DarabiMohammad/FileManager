package com.darabi.mohammad.filemanager.ui

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.CompoundButton
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.model.FileType
import com.darabi.mohammad.filemanager.ui.dialog.NewFileDialog
import com.darabi.mohammad.filemanager.ui.fragment.AppManagerFragment
import com.darabi.mohammad.filemanager.ui.fragment.base.BaseFragment
import com.darabi.mohammad.filemanager.ui.fragment.contents.ContentFragment
import com.darabi.mohammad.filemanager.ui.fragment.contents.CopyMoveBottomSheetFragment
import com.darabi.mohammad.filemanager.ui.fragment.home.HomeFragment
import com.darabi.mohammad.filemanager.ui.fragment.settings.SettingsFragment
import com.darabi.mohammad.filemanager.util.factory.InjectingFragmentFactory
import com.darabi.mohammad.filemanager.util.factory.ViewModelFactory
import com.darabi.mohammad.filemanager.util.fadeIn
import com.darabi.mohammad.filemanager.util.fadeOut
import com.darabi.mohammad.filemanager.util.navigateTo
import com.darabi.mohammad.filemanager.util.removeFromBackstack
import com.darabi.mohammad.filemanager.vm.base.MainViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import javax.inject.Inject
import javax.inject.Provider

open class BaseActivity : AppCompatActivity(), HasAndroidInjector,
    View.OnClickListener, CompoundButton.OnCheckedChangeListener, PopupMenu.OnMenuItemClickListener {

    @Inject
    protected lateinit var injector: DispatchingAndroidInjector<Any>

    @Inject
    protected lateinit var viewModelFactory: ViewModelFactory

    @Inject
    protected lateinit var fragmentFactory: InjectingFragmentFactory

    @Inject
    protected lateinit var homeFragment: HomeFragment

    @Inject
    protected lateinit var contentFragment: ContentFragment

    @Inject
    protected lateinit var appManagerFragment: AppManagerFragment

    @Inject
    protected lateinit var settingsFragment: SettingsFragment

    @Inject
    protected lateinit var newFileDialogProvider: Provider<NewFileDialog>

    @Inject
    protected lateinit var bottomSheetProvider: Provider<CopyMoveBottomSheetFragment>

    protected val viewModel: MainViewModel by viewModels { viewModelFactory }

    private lateinit var tempCopyBottomSheet: CopyMoveBottomSheetFragment
    private lateinit var tempNewFileDialog: NewFileDialog
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    override fun androidInjector(): AndroidInjector<Any> = injector

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        supportFragmentManager.fragmentFactory = fragmentFactory
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initStartupViews()
        observeViewModel()
        navigateTo(fragment = homeFragment, isReplace = true)
    }

    fun initStartupViews() {
        // initializing toolbar
        layout_toolbar.fadeIn()
        img_toggle.setOnClickListener(this)
        img_back.setOnClickListener(this)
        img_options.setOnClickListener(this)
        txt_toolbar_delete.setOnClickListener(this)
        txt_toolbar_share.setOnClickListener(this)

        bottomSheetBehavior = BottomSheetBehavior.from(copy_move_container)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        bottomSheetBehavior.addBottomSheetCallback(BottomSheetStateCallback())
    }

    override fun onClick(view: View?) = when (view?.id) {
        R.id.img_toggle -> openNavDrawer()
        R.id.img_back -> onBackPressed()
        R.id.img_options -> onOptionsClick()
        R.id.txt_toolbar_delete -> contentFragment.onDeleteClicked()
        else -> {}
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) =
        contentFragment.onAllSelectionClick(isChecked)

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.copy -> onCopyClicked()
            R.id.move -> onMoveClicked()
            R.id.hide -> {}
            R.id.sort -> {}
        }
        return true
    }

    override fun onBackPressed() = when {
        layout_drawer.isDrawerOpen(GravityCompat.START) -> closeNavDrawer()
        bottomSheetBehavior.state != BottomSheetBehavior.STATE_HIDDEN -> tempCopyBottomSheet.onBackPressed()
        else -> (supportFragmentManager.fragments.last().takeIf { it is BaseFragment } as BaseFragment?)?.onBackPressed()
            ?: super.onBackPressed()
    }

    protected open fun observeViewModel() {

        viewModel.onFragmentBackPressed.observe(this, { supportFragmentManager.popBackStack() })

        viewModel.updateToobarTitle.observe(this, { txt_toolbar_title.text = it })

        viewModel.onActionModeChanged.observe(this, {
            if (it.first > 0) fadeInActionMode(it.first, it.second) else fadeOutActionMode()
        })

        viewModel.drawerSettingsLiveData.observe(this, {
            navigateTo(fragment = settingsFragment, addToBackStack = true).also { closeNavDrawer() }
        })

        viewModel.onPathSelected.observe(this, {
            if (it == null) bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        })

        viewModel.openNewFileDialog.observe(this, {
            if (it != null) openNewFileDialog(it) else tempNewFileDialog.dismiss()
        })

        viewModel.onCreateFile.observe(this, {
            if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_HIDDEN)
                tempCopyBottomSheet.onNewFileCreated(it.first)
            else
                contentFragment.onNewFileCreated(it.first, it.second)
        })
    }

    protected fun closeNavDrawer() = layout_drawer.closeDrawer(GravityCompat.START)

    private fun openNavDrawer() = layout_drawer.openDrawer(GravityCompat.START)

    private fun lockNavDrawer() = layout_drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

    private fun unlockNavDrawer() = layout_drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)

    private fun fadeInActionMode(itemCount: Int, isChecked: Boolean) {
        lockNavDrawer()
        txt_toolbar_title.text = itemCount.toString()
        chb_select_all.fadeIn()
        chb_select_all.setOnCheckedChangeListener(null)
        chb_select_all.isChecked = isChecked
        container_more_options.fadeIn()
        chb_select_all.setOnCheckedChangeListener(this)
    }

    private fun fadeOutActionMode() {
        unlockNavDrawer()
        chb_select_all.fadeOut()
        container_more_options.fadeOut()
    }

    private fun openNewFileDialog(type: FileType) = newFileDialogProvider.get().apply {
        if (type is FileType.Directory) forFolder() else forFile()
        tempNewFileDialog = this
    }.show(supportFragmentManager, tempNewFileDialog.dialogTag)

    private fun onOptionsClick() = PopupMenu(this, img_options, GravityCompat.END).apply {
        setOnMenuItemClickListener(this@BaseActivity)
        inflate(if (chb_select_all.isVisible) R.menu.menu_selection_ops else R.menu.menu_base_ops)
    }.show()

    private fun onCopyClicked() {
        tempCopyBottomSheet = bottomSheetProvider.get()
        shadow_view.fadeIn()
        navigateTo(R.id.copy_move_container, tempCopyBottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
    }

    private fun onMoveClicked() {}

    private inner class BottomSheetStateCallback : BottomSheetBehavior.BottomSheetCallback() {

        override fun onStateChanged(bottomSheet: View, newState: Int) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN)
                removeFromBackstack(tempCopyBottomSheet).also { shadow_view.fadeOut() }
        }

        override fun onSlide(bottomSheet: View, slideOffset: Float) {}
    }
}