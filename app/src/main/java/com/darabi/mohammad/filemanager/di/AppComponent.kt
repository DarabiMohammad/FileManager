package com.darabi.mohammad.filemanager.di

import com.darabi.mohammad.filemanager.App
import com.darabi.mohammad.filemanager.di.module.*
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        AppModule::class,
        ActivityBuilderModule::class,
        BroadCastReceiverBuilderModule::class,
        FragmentBuildersModule::class,
        FragmentFactoryBuilderModule::class,
        ViewModelBuildersModule::class,
        ViewModelFactoryBuilderModule::class
    ]
)
interface AppComponent : AndroidInjector<App> {

    @Component.Factory
    abstract class Factory: AndroidInjector.Factory<App>
}