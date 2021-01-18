package com.darabi.mohammad.filemanager.ui.dialog

import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.model.FileType
import com.darabi.mohammad.filemanager.ui.fragment.base.BaseDialogFragment
import com.darabi.mohammad.filemanager.util.EMPTY_STRING
import com.darabi.mohammad.filemanager.util.fadeIn
import com.darabi.mohammad.filemanager.util.invisible
import com.darabi.mohammad.filemanager.vm.DirsListViewModel
import com.darabi.mohammad.filemanager.util.factory.ViewModelFactory
import kotlinx.android.synthetic.main.dialog_new_file.*
import javax.inject.Inject


class NewFileDialog @Inject constructor (
    private val viewModelFactory: ViewModelFactory
) : BaseDialogFragment(), View.OnClickListener {

    override val dialogTag: String get() = this.javaClass.simpleName
    override val layoutRes: Int get() = R.layout.dialog_new_file

    private val viewModel: DirsListViewModel by viewModels { viewModelFactory }
    private var type: FileType? = null

    override fun onResume() {
        super.onResume()

        initViews()
    }

    override fun show(manager: FragmentManager, tag: String?) {
        if(type == null) throw IllegalAccessException("$dialogTag : type must not be null.")
        super.show(manager, tag)
    }

    override fun onClick(view: View?) {
        when(view?.id) {
            R.id.btn_create_file -> onCreateButtonClick()
        }
    }

    private fun initViews() {

        edt_file_name.doAfterTextChanged {
            if(it.toString().isEmpty()) btn_create_file.invisible() else btn_create_file.fadeIn()
        }

        btn_create_file.setOnClickListener(this)

        if(type == FileType.Directory)
            edt_file_name.setText(EMPTY_STRING)
        else if (type == FileType.File)
            edt_file_name.setText(R.string.simple_txt_format)
    }

    private fun onCreateButtonClick() = viewModel.createFile(edt_file_name.text.toString(), type!!)

    fun forFile(): NewFileDialog = this.apply { type = FileType.File }

    fun forFolder(): NewFileDialog = this.apply { type = FileType.Directory }
}