package com.darabi.mohammad.filemanager.ui.fragment.contents

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.model.*
import com.darabi.mohammad.filemanager.ui.fragment.base.BaseFragment
import com.darabi.mohammad.filemanager.util.SingleEventWrapper
import com.darabi.mohammad.filemanager.util.fadeIn
import com.darabi.mohammad.filemanager.util.invisible
import com.darabi.mohammad.filemanager.view.adapter.base.OnItemClickListener
import com.darabi.mohammad.filemanager.view.adapter.content.CopyMoveRecyclerAdapter
import com.darabi.mohammad.filemanager.vm.base.MainViewModel
import com.darabi.mohammad.filemanager.vm.ccontent.CopyMoveViewModel
import kotlinx.android.synthetic.main.bottom_sheet_fragment_copy_move.*
import kotlinx.android.synthetic.main.bottom_sheet_fragment_copy_move.view.*
import kotlinx.android.synthetic.main.fragment_content.*
import java.io.IOException
import java.util.*
import javax.inject.Inject

class CopyMoveBottomSheetFragment @Inject constructor (
    private val viewModel: CopyMoveViewModel,
    private val adapter: CopyMoveRecyclerAdapter
) : BaseFragment(R.layout.bottom_sheet_fragment_copy_move), OnItemClickListener<BaseItem>,
    Observer<Result<ArrayList<out BaseItem>>>, View.OnClickListener {

    override val fragmentTag: String get() = this.javaClass.simpleName
    override val mainViewModel: MainViewModel by viewModels( { requireActivity() } )

    private val volumesHandler by lazy { VolumesHandler() }

    enum class Action { COPY, MOVE }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        observeViewModel()
        viewModel.clearTempDirectories()
        viewModel.getVolumes().observe(viewLifecycleOwner, volumesHandler)
    }

    override fun onClick(view: View?) = when (view?.id) {
        R.id.txt_done -> {}
        R.id.txt_cancel -> mainViewModel.onPathSelected.value = null
        else -> {}
    }

    override fun onChanged(response: Result<ArrayList<out BaseItem>>?) {
        response?.let {
            when(it.status) {
                Status.LOADING -> txt_done.fadeIn()
                Status.SUCCESS -> adapter.setSource(arrayListOf<BaseItem>(EmptyDivider).apply { addAll(it.result!!) })
                Status.ERROR -> onError(response.throwable!!)
            }
        }
    }

    override fun onItemClick(item: BaseItem) = if (item is Directory)
        viewModel.getFolders(item.path).observe(this, this)
    else
        mainViewModel.openNewFileDialog.value = FileType.Directory

    override fun onBackPressed() = if (txt_done.visibility == View.INVISIBLE)
        mainViewModel.onPathSelected.value = null
    else
        viewModel.onBackPressed().observe(this, this)

    fun onNewFileCreated(fileName: String) = viewModel.createFolder(fileName).observe(viewLifecycleOwner, {
        when (it.status) {
            Status.LOADING -> {}
            Status.SUCCESS -> adapter.addSource(it.result!!.first, it.result.second).also {
                mainViewModel.openNewFileDialog.value = null
            }
            Status.ERROR -> mainViewModel.onCreateFileError.value = SingleEventWrapper(it.throwable!!.message!!)
        }
    })

    private fun initViews() {
        txt_done.setOnClickListener(this)
        txt_cancel.setOnClickListener(this)
        rcv_folders.adapter = adapter.apply { callback = this@CopyMoveBottomSheetFragment }
    }

    private fun observeViewModel() {

    }

    private fun onError(throwable: Throwable): Unit = when (throwable) {
        is NullPointerException -> viewModel.getVolumes().observe(viewLifecycleOwner, volumesHandler)
//        is IOException -> makeToast("${throwable.message}")
        else -> throw throwable
    }

    private inner class VolumesHandler : Observer<Result<ArrayList<StorageVolume>>> {
        override fun onChanged(response: Result<ArrayList<StorageVolume>>?) {
            response?.let {
                when (it.status) {
                    Status.LOADING -> txt_done.invisible()
                    Status.SUCCESS -> {
                        adapter.setSource(it.result!!.map { storageVolume ->
                            Directory(storageVolume.name, storageVolume.path, storageVolume.totalSpace)
                        }).also { rcv_folders.fadeIn() }
                    }
                    Status.ERROR -> onError(it.throwable!!)
                }
            }
        }
    }
}