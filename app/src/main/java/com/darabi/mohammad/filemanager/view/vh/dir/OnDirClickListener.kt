package com.darabi.mohammad.filemanager.view.vh.dir

interface OnDirClickListener {

    fun onMoreClick(position: Int)

    fun onLongClick(positon: Int): Boolean

    fun onClick(position: Int)
}