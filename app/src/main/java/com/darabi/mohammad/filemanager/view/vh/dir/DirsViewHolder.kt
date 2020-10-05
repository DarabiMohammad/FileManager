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

    override fun bindModel(model: DirItem) {
        if(model is DirItem.Item) {
            //todo add separate view holder for dividing items
            title.text = model.itemName
            if(model.imageRes == null) {
                image.visibility = View.INVISIBLE
                imageMore.visibility = View.GONE
                return
            }
            Glide.with(view).load(model.imageRes).into(image)
            image.setOnClickListener { callback.onLongClick(adapterPosition) }
            view.setOnLongClickListener { callback.onLongClick(adapterPosition) }
            imageMore.setOnClickListener { callback.onMoreClick(adapterPosition) }
            view.setOnClickListener { callback.onClick(adapterPosition) }
        }
    }
}