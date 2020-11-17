package com.darabi.mohammad.filemanager.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.darabi.mohammad.filemanager.di.ViewModelKey
import com.darabi.mohammad.filemanager.vm.*
import com.darabi.mohammad.filemanager.vm.settings.AppearanceViewModel
import com.darabi.mohammad.filemanager.vm.settings.SettingsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelBuilderModule {

    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

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
    @ViewModelKey(DirsListViewModel::class)
    abstract fun bindDirsListViewModel(dirsListViewModel: DirsListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun bindHomeViewModel(homeViewModel: HomeViewModel): ViewModel
}