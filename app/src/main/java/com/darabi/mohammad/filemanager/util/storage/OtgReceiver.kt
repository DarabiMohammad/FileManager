package com.darabi.mohammad.filemanager.util.storage

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.hardware.usb.UsbManager
import javax.inject.Inject

class OtgReceiver @Inject constructor() : BroadcastReceiver() {

    internal var callback: OnUsbConnectionListener? = null

    override fun onReceive(context: Context?, intent: Intent?) {
        if(callback != null) {
            if (intent?.action == UsbManager.ACTION_USB_DEVICE_ATTACHED) {
                callback!!.onOtgConnectionChange("ACTION_USB_DEVICE_ATTACHED")
            } else if (intent?.action == UsbManager.ACTION_USB_DEVICE_DETACHED) {
                callback!!.onOtgConnectionChange("ACTION_USB_DEVICE_DETACHED")
            }
        }
    }
}