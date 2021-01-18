package com.darabi.mohammad.filemanager.view.vh.home

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.model.StorageVolume
import com.darabi.mohammad.filemanager.view.adapter.base.OnItemClickListener
import com.darabi.mohammad.filemanager.view.vh.BaseViewHolder

class HomeRecyclerViewHolder constructor(
    private val view: View,
    private val callback: OnItemClickListener<StorageVolume>?
) : BaseViewHolder<StorageVolume>(view, callback) {

    private val name: TextView = view.findViewById(R.id.txt_storage_name)
    private val space: TextView = view.findViewById(R.id.txt_storage_space)
    private val image: ImageView = view.findViewById(R.id.img_rcv_item_storage_dev_icon)
    private val glide: RequestManager = Glide.with(view)

    @SuppressLint("SetTextI18n")
    override fun bindModel(model: StorageVolume) {
        name.text = model.name
        space.text = "${model.freeSpace} / ${model.totalSpace}"
        glide.asDrawable().load(model.icon).into(image)
        super.bindModel(model)
    }
}