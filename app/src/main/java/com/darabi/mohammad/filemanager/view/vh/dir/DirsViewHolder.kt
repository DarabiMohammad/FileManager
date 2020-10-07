package com.darabi.mohammad.filemanager.view.vh.dir

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.model.DirItem
import com.darabi.mohammad.filemanager.view.vh.BaseViewHolder

class DirsViewHolder constructor(
    val view: View,
    val callback: OnDirClickListener
) : BaseViewHolder<DirItem>(view) {

    private val title: TextView = view.findViewById(R.id.txt_rcv_item_dir_name)
    private val image: ImageView = view.findViewById(R.id.img_rcv_item_dir_icon)
    private val imageMore: ImageView = view.findViewById(R.id.img_rcv_item_dir_more)

    private val glide by lazy { Glide.with(view) }

    override fun bindModel(model: DirItem) {
        if(model is DirItem.Item) {
            //todo add separate view holder for dividing items
            title.text = model.itemName
            image.setOnClickListener { callback.onLongClick(model) }
            view.setOnLongClickListener { callback.onLongClick(model) }
            imageMore.setOnClickListener { callback.onMoreClick(model) }
            view.setOnClickListener { callback.onClick(model) }
            glide.asDrawable().load(model.imageRes).into(image)
            glide.asDrawable().load(R.drawable.ic_more_vert_black).into(imageMore)
        }
    }
}