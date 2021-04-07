package com.darabi.mohammad.filemanager.view.adapter.content

import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import com.bumptech.glide.Glide
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.model.BaseItem
import com.darabi.mohammad.filemanager.model.FileItem
import com.darabi.mohammad.filemanager.view.adapter.selection.AdapterCallback
import com.darabi.mohammad.filemanager.view.adapter.selection.HasSelectable
import com.darabi.mohammad.filemanager.view.adapter.selection.SelectionViewHolder

class ContentViewHolder constructor(
        private val view: View,
        private val callback: DirsViewHolderCallback<BaseItem>
) : SelectionViewHolder.SelectableViewHolder<BaseItem>(view, callback) {

    private val title: TextView = view.findViewById(R.id.txt_rcv_item_dir_name)
    private val image: ImageView = view.findViewById(R.id.img_rcv_item_dir_icon)
    private val imageMore: ImageView = view.findViewById(R.id.img_rcv_item_dir_more)

    private val glide by lazy { Glide.with(view) }
    private val popupMenu: PopupMenu

    init {
        popupMenu = PopupMenu(imageMore.context, imageMore, Gravity.END).apply { inflate(R.menu.menu_dir_item) }
    }

    override fun bindModel(model: BaseItem, position: Int) {
        if(model is FileItem) {
            super.bindModel(model, position)
            title.text = model.name
            image.setOnClickListener { notifySelectionChanged(model, position) }
            imageMore.setOnClickListener {
                popupMenu.setOnMenuItemClickListener {menuItem ->
                    when (menuItem.itemId) {
                        R.id.rename -> callback.onRenameClick(model)
                        R.id.encrypt -> callback.onEncryptClick(model)
                        R.id.details -> callback.onDetailsClick(model)
                    }
                    true
                }
                popupMenu.show()
            }
            glide.asDrawable().load(model.icon).into(image)
            glide.asDrawable().load(R.drawable.ic_more_vert_black).into(imageMore)
        }
    }

    interface DirsMenuClickListener <I> {

        fun onRenameClick(item: I)

        fun onEncryptClick(item: I)

        fun onDetailsClick(item: I)
    }

    interface DirsViewHolderCallback <O: HasSelectable> : AdapterCallback <O>,
        DirsMenuClickListener<O>
}