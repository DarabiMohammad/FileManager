package com.darabi.mohammad.filemanager.ui.dialog

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.model.FileType
import com.darabi.mohammad.filemanager.ui.fragment.base.BaseDialogFragment
import com.darabi.mohammad.filemanager.util.*
import com.darabi.mohammad.filemanager.vm.base.MainViewModel
import kotlinx.android.synthetic.main.dialog_new_file.*
import javax.inject.Inject


class NewFileDialog @Inject constructor () : BaseDialogFragment(), View.OnClickListener {

    override val dialogTag: String get() = this.javaClass.simpleName
    override val layoutRes: Int get() = R.layout.dialog_new_file

    private val viewModel: MainViewModel by viewModels ( { requireActivity() } )

    private var type: FileType? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()

        viewModel.onCreateFileError.observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let { message ->
                txt_error.fadeIn().also { txt_error.text = message }
                btn_create_file.invisible()
            }
        })
    }

    override fun show(manager: FragmentManager, tag: String?) {
        if(type == null) throw IllegalAccessException("$dialogTag : type must not be null.")
        super.show(manager, tag)
    }

    override fun dismiss() {
        super.dismiss()
        // todo maybe unesseccary
        requireActivity().removeFromBackstack(this)
    }

    override fun onClick(view: View?) = when(view?.id) {
        R.id.btn_create_file -> txt_error.fadeOut().also { viewModel.onCreateFile.value = Pair(edt_file_name.text.toString(), type!!) }
        else -> dismiss()
    }

    private fun initViews() {

        btn_create_file.setOnClickListener(this)
        btn_cancel.setOnClickListener(this)

        edt_file_name.doAfterTextChanged {
            txt_error.fadeOut()
            if(it.toString().isEmpty()) btn_create_file.invisible() else btn_create_file.fadeIn()
        }

        if(type == FileType.Directory)
            edt_file_name.setText(EMPTY_STRING)
        else if (type == FileType.File)
            edt_file_name.setText(R.string.simple_txt_format)
    }

    fun forFile(): NewFileDialog = this.apply { type = FileType.File }

    fun forFolder(): NewFileDialog = this.apply { type = FileType.Directory }
}