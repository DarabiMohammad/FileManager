package com.darabi.mohammad.filemanager.ui.fragment.contents

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.model.*
import com.darabi.mohammad.filemanager.ui.fragment.base.BaseBottomSheetFragment
import com.darabi.mohammad.filemanager.util.factory.ViewModelFactory
import com.darabi.mohammad.filemanager.view.adapter.base.OnItemClickListener
import com.darabi.mohammad.filemanager.view.adapter.content.CopyMoveRecyclerAdapter
import com.darabi.mohammad.filemanager.vm.ccontent.ContentViewModel
import com.darabi.mohammad.filemanager.vm.ccontent.CopyMoveViewModel
import kotlinx.android.synthetic.main.bottom_sheet_fragment_copy_move.*
import java.util.*
import javax.inject.Inject

class CopyMoveBottomSheetFragment @Inject constructor (
    private val viewModelFactory: ViewModelFactory,
    private val copyMoveViewModel: CopyMoveViewModel,
    private val adapter: CopyMoveRecyclerAdapter
) : BaseBottomSheetFragment(), OnItemClickListener<BaseItem>,
    Observer<Result<ArrayList<out BaseItem>>> {

    override val layoutRes: Int get() = R.layout.bottom_sheet_fragment_copy_move

    private val viewModel: ContentViewModel by viewModels { viewModelFactory }

    enum class Action { COPY, MOVE }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        observeViewModel()
        copyMoveViewModel.getVolumes()
    }

    override fun onChanged(response: Result<ArrayList<out BaseItem>>?) {
        response?.let {
            when(it.status) {
                Status.LOADING -> {}
                Status.SUCCESS -> {
                    val list = arrayListOf<BaseItem>(EmptyDivider).apply { addAll(it.result!!) }
                    adapter.setSource(list)
                }
                Status.ERROR -> {}
            }
        }
    }

    override fun onItemClick(item: BaseItem) {
        if (item is Directory)
            viewModel.getFiles(item.path).observe(this, this)
        else {
        }
    }

    fun show(manager: FragmentManager, action: Action) = super.show(manager, fragmentTag)

    private fun initViews() {
        rcv_folders.adapter = adapter.apply { callback = this@CopyMoveBottomSheetFragment }
//        val behavior = BottomSheetBehavior.from(requireView())
//        behavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
    }

    private fun observeViewModel() {

        copyMoveViewModel.availableVolumes.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.LOADING -> {}
                Status.SUCCESS -> { adapter.setSource(it.result!!.map { storageVolume ->
                    Directory(storageVolume.name, storageVolume.path, storageVolume.totalSpace)
                }) }
                Status.ERROR -> {}
            }
        })
    }
}