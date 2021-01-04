package com.darabi.mohammad.filemanager.vm.base

import android.app.Application
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainViewModel @Inject constructor (app: Application) : AbstractMainViewModel(app) {
}