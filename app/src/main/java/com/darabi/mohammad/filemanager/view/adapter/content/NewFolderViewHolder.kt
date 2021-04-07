package com.darabi.mohammad.filemanager.view.adapter.content

import android.view.View
import com.darabi.mohammad.filemanager.model.BaseItem
import com.darabi.mohammad.filemanager.view.adapter.base.OnItemClickListener
import com.darabi.mohammad.filemanager.view.adapter.base.BaseViewHolder

class NewFolderViewHolder constructor(
    private val view: View,
    private val callback: OnItemClickListener<BaseItem>?
) : BaseViewHolder<BaseItem>(view, callback)