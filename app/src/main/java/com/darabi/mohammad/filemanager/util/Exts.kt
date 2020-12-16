package com.darabi.mohammad.filemanager.util

import android.app.Activity
import android.app.Application
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.model.BaseResult
import com.darabi.mohammad.filemanager.model.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

fun Activity.makeToast(message: String) = Toast.makeText(this, message, Toast.LENGTH_LONG).show()

fun Application.makeToast(message: String) = Toast.makeText(this, message, Toast.LENGTH_LONG).show()

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

fun inflateLayout(view: ViewGroup, @LayoutRes layout: Int): View =
    LayoutInflater.from(view.context).inflate(layout, view, false)

fun View.fadeOut() { visibility = View.GONE }

fun View.fadeIn() { visibility = View.VISIBLE }

fun View.invisible() { visibility = View.INVISIBLE }

suspend inline fun <T> safeSuspendCall(crossinline function: suspend () -> BaseResult<T>): Result<T> = try {
    withContext(Dispatchers.Default) { Result.success(function().result!!) }
} catch (exception: Exception) {
    withContext(Dispatchers.Main) { Result.error(throwable = exception) }
}