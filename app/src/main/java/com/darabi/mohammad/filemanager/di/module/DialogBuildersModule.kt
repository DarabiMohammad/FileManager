package com.darabi.mohammad.filemanager.di.module

import androidx.fragment.app.Fragment
import com.darabi.mohammad.filemanager.di.FragmentKey
import com.darabi.mohammad.filemanager.ui.dialog.CopyMoveStatusDialog
import com.darabi.mohammad.filemanager.ui.dialog.DeleteDialog
import com.darabi.mohammad.filemanager.ui.dialog.NewFileDialog
import com.darabi.mohammad.filemanager.ui.dialog.ThemeSelectionDialog
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class DialogBuildersModule {

    @Binds
    @IntoMap
    @FragmentKey(NewFileDialog::class)
    abstract fun bindNewFileDialog(newFileDialog: NewFileDialog): Fragment

    @Binds
    @IntoMap
    @FragmentKey(DeleteDialog::class)
    abstract fun bindDeleteDialog(deleteDialog: DeleteDialog): Fragment

    @Binds
    @IntoMap
    @FragmentKey(ThemeSelectionDialog::class)
    abstract fun bindThemeSelectionDialog(themeSelectionDialog: ThemeSelectionDialog): Fragment

    @Binds
    @IntoMap
    @FragmentKey(CopyMoveStatusDialog::class)
    abstract fun bindCopyMoveStatusDialog(copyMoveStatusDialog: CopyMoveStatusDialog): Fragment
}