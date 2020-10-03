package com.darabi.mohammad.filemanager.view.vh.drawer

import com.darabi.mohammad.filemanager.view.adapter.base.BaseAdapterCallback

interface OnDrawerItemClickListener : BaseAdapterCallback {

    fun onDrawerItemClick(title: String)
}