package com.darabi.mohammad.filemanager.view.vh.dir

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.model.DirItem
import com.darabi.mohammad.filemanager.view.adapter.checkable.CheckableAdapter
import com.darabi.mohammad.filemanager.view.vh.checkable.CheckableViewHolder

class DirsViewHolder constructor(
    private val view: View,
    private val viewHolderCallback: DirsViewHolderCallback<DirItem>,
    adapterCallback: CheckableAdapter
) : CheckableViewHolder<DirItem>(view, viewHolderCallback, adapterCallback) {

    private val title: TextView = view.findViewById(R.id.txt_rcv_item_dir_name)
    private val image: ImageView = view.findViewById(R.id.img_rcv_item_dir_icon)
    private val imageMore: ImageView = view.findViewById(R.id.img_rcv_item_dir_more)

    private val glide by lazy { Glide.with(view) }

    override fun bindModel(model: DirItem, position: Int) {
        if(model is DirItem.Item) {
            super.bindModel(model, position)
            //todo add separate view holder for dividing items
            title.text = model.itemName
            image.setOnClickListener { notifyItemCheckedStateChanged(position) }
            imageMore.setOnClickListener { viewHolderCallback.onMoreOptionClick(model) }
            glide.asDrawable().load(model.itemImageRes).into(image)
            glide.asDrawable().load(R.drawable.ic_more_vert_black).into(imageMore)
        }
    }

    interface DirsViewHolderCallback<M> : CheckableViewHolderCallback<M> {

        fun onMoreOptionClick(model: M)
    }
}