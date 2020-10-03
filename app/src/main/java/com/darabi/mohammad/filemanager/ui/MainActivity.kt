package com.darabi.mohammad.filemanager.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.ui.fragment.HomeFragment
import com.darabi.mohammad.filemanager.util.navigateTo
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
        layout_toolbar.visibility = View.VISIBLE
        layout_toolbar.img_toggle.setOnClickListener(this)
    }

    private fun observeViewModel() {

        viewModel.onDrawerItemClick.observe(this, {
            if(layout_toolbar.txt_toolbar_title.text != it) layout_toolbar.txt_toolbar_title.text = it
            layout_drawer.closeDrawer(GravityCompat.START)
        })
    }

    private fun openOrCloseNavDrawer() =
        if(layout_drawer.isDrawerOpen(GravityCompat.START))
            layout_drawer.closeDrawer(GravityCompat.START)
        else
            layout_drawer.openDrawer(GravityCompat.START)

    override fun androidInjector(): AndroidInjector<Any> = injector

    override fun onClick(view: View?) =
        when(view?.id) {
            R.id.img_toggle -> openOrCloseNavDrawer()
            else -> {}
        }

    override fun onBackPressed() {
        if(layout_drawer.isDrawerOpen(GravityCompat.START))
            layout_drawer.closeDrawer(GravityCompat.START)
        else
            super.onBackPressed()
    }
}