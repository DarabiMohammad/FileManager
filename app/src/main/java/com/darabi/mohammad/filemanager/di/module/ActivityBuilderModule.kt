package com.darabi.mohammad.filemanager.di.module

import com.darabi.mohammad.filemanager.ui.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilderModule {

    @ContributesAndroidInjector(modules = [FragmentFactoryBuilderModule::class])
    abstract fun contributeMainActivity(): MainActivity
}