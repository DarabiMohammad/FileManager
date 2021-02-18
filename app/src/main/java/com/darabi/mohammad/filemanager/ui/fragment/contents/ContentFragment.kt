package com.darabi.mohammad.filemanager.ui.fragment.contents

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.model.*
import com.darabi.mohammad.filemanager.ui.dialog.DeleteDialog
import com.darabi.mohammad.filemanager.ui.dialog.NewFileDialog
import com.darabi.mohammad.filemanager.ui.fragment.base.BaseFragment
import com.darabi.mohammad.filemanager.util.factory.ViewModelFactory
import com.darabi.mohammad.filemanager.util.fadeIn
import com.darabi.mohammad.filemanager.util.fadeOut
import com.darabi.mohammad.filemanager.util.navigateTo
import com.darabi.mohammad.filemanager.util.removeFromBackstack
import com.darabi.mohammad.filemanager.view.adapter.content.ContentAdapterCallback
import com.darabi.mohammad.filemanager.view.adapter.content.ContentRecyclerAdapter
import com.darabi.mohammad.filemanager.vm.base.MainViewModel
import com.darabi.mohammad.filemanager.vm.ccontent.ContentViewModel
import com.darabi.mohammad.filemanager.vm.ccontent.CopyMoveViewModel
import com.darabi.mohammad.filemanager.vm.ccontent.FileCreationViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.fragment_content.*
import java.io.IOException
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
class ContentFragment @Inject constructor (
    private val newFileDialog: NewFileDialog,
    private val deleteDialog: DeleteDialog,
    private val provider: Provider<CopyMoveBottomSheetFragment>,
    private val viewModelFactory: ViewModelFactory,
    private val contentViewModel: ContentViewModel,
    private val adapter: ContentRecyclerAdapter
) : BaseFragment (R.layout.fragment_content), View.OnClickListener, ContentAdapterCallback<BaseItem>,
    Observer<Result<ArrayList<out BaseItem>>?> {

    override val fragmentTag: String get() = this.javaClass.simpleName
    override val mainViewModel: MainViewModel by viewModels( { requireActivity() } )

    private val copyMoveViewModel: CopyMoveViewModel by viewModels { viewModelFactory }
    private val fileCreationViewModel: FileCreationViewModel by viewModels { viewModelFactory }

    private lateinit var copyBottomSheet: CopyMoveBottomSheetFragment
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        observeViewModel()
        initViews()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onClick(view: View?) = when (view?.id) {
        R.id.btn_fab -> openNewFileDialog(FileType.Directory)
        else -> {}
    }

    override fun onChanged(response: Result<ArrayList<out BaseItem>>?) {
        response?.let { result ->
            when (result.status) {
                Status.LOADING -> {}
                Status.SUCCESS -> adapter.setSource(result.result!!).also {
//                    viewModel.updateToobarTitle.value = dirsListViewModel.getCurrentDirectoryName()
                    if(result.result.isNotEmpty()) rcv_content.fadeIn() else rcv_content.fadeOut()
                }
                Status.ERROR -> onError(result.throwable!!)
            }
        }
    }

    override fun onSelectionChanged(isAllSelected: Boolean, item: BaseItem) =
        contentViewModel.onSelectionChanged(item).observe(this, {
            mainViewModel.onActionModeChanged.value = Pair(it, isAllSelected)
        })

    override fun onSelectAll(items: ArrayList<BaseItem>) =
        contentViewModel.onSelectAll(items).observe(this, {
            mainViewModel.onActionModeChanged.value = it
        })

    override fun onRenameClick(item: BaseItem) {}

    override fun onEncryptClick(item: BaseItem) {}

    override fun onDetailsClick(item: BaseItem) {}

    override fun onItemClick(item: BaseItem) {
        if (item is Directory) contentViewModel.getFiles(item.path).observe(this, this)
    }

    override fun onBackPressed() = when {
        bottomSheetBehavior.state != BottomSheetBehavior.STATE_HIDDEN -> copyBottomSheet.onBackPressed()
        contentViewModel.getSelectedItemsCount() > 0 -> adapter.unselectAll()
        else -> contentViewModel.onBackPressed().observe(this, this)
    }

    fun getFilesForPath(path: String?) = contentViewModel.getFiles(path).observe(this, this)

    fun getFilesForCategory(categoryType: CategoryType?) = contentViewModel.getFilesForCategory(categoryType).observe(this, this)

    fun onAllSelectionClick(isAllSelected: Boolean) = if (isAllSelected) adapter.selectAll() else adapter.unselectAll()

    fun onDeleteClicked() = deleteDialog.show(childFragmentManager)

    fun onCopyClicked() {
        copyBottomSheet = provider.get()
        navigateTo(R.id.copy_move_container, copyBottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
    }

    fun onMoveClicked() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
    }

    private fun initViews() {
        btn_fab.setOnClickListener(this)
        if(rcv_content.adapter == null) rcv_content.adapter = adapter.also { it.adapterCallback = this@ContentFragment }
        bottomSheetBehavior = BottomSheetBehavior.from(copy_move_container)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        bottomSheetBehavior.addBottomSheetCallback(BottomSheetStateCallback())
    }

    private fun observeViewModel() {

        fileCreationViewModel.onCreateFile.observe(viewLifecycleOwner, {
            if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_HIDDEN)
                it.getContentIfNotHandled()?.let { event -> createFile(event.first, event.second) }
        })

        contentViewModel.onFilesDeleted.observe(viewLifecycleOwner, {
            adapter.apply {
                removeSource(it)
                if (itemCount == 0) rcv_content.fadeOut()
                unselectAll()
                deleteDialog.dismiss()
            }
        })

        copyMoveViewModel.onPathSelected.observe(viewLifecycleOwner, {
            if (it == null) bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        })

        copyMoveViewModel.openNewFileDialog.observe(viewLifecycleOwner, {
            if (it != null) openNewFileDialog(it) else newFileDialog.dismiss()
        })
    }

    private fun createFile(fileName: String, type: FileType) =
        contentViewModel.createFile(fileName, type).observe(viewLifecycleOwner, {
            when (it.status) {
                Status.LOADING -> {}
                Status.SUCCESS -> adapter.addSource(it.result!!.first, it.result.second).also {
                    newFileDialog.dismiss()
                    rcv_content.fadeIn()
                }
                Status.ERROR -> onError(it.throwable!!)
            }
        })

    private fun onError(throwable: Throwable) = when (throwable) {
        is NullPointerException -> super.onBackPressed()
        is IOException -> makeToast("${throwable.message}")
        is IllegalArgumentException -> {} // todo handle refresh content list here.
        else -> throw throwable
    }

    private fun openNewFileDialog(type: FileType) = newFileDialog.apply {
        if (type is FileType.Directory) forFolder() else forFile()
    }.show(childFragmentManager, newFileDialog.dialogTag)

    private inner class BottomSheetStateCallback : BottomSheetBehavior.BottomSheetCallback() {

        override fun onStateChanged(bottomSheet: View, newState: Int) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN)
                removeFromBackstack(copyBottomSheet).also { view_shadow.fadeOut() }
            else view_shadow.fadeIn()
        }

        override fun onSlide(bottomSheet: View, slideOffset: Float) {
        }
    }
}