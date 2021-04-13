package com.darabi.mohammad.filemanager.ui.dialog

import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.util.Log
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.model.Result
import com.darabi.mohammad.filemanager.model.Status
import com.darabi.mohammad.filemanager.repository.storage.OnProgressChanged
import com.darabi.mohammad.filemanager.ui.fragment.base.BaseDialogFragment
import com.darabi.mohammad.filemanager.util.TransferAction
import com.darabi.mohammad.filemanager.util.factory.ViewModelFactory
import com.darabi.mohammad.filemanager.util.fadeIn
import com.darabi.mohammad.filemanager.util.fadeOut
import com.darabi.mohammad.filemanager.vm.ccontent.ContentViewModel
import kotlinx.android.synthetic.main.dialog_copy_move_status.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class CopyMoveStatusDialog @Inject constructor(
    private val viewModelFactory: ViewModelFactory
) : BaseDialogFragment(), View.OnClickListener,
    Observer<Result<Unit>>, OnProgressChanged {

    override val layoutRes: Int get() = R.layout.dialog_copy_move_status

    private lateinit var targetPath: String
    private lateinit var action: TransferAction

    private val viewModel: ContentViewModel by viewModels { viewModelFactory }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        viewModel.copyOrMove(targetPath, action, this).observe(viewLifecycleOwner, this)
    }

    override fun show(manager: FragmentManager) = super.show(manager).also { isCancelable = false }

    override fun onClick(view: View?) = when(view?.id) {
        R.id.btn_rename_or_cancle -> if (view.isActivated) { /* handle rename operation */ } else dismiss()
        R.id.btn_replace -> {}
        else -> dismiss() // replace dismiss() with skip stuffs
    }

    override fun onChanged(result: Result<Unit>) {
        when (result.status) {
            Status.LOADING -> {}
            Status.SUCCESS -> dismiss()
            Status.ERROR -> {
                btn_rename_or_cancle.apply {
                    text = getString(R.string.rename)
                    isActivated = true
                }
                txt_title.text = getString(R.string.replace_or_rename)
                btn_replace.fadeIn()
                btn_skip.fadeIn()
                chb_apply_to_all.fadeIn()
                txt_percentage.text = getSpannableError("${result.throwable!!.message}")
                prg_copy.fadeOut()
            }
        }
    }

    override fun onChanged(progress: Int) {
        lifecycleScope.launch {
            prg_copy.progress = progress
            txt_percentage.text = progress.toString()
        }
    }

    fun forAction(targetPath: String, action: TransferAction): CopyMoveStatusDialog = this.apply {
        this.targetPath = targetPath
        this.action = action
    }

    private fun initViews() {

        txt_title.text = if (action == TransferAction.COPY) getString(R.string.copying_to) else getString(R.string.moving_to)

        btn_rename_or_cancle.setOnClickListener(this)
        btn_replace.setOnClickListener(this)
        btn_skip.setOnClickListener(this)
    }

    private fun getSpannableError(text: String) = SpannableString(text).apply {
        setSpan(ForegroundColorSpan(Color.BLACK), 0, text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        setSpan(RelativeSizeSpan(1.5f), 0, text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }
}