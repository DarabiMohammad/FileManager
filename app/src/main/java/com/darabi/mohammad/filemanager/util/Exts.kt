package com.darabi.mohammad.filemanager.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.model.BaseResult
import com.darabi.mohammad.filemanager.model.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private fun beginTransaction(
    fragmentManager: FragmentManager, containerId: Int, fragment: Fragment, addToBackStack: Boolean, isReplace: Boolean
) {
    if(fragment.isAdded) return
    else fragmentManager.beginTransaction().also {
        if(isReplace) {
            if(addToBackStack)
                it.addToBackStack(fragment.tag).replace(containerId, fragment, fragment.tag)
            else
                it.replace(containerId, fragment, fragment.tag)
        } else {
            if(addToBackStack)
                it.addToBackStack(fragment.tag).add(containerId, fragment, fragment.tag)
            else
                it.add(containerId, fragment, fragment.tag)
        }
    }.commitAllowingStateLoss()
}

fun AppCompatActivity.navigateTo(
    @IdRes containerId: Int = R.id.container_main, fragment: Fragment, addToBackStack: Boolean = false, isReplace: Boolean = false
) = beginTransaction(supportFragmentManager, containerId, fragment, addToBackStack, isReplace)

fun Fragment.navigateTo(
    @IdRes containerId: Int, fragment: Fragment, addToBackStack: Boolean = false, isReplace: Boolean = false
) = beginTransaction(childFragmentManager, containerId, fragment, addToBackStack, isReplace)

fun FragmentActivity.removeFromBackstack(fragment: Fragment) {
    supportFragmentManager.beginTransaction().also { it.remove(fragment) }.commit()
}

fun inflateLayout(view: ViewGroup, @LayoutRes layout: Int): View = LayoutInflater.from(view.context).inflate(layout, view, false)

fun View.fadeOut() { if(isVisible) visibility = View.GONE }

fun View.fadeIn() { if (!isVisible) visibility = View.VISIBLE }

fun View.invisible() { visibility = View.INVISIBLE }