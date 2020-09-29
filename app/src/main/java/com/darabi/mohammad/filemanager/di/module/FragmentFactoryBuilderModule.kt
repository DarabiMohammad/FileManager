package com.darabi.mohammad.filemanager.di.module

import androidx.fragment.app.FragmentFactory
import com.darabi.mohammad.filemanager.ui.InjectingFragmentFactory
import dagger.Binds
import dagger.Module

@Module
abstract class FragmentFactoryBuilderModule {

    @Binds
    abstract fun bindFragmentFactory(fragmentFactory: InjectingFragmentFactory): FragmentFactory
}