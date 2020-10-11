package com.darabi.mohammad.filemanager.view.adapter.checkable

interface CheckableAdapter {

    var checkedItemCount: Int

    fun onItemCheckedChangeState(position: Int, isChecked: Boolean)

    fun isChecked(position: Int): Boolean
}