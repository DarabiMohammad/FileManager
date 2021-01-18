package com.darabi.mohammad.filemanager.ui

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.CompoundButton
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.ui.fragment.AppManagerFragment
import com.darabi.mohammad.filemanager.ui.fragment.base.BaseFragment
import com.darabi.mohammad.filemanager.ui.fragment.contents.ContentFragment
import com.darabi.mohammad.filemanager.ui.fragment.home.HomeFragment
import com.darabi.mohammad.filemanager.ui.fragment.settings.SettingsFragment
import com.darabi.mohammad.filemanager.util.factory.InjectingFragmentFactory
import com.darabi.mohammad.filemanager.util.factory.ViewModelFactory
import com.darabi.mohammad.filemanager.util.fadeIn
import com.darabi.mohammad.filemanager.util.fadeOut
import com.darabi.mohammad.filemanager.util.navigateTo
import com.darabi.mohammad.filemanager.vm.base.MainViewModel
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import javax.inject.Inject

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

    protected val viewModel: MainViewModel by viewModels { viewModelFactory }

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
    }

    override fun onClick(view: View?) = when (view?.id) {
        R.id.img_toggle -> openNavDrawer()
        R.id.img_back -> onBackPressed()
        R.id.img_options -> onOptionsClick()
        R.id.txt_toolbar_delete -> onDeleteClick()
        else -> {}
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) =
        contentFragment.onAllSelectionClick(isChecked)

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.add_short_cut -> {}
            R.id.hide -> {}
        }
        return true
    }

    override fun onBackPressed() = if(layout_drawer.isDrawerOpen(GravityCompat.START))
        closeNavDrawer()
    else (supportFragmentManager.fragments.last().takeIf { it is BaseFragment } as BaseFragment?)?.onBackPressed()
        ?: super.onBackPressed()

    protected open fun observeViewModel() {

        viewModel.onFragmentBackPressed.observe(this, { supportFragmentManager.popBackStack() })

        viewModel.updateToobarTitle.observe(this, { txt_toolbar_title.text = it })

        viewModel.onActionModeChanged.observe(this, {
            if (it.first > 0) fadeInActionMode(it.first, it.second) else fadeOutActionMode()
        })

        viewModel.drawerSettingsLiveData.observe(this, {
            navigateTo(fragment = settingsFragment, addToBackStack = true).also { closeNavDrawer() }
        })
    }

    protected fun closeNavDrawer() = layout_drawer.closeDrawer(GravityCompat.START)

    private fun openNavDrawer() = layout_drawer.openDrawer(GravityCompat.START)

    private fun lockNavDrawer() = layout_drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

    private fun unlockNavDrawer() = layout_drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)

    private fun onDeleteClick() = contentFragment.onDeleteClicked()

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

    private fun onOptionsClick() = PopupMenu(this, img_options, GravityCompat.END).apply {
        setOnMenuItemClickListener(this@BaseActivity)
        inflate(R.menu.menu_selected_items)
    }.show()
}