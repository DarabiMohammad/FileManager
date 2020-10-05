package com.darabi.mohammad.filemanager.ui

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.model.BaseItem
import com.darabi.mohammad.filemanager.model.ItemType
import com.darabi.mohammad.filemanager.ui.fragment.AppManagerFragment
import com.darabi.mohammad.filemanager.ui.fragment.HomeFragment
import com.darabi.mohammad.filemanager.ui.fragment.SettingsFragment
import com.darabi.mohammad.filemanager.ui.fragment.base.BaseFragment
import com.darabi.mohammad.filemanager.ui.fragment.dirs.DirsListFragment
import com.darabi.mohammad.filemanager.util.navigateTo
import com.darabi.mohammad.filemanager.vm.DrawerViewModel
import com.darabi.mohammad.filemanager.vm.MainViewModel
import com.darabi.mohammad.filemanager.vm.ViewModelFactory
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_toolbar.view.*
import javax.inject.Inject

class MainActivity @Inject constructor() : AppCompatActivity(), HasAndroidInjector,
    View.OnClickListener {

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

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        supportFragmentManager.fragmentFactory = fragmentFactory
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigateTo(fragment = homeFragment, isReplace = true)

        initView()

        observeViewModel()
    }

    private fun initView() {

        // initializing toolbar
        layout_toolbar.visibility = View.VISIBLE
        layout_toolbar.img_toggle.setOnClickListener(this)

        // initializing buttons
        btn_fab.setOnClickListener(this)
    }

    private fun observeViewModel() {

        viewModel.onItemClicke.observe(this, {
            when(it.itemType) {
                ItemType.DRAWER_ITEM -> onDrawerItemClick(it)
                ItemType.LIST_FOLDER_ITEM -> {}
                else -> {}
            }
        })
    }

    private fun onDrawerItemClick(item: BaseItem) {
        layout_toolbar.txt_toolbar_title.text = item.itemName
        val destinationFragment: BaseFragment = when(item.itemName) {
            getString(R.string.settings) -> settingsFragment
            getString(R.string.app_manager) -> appManagerFragment
            else -> dirsListFragment
        }
        navigateTo(fragment = destinationFragment, addToBackstack = true)
        layout_drawer.closeDrawer(GravityCompat.START)
    }

    private fun openNavDrawer() = layout_drawer.openDrawer(GravityCompat.START)

    private fun closeNavDrawer() = layout_drawer.closeDrawer(GravityCompat.START)

    override fun androidInjector(): AndroidInjector<Any> = injector

    override fun onClick(view: View?) =
        when(view?.id) {
            R.id.img_toggle -> openNavDrawer()
            R.id.btn_fab -> {}
            else -> {}
        }

    override fun onBackPressed() {
        if(layout_drawer.isDrawerOpen(GravityCompat.START))
            closeNavDrawer()
        else
            super.onBackPressed()
    }
}