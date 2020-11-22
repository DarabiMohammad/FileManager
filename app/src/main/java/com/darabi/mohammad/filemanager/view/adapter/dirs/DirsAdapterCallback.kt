package com.darabi.mohammad.filemanager.view.adapter.dirs

import com.darabi.mohammad.filemanager.view.adapter.base.BaseAdapterCallback

interface DirsAdapterCallback<I> : BaseAdapterCallback<I> {

    fun onAllSelected()

    fun onClear()

    fun onCheckStateChange(position: Int, isChecked: Boolean)

    fun onRenameClick(item: I)

    fun onEncryptClick(item: I)

    fun onDetailsClick(item: I)
}