package com.darabi.mohammad.filemanager.di.module

import android.os.Handler
import android.os.Looper
import com.darabi.mohammad.filemanager.ui.MainActivity
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilderModule {

    @ContributesAndroidInjector(modules = [FragmentFactoryBuilderModule::class])
    abstract fun contributeMainActivity(): MainActivity

    companion object {

        @Provides
        fun provideHandler(): Handler = Handler(Looper.getMainLooper())
    }
}