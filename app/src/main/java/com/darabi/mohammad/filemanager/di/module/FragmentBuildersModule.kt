package com.darabi.mohammad.filemanager.di.module

import androidx.fragment.app.Fragment
import com.darabi.mohammad.filemanager.di.FragmentKey
import com.darabi.mohammad.filemanager.ui.fragment.AppManagerFragment
import com.darabi.mohammad.filemanager.ui.fragment.contents.ContentFragment
import com.darabi.mohammad.filemanager.ui.fragment.contents.CopyMoveBottomSheetFragment
import com.darabi.mohammad.filemanager.ui.fragment.drawer.DrawerFragment
import com.darabi.mohammad.filemanager.ui.fragment.home.HomeFragment
import com.darabi.mohammad.filemanager.ui.fragment.settings.AppearanceFragment
import com.darabi.mohammad.filemanager.ui.fragment.settings.SettingsFragment
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module(
    includes = [
        EssentialFragmentsBuilderModule::class,
        DialogBuildersModule::class
    ]
)
abstract class FragmentBuildersModule {

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
    @FragmentKey(ContentFragment::class)
    abstract fun bindDirsListFragment(contentFragment: ContentFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(CopyMoveBottomSheetFragment::class)
    abstract fun bindCopyMoveBottomSheetFragment(copyMoveBottomSheetFragment: CopyMoveBottomSheetFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(SettingsFragment::class)
    abstract fun bindSettingsFragment(settingsFragment: SettingsFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(AppearanceFragment::class)
    abstract fun bindAppearanceFragment(appearanceFragment: AppearanceFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(AppManagerFragment::class)
    abstract fun bindAppManagerFragment(appManagerFragment: AppManagerFragment): Fragment
}