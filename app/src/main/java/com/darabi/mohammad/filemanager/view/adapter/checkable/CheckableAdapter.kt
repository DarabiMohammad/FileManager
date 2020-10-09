package com.darabi.mohammad.filemanager.view.adapter.checkable

interface CheckableAdapter {

    var checkedItemCount: Int

    fun onItemCheckedChangeState(position: Int, isChecked: Boolean)
}