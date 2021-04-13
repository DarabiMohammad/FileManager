package com.darabi.mohammad.filemanager.di.module

import androidx.lifecycle.ViewModelProvider
import com.darabi.mohammad.filemanager.util.factory.ViewModelFactory;

import dagger.Binds;
import dagger.Module;

@Module
abstract class ViewModelFactoryBuilderModule {

    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory
}
