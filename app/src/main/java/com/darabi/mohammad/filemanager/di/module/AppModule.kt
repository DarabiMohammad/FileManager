package com.darabi.mohammad.filemanager.di.module

import android.app.Application
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.darabi.mohammad.filemanager.App
import com.darabi.mohammad.filemanager.util.path.BasePathManager
import com.darabi.mohammad.filemanager.util.path.PathManager
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
abstract class AppModule {

    @Binds
    abstract fun bindContext(application: App): Application

    @Binds
    abstract fun bindPathManager(pathManager: PathManager): BasePathManager

    companion object {

        @Provides
        fun provideBundle() = Bundle()

        @Provides
        fun provideHandler() = Handler(Looper.getMainLooper())
    }
}