package com.darabi.mohammad.filemanager.util

import android.app.Activity
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.darabi.mohammad.filemanager.R

fun Activity.makeToast(message: String) = Toast.makeText(this, message, Toast.LENGTH_LONG).show()

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
    @IdRes containerId: Int = R.id.container_main,
    fragment: Fragment,
    addToBackstack: Boolean = false,
    isReplace: Boolean = false
){
    beginTransaction(supportFragmentManager, containerId, fragment, addToBackstack, isReplace)
}

fun Fragment.navigateTo(
    @IdRes containerId: Int, fragment: Fragment, addToBackstack: Boolean = false, isReplace: Boolean = false
){
    beginTransaction(childFragmentManager, containerId, fragment, addToBackstack, isReplace)
}