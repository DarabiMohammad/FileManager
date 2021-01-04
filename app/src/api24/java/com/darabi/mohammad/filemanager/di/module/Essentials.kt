package com.darabi.mohammad.filemanager.di.module

import androidx.fragment.app.Fragment
import com.darabi.mohammad.filemanager.di.FragmentKey
import com.darabi.mohammad.filemanager.ui.dialog.PermissionDescriptionDialog
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class Essentials {

    @Binds
    @IntoMap
    @FragmentKey(PermissionDescriptionDialog::class)
    abstract fun bindPermissionDescriptionDialog(PermissionDescriptionDialog: PermissionDescriptionDialog): Fragment
}