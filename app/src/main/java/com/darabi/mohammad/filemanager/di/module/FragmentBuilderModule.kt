package com.darabi.mohammad.filemanager.di.module

import androidx.fragment.app.Fragment
import com.darabi.mohammad.filemanager.di.FragmentKey
import com.darabi.mohammad.filemanager.ui.fragment.DirsListFragment
import com.darabi.mohammad.filemanager.ui.fragment.DrawerFragment
import com.darabi.mohammad.filemanager.ui.fragment.HomeFragment
import com.darabi.mohammad.filemanager.ui.fragment.SettingsFragment
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class FragmentBuilderModule {

    @Binds
    @IntoMap
    @FragmentKey(DrawerFragment::class)
    abstract fun bindDrawerFragment(drawerFragment: DrawerFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(HomeFragment::class)
    abstract fun bindHomeFragment(homeFragment: HomeFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(DirsListFragment::class)
    abstract fun bindDirsListFragment(dirsListFragment: DirsListFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(SettingsFragment::class)
    abstract fun bindSettingsFragment(settingsFragment: SettingsFragment): Fragment
}