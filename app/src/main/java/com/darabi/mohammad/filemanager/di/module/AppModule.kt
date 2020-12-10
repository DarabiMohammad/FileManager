package com.darabi.mohammad.filemanager.di.module

import android.app.Application
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.darabi.mohammad.filemanager.App
import com.darabi.mohammad.filemanager.util.path.BasePathManager
import com.darabi.mohammad.filemanager.util.path.PathManager
import com.darabi.mohammad.filemanager.util.storage.PrimaryExternalStorageManager
import com.darabi.mohammad.filemanager.util.storage.AbstractStorageManager
import com.darabi.mohammad.filemanager.util.storage.SecondaryExternalStorageManager
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
abstract class AppModule {

    @Binds
    abstract fun bindContext(application: App): Application

    @Binds
    abstract fun bindPathManager(pathManager: PathManager): BasePathManager

    @Binds
    abstract fun bindPrimaryExternalStorageManager(primaryExternalStorageManager: PrimaryExternalStorageManager): AbstractStorageManager

    @Binds
    abstract fun bindSecondaryExternalStorageManager(secondaryExternalStorageManager: SecondaryExternalStorageManager): AbstractStorageManager

    companion object {

        @Provides
        fun provideBundle() = Bundle()

        @Provides
        fun provideHandler() = Handler(Looper.getMainLooper())
    }
}