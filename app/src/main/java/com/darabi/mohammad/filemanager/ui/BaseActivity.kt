package com.darabi.mohammad.filemanager.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import android.view.View
import android.widget.CompoundButton
import androidx.activity.viewModels
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.ui.fragment.AppManagerFragment
import com.darabi.mohammad.filemanager.ui.fragment.base.BaseFragment
import com.darabi.mohammad.filemanager.ui.fragment.dirs.DirsListFragment
import com.darabi.mohammad.filemanager.ui.fragment.home.HomeFragment
import com.darabi.mohammad.filemanager.ui.fragment.settings.SettingsFragment
import com.darabi.mohammad.filemanager.util.factory.InjectingFragmentFactory
import com.darabi.mohammad.filemanager.util.factory.ViewModelFactory
import com.darabi.mohammad.filemanager.util.navigateTo
import com.darabi.mohammad.filemanager.vm.base.MainViewModel
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import javax.inject.Inject
import kotlin.system.exitProcess

abstract class BaseActivity : AppCompatActivity(), HasAndroidInjector,
    View.OnClickListener, CompoundButton.OnCheckedChangeListener, PopupMenu.OnMenuItemClickListener {

    @Inject
    protected lateinit var injector: DispatchingAndroidInjector<Any>

    @Inject
    protected lateinit var viewModelFactory: ViewModelFactory
    protected val viewModel: MainViewModel by viewModels { viewModelFactory }

    @Inject
    protected lateinit var fragmentFactory: InjectingFragmentFactory

    @Inject
    protected lateinit var homeFragment: HomeFragment

    @Inject
    protected lateinit var dirsListFragment: DirsListFragment

    @Inject
    protected lateinit var appManagerFragment: AppManagerFragment

    @Inject
    protected lateinit var settingsFragment: SettingsFragment

    @get:LayoutRes
    protected abstract val layoutId: Int

    protected abstract fun initStartupViews()

    override fun androidInjector(): AndroidInjector<Any> = injector

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        supportFragmentManager.fragmentFactory = fragmentFactory
        super.onCreate(savedInstanceState)
        setContentView(layoutId)

        initStartupViews()
        observeViewModel()
    }

    override fun onClick(view: View?) = when (view?.id) {
        R.id.img_toggle -> openNavDrawer()
        R.id.img_back -> onBackPressed()
        R.id.img_options -> onOptionsClick()
        else -> {}
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {}

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
        viewModel.onFragmentBackPressed.observe(this, {
            supportFragmentManager.popBackStack()
        })
    }

    protected fun closeNavDrawer() = layout_drawer.closeDrawer(GravityCompat.START)

    private fun openNavDrawer() = layout_drawer.openDrawer(GravityCompat.START)

    private fun lockNavDrawer() = layout_drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

    private fun unlockNavDrawer() = layout_drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)

    private fun onOptionsClick() = PopupMenu(this, img_options, GravityCompat.END).apply {
        setOnMenuItemClickListener(this@BaseActivity)
        inflate(R.menu.menu_selected_items)
    }.show()
}