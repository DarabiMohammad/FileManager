package com.darabi.mohammad.filemanager.di.module

import android.content.IntentFilter
import android.hardware.usb.UsbManager
import com.darabi.mohammad.filemanager.util.storage.OtgReceiver
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector

@Module
abstract class BroadCastReceiverBuilderModule {

    @ContributesAndroidInjector
    abstract fun contributeOtgReceiver(): OtgReceiver

    companion object {
        @Provides
        fun provideOtgIntentFilter(): IntentFilter {
            val intentFilter = IntentFilter()
            intentFilter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED)
            intentFilter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED)
            return intentFilter
        }
    }
}