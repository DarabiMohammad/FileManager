package com.darabi.mohammad.filemanager.vm

import android.app.Application
import com.darabi.mohammad.filemanager.vm.base.BaseViewModel
import javax.inject.Inject

class AppManagerViewModel @Inject constructor(
    private val app: Application
) : BaseViewModel(app) {
}