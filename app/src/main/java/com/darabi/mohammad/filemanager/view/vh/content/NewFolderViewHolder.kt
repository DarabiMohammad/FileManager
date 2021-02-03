package com.darabi.mohammad.filemanager.view.vh.content

import android.view.View
import com.darabi.mohammad.filemanager.model.BaseFile
import com.darabi.mohammad.filemanager.model.BaseItem
import com.darabi.mohammad.filemanager.view.adapter.base.OnItemClickListener
import com.darabi.mohammad.filemanager.view.vh.BaseViewHolder

class NewFolderViewHolder constructor(
    private val view: View,
    private val callback: OnItemClickListener<BaseItem>?
) : BaseViewHolder<BaseItem>(view, callback)