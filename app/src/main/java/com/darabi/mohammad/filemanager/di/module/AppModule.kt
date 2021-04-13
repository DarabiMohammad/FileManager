package com.darabi.mohammad.filemanager.di.module

import android.app.Application
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.darabi.mohammad.filemanager.App
import com.darabi.mohammad.filemanager.repository.storage.*
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
abstract class AppModule {

    @Binds
    abstract fun bindApplication(application: App): Application

    @Binds
    abstract fun bindVolumes(volumesImpl: VolumesImpl): Volumes

    @Binds
    abstract fun bindDrawerItemProvider(provider: DrawerItemProviderImpl): DrawerItemProvider

    @Binds
    abstract fun bindStorageManager(storageManager: StorageManagerImpl): StorageManager

    companion object {

        @Provides
        fun provideHandler(): Handler = Handler(Looper.getMainLooper())
    }
}