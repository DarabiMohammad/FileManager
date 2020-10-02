package com.darabi.mohammad.filemanager.util

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.darabi.mohammad.filemanager.R
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.FileReader
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

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
    @IdRes containerId: Int = R.id.container_main, fragment: Fragment,
    addToBackstack: Boolean = false, isReplace: Boolean = false
){
    beginTransaction(supportFragmentManager, containerId, fragment, addToBackstack, isReplace)
}

fun Fragment.navigateTo(
    @IdRes containerId: Int, fragment: Fragment, addToBackstack: Boolean = false, isReplace: Boolean = false
){
    beginTransaction(childFragmentManager, containerId, fragment, addToBackstack, isReplace)
}

fun inflateLayout(view: ViewGroup, @LayoutRes layout: Int): View =
    LayoutInflater.from(view.context).inflate(layout, view, false)




//val storageList: List<StorageInfo>
//    get() {
//        val list: MutableList<StorageInfo> = ArrayList()
//        val paths = HashSet<String>()
//        var cur_removable_number = 1
//        var buf_reader: BufferedReader? = null
//        try {
//            buf_reader = BufferedReader(FileReader("/proc/mounts"))
//            var line: String = ""
//            while (buf_reader.readLine().also {
//                    if(it != null)
//                        line = it
//                } != null) {
//                if (line.contains("vfat") || line.contains("/mnt")) {
//                    val tokens = StringTokenizer(line, " ")
//                    var unused: String = tokens.nextToken() //device
//                    val mount_point: String = tokens.nextToken() //mount point
//                    if (paths.contains(mount_point)) {
//                        continue
//                    }
//                    unused = tokens.nextToken() //file system
//                    val flags: MutableList<List<String>> =
//                        Arrays.asList(tokens.nextToken().split(",")) //flags
//                    val readonly = flags.contains("ro")
//                    if (line.contains("/dev/block/vold")) {
//                        if (!line.contains("/mnt/secure")
//                            && !line.contains("/mnt/asec")
//                            && !line.contains("/mnt/obb")
//                            && !line.contains("/dev/mapper")
//                            && !line.contains("tmpfs")
//                        ) {
//                            paths.add(mount_point)
//                            list.add(
//                                StorageInfo(
//                                    mount_point,
//                                    readonly,
//                                    true,
//                                    cur_removable_number++
//                                )
//                            )
//                        }
//                    }
//                }
//            }
//        } catch (ex: FileNotFoundException) {
//            ex.printStackTrace()
//        } catch (ex: IOException) {
//            ex.printStackTrace()
//        } finally {
//            if (buf_reader != null) {
//                try {
//                    buf_reader.close()
//                } catch (ex: IOException) {
//                }
//            }
//        }
//        return list
//    }
//
//data class StorageInfo internal constructor(
//    val path: String,
//    val readonly: Boolean,
//    val removable: Boolean,
//    val number: Int
//) {
//    val displayName: String
//        get() {
//            val res = StringBuilder()
//            if (!removable) {
//                res.append("Internal SD card")
//            } else if (number > 1) {
//                res.append("SD card $number")
//            } else {
//                res.append("SD card")
//            }
//            if (readonly) {
//                res.append(" (Read only)")
//            }
//            return res.toString()
//        }
//}