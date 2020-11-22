package com.darabi.mohammad.filemanager.view.adapter.checkable

import com.darabi.mohammad.filemanager.view.adapter.base.BaseAdapterCallback

interface CheckableAdapter<I> : BaseAdapterCallback<I> {

    fun hasCheckedItem(): Boolean

    fun onItemCheckedChangeState(position: Int, isChecked: Boolean)

    fun isChecked(position: Int): Boolean

    fun selectAll()

    fun clearAll()
}