package com.darabi.mohammad.filemanager.di.module

import androidx.lifecycle.ViewModel
import com.darabi.mohammad.filemanager.di.ViewModelKey
import com.darabi.mohammad.filemanager.vm.DrawerViewModel
import com.darabi.mohammad.filemanager.vm.HomeViewModel
import com.darabi.mohammad.filemanager.vm.base.MainViewModel
import com.darabi.mohammad.filemanager.vm.ccontent.ContentViewModel
import com.darabi.mohammad.filemanager.vm.ccontent.CopyMoveViewModel
import com.darabi.mohammad.filemanager.vm.settings.AppearanceViewModel
import com.darabi.mohammad.filemanager.vm.settings.SettingsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelBuildersModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel(mainViewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DrawerViewModel::class)
    abstract fun bindDrawerViewModel(drawerViewModel: DrawerViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SettingsViewModel::class)
    abstract fun bindSettingsViewModel(settingsViewModel: SettingsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AppearanceViewModel::class)
    abstract fun bindAppearanceViewModel(appearanceViewModel: AppearanceViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ContentViewModel::class)
    abstract fun bindDirsListViewModel(contentViewModel: ContentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CopyMoveViewModel::class)
    abstract fun bindCopyMoveViewModel(copyMoveViewModel: CopyMoveViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun bindHomeViewModel(homeViewModel: HomeViewModel): ViewModel
}