package com.darabi.mohammad.filemanager.ui

import android.os.Bundle
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.util.fadeIn
import com.darabi.mohammad.filemanager.util.navigateTo
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import javax.inject.Inject

class MainActivity @Inject constructor() : RuntimeActivity() {

    override val layoutId: Int get() = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        navigateTo(fragment = homeFragment, isReplace = true)
    }

    override fun initStartupViews() {
        // initializing toolbar
        layout_toolbar.fadeIn()
        img_toggle.setOnClickListener(this)
        img_back.setOnClickListener(this)
        img_options.setOnClickListener(this)
        txt_toolbar_delete.setOnClickListener(this)
        txt_toolbar_share.setOnClickListener(this)
    }
}