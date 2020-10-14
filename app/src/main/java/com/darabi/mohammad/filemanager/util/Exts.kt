package com.darabi.mohammad.filemanager.util

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.darabi.mohammad.filemanager.App
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.ui.MainActivity
import com.darabi.mohammad.filemanager.util.storage.VolumeManager
import java.io.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

fun Activity.makeToast(message: String) = Toast.makeText(this, message, Toast.LENGTH_LONG).show()

fun Application.makeToast(message: String) = Toast.makeText(this, message, Toast.LENGTH_LONG).show()

private fun beginTransaction(
    fragmentManager: FragmentManager, containerId: Int, fragment: Fragment, addToBackstack: Boolean, isReplace: Boolean
){
    if(fragment.isAdded) return
    else fragmentManager.beginTransaction().also {
        if(isReplace) {
            if(addToBackstack)
                it.addToBackStack(fragment.tag).replace(containerId, fragment, fragment.tag)
            else
                it.replace(containerId, fragment, fragment.tag)
        } else {
            if(addToBackstack)
                it.addToBackStack(fragment.tag).add(containerId, fragment, fragment.tag)
            else
                it.add(containerId, fragment, fragment.tag)
        }
    }.commit()
}

fun FragmentActivity.navigateTo(
    @IdRes containerId: Int = R.id.container_main, fragment: Fragment,
    addToBackstack: Boolean = false, isReplace: Boolean = false
) {
    beginTransaction(supportFragmentManager, containerId, fragment, addToBackstack, isReplace)
}

fun Fragment.navigateTo(
    @IdRes containerId: Int, fragment: Fragment, addToBackstack: Boolean = false, isReplace: Boolean = false
){
    beginTransaction(childFragmentManager, containerId, fragment, addToBackstack, isReplace)
}

fun inflateLayout(view: ViewGroup, @LayoutRes layout: Int): View =
    LayoutInflater.from(view.context).inflate(layout, view, false)

fun View.fadeOut() { visibility = View.GONE }

fun View.fadeIn() { visibility = View.VISIBLE }

fun View.invisible() { visibility = View.INVISIBLE }