package com.darabi.mohammad.filemanager.ui.fragment.contents

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.model.*
import com.darabi.mohammad.filemanager.ui.dialog.DeleteDialog
import com.darabi.mohammad.filemanager.ui.dialog.NewFileDialog
import com.darabi.mohammad.filemanager.ui.fragment.base.BaseFragment
import com.darabi.mohammad.filemanager.util.fadeIn
import com.darabi.mohammad.filemanager.util.fadeOut
import com.darabi.mohammad.filemanager.view.adapter.content.ContentAdapterCallback
import com.darabi.mohammad.filemanager.view.adapter.content.ContentRecyclerAdapter
import com.darabi.mohammad.filemanager.vm.base.MainViewModel
import com.darabi.mohammad.filemanager.vm.ccontent.ContentViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.fragment_content.*
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContentFragment @Inject constructor (
    private val newFileDialog: NewFileDialog,
    private val deleteDialog: DeleteDialog,
    private val contentViewModel: ContentViewModel,
    private val adapter: ContentRecyclerAdapter
) : BaseFragment(R.layout.fragment_content), View.OnClickListener, ContentAdapterCallback<BaseItem>,
    Observer<Result<ArrayList<out BaseItem>>?> {

    override val fragmentTag: String get() = this.javaClass.simpleName
    override val viewModel: MainViewModel by viewModels( { requireActivity() } )

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        observeViewModel()
        initViews()
        super.onViewCreated(view, savedInstanceState)
        resources.configuration.uiMode
    }

    override fun onClick(view: View?) = when (view?.id) {
        R.id.btn_fab -> onFabClicked()
        else -> {}
    }

    override fun onChanged(response: Result<java.util.ArrayList<out BaseItem>>?) {
        response?.let { result ->
            when (result.status) {
                Status.LOADING -> {}
                Status.SUCCESS -> adapter.setSource(result.result!!).also {
//                    viewModel.updateToobarTitle.value = dirsListViewModel.getCurrentDirectoryName()
                    if(result.result.isNotEmpty()) rcv_dirs.fadeIn() else rcv_dirs.fadeOut()
                }
                Status.ERROR -> onError(result.throwable!!)
            }
        }
    }

    override fun onSelectionChanged(isAllSelected: Boolean, item: BaseItem) =
        contentViewModel.onSelectionChanged(item).observe(this, {
            viewModel.onActionModeChanged.value = Pair(it, isAllSelected)
        })

    override fun onSelectAll(items: ArrayList<BaseItem>) =
        contentViewModel.onSelectAll(items).observe(this, {
            viewModel.onActionModeChanged.value = it
        })

    override fun onRenameClick(item: BaseItem) {}

    override fun onEncryptClick(item: BaseItem) {}

    override fun onDetailsClick(item: BaseItem) {}

    override fun onItemClick(item: BaseItem) {
        if (item is Directory) contentViewModel.getFiles(item.path).observe(this, this)
    }

    override fun onBackPressed() = if (contentViewModel.getSelectedItemsCount() > 0)
        adapter.unselectAll()
    else
        contentViewModel.onBackPressed().observe(this, this)

    fun getFilesForPath(path: String) = contentViewModel.getFiles(path).observe(this, this)

    fun getFilesForCategory(categoryType: CategoryType) = contentViewModel.getFilesForCategory(categoryType).observe(this, this)

    fun onAllSelectionClick(isAllSelected: Boolean) = if (isAllSelected) adapter.selectAll() else adapter.unselectAll()

    fun onDeleteClicked() = deleteDialog.show(childFragmentManager)

    fun onCopyClicked() = contentViewModel.copy().also {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
    }

    fun onMoveClicked() = contentViewModel.move().also {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
    }

    private fun initViews() {
        btn_fab.setOnClickListener(this)
        if(rcv_dirs.adapter == null) rcv_dirs.adapter = adapter.also { it.adapterCallback = this@ContentFragment }
        bottomSheetBehavior = BottomSheetBehavior.from(view!!.findViewById(R.id.copy_move_bottom_sheet))
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                Log.d("test" , "===========onStateChanged $newState")
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                Log.d("test" , "===========onSlide")
            }
        })
    }

    private fun observeViewModel() {

        contentViewModel.onFileCreated.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.LOADING -> {}
                Status.SUCCESS -> adapter.addSource(it.result!!.first, it.result.second).also {
                    newFileDialog.dismiss()
                    rcv_dirs.fadeIn()
                }
                Status.ERROR -> onError(it.throwable!!)
            }
        })

        contentViewModel.onFilesDeleted.observe(viewLifecycleOwner, {
            adapter.apply {
                removeSource(it)
                if (itemCount == 0) rcv_dirs.fadeOut()
                unselectAll()
                deleteDialog.dismiss()
            }
        })
    }

    private fun onError(throwable: Throwable) = when (throwable) {
        is NullPointerException -> super.onBackPressed()
        is IOException -> makeToast("${throwable.message}")
        else -> throw throwable
    }

    private fun onFabClicked() = newFileDialog.forFolder().show(childFragmentManager, newFileDialog.dialogTag)
}