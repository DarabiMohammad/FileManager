package com.darabi.mohammad.filemanager.di.module

import androidx.fragment.app.Fragment
import com.darabi.mohammad.filemanager.di.FragmentKey
import com.darabi.mohammad.filemanager.ui.dialog.DeleteDialog
import com.darabi.mohammad.filemanager.ui.dialog.NewFileDialog
import com.darabi.mohammad.filemanager.ui.dialog.ThemeSelectionDialog
import com.darabi.mohammad.filemanager.ui.fragment.AppManagerFragment
import com.darabi.mohammad.filemanager.ui.fragment.contents.DirsListFragment
import com.darabi.mohammad.filemanager.ui.fragment.drawer.DrawerFragment
import com.darabi.mohammad.filemanager.ui.fragment.home.HomeFragment
import com.darabi.mohammad.filemanager.ui.fragment.settings.AppearanceFragment
import com.darabi.mohammad.filemanager.ui.fragment.settings.SettingsFragment
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module(includes = [EssentialFragmentsBuilderModule::class])
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

    @Binds
    @IntoMap
    @FragmentKey(AppearanceFragment::class)
    abstract fun bindAppearanceFragment(appearanceFragment: AppearanceFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(AppManagerFragment::class)
    abstract fun bindAppManagerFragment(appManagerFragment: AppManagerFragment): Fragment

    // DialogFragments
    @Binds
    @IntoMap
    @FragmentKey(NewFileDialog::class)
    abstract fun bindNewFileDialog(newFileDialog: NewFileDialog): Fragment

    @Binds
    @IntoMap
    @FragmentKey(DeleteDialog::class)
    abstract fun bindDeleteDialog(deleteDialog: DeleteDialog): Fragment

    @Binds
    @IntoMap
    @FragmentKey(ThemeSelectionDialog::class)
    abstract fun bindThemeSelectionDialog(themeSelectionDialog: ThemeSelectionDialog): Fragment
}