package com.darabi.mohammad.filemanager.di

import com.darabi.mohammad.filemanager.App
import com.darabi.mohammad.filemanager.di.module.ActivityBuilderModule
import com.darabi.mohammad.filemanager.di.module.AppModule
import com.darabi.mohammad.filemanager.di.module.FragmentBuilderModule
import com.darabi.mohammad.filemanager.di.module.ViewModelBuilderModule
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule

@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        AppModule::class,
        ActivityBuilderModule::class,
        FragmentBuilderModule::class,
        ViewModelBuilderModule::class
    ]
)
interface AppComponent : AndroidInjector<App> {

    @Component.Factory
    abstract class Factory: AndroidInjector.Factory<App>
}