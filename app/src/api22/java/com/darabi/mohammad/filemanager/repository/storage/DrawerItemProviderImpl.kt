package com.darabi.mohammad.filemanager.repository.storage

import android.app.Application
import android.os.Environment.*
import com.darabi.mohammad.filemanager.R
import com.darabi.mohammad.filemanager.model.*
import java.io.File
import javax.inject.Inject

@Suppress("DEPRECATION")
class DrawerItemProviderImpl @Inject constructor(private val app: Application) : DrawerItemProvider {

    override fun divider(): Divider = Divider

    override fun dcimDirectory(): Document = getExternalStoragePublicDirectory(DIRECTORY_DCIM).path.run {
        Document(extractNameFromPath(this), CategoryType.DCIM, this, R.drawable.ic_settings_black)
    }

    override fun downloadDirectory(): Document = getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS).path.run {
        Document(extractNameFromPath(this), CategoryType.DOWNLOAD, this, R.drawable.ic_settings_black)
    }

    override fun moviesDirectory(): Document = getExternalStoragePublicDirectory(DIRECTORY_MOVIES).path.run {
        Document(extractNameFromPath(this), CategoryType.MOVIES, this, R.drawable.ic_settings_black)
    }

    override fun musicsDirectory(): Document = getExternalStoragePublicDirectory(DIRECTORY_MUSIC).path.run {
        Document(extractNameFromPath(this), CategoryType.MUSICS, this, R.drawable.ic_settings_black)
    }

    override fun picturesDirectory() = getExternalStoragePublicDirectory(DIRECTORY_PICTURES).path.run {
        Document(extractNameFromPath(this), CategoryType.PICTURES, this, R.drawable.ic_settings_black)
    }

    override fun quickAccess(): Category =
        Category(app.getString(R.string.quick_access), R.drawable.ic_settings_black, CategoryType.QUICK_ACCESS)

    override fun recentFiles(): Category =
        Category(app.getString(R.string.recent_files), R.drawable.ic_settings_black, CategoryType.RECENT_FILES)

    override fun images(): Category =
        Category(app.getString(R.string.images), R.drawable.ic_settings_black, CategoryType.IMAGES)

    override fun videos(): Category =
        Category(app.getString(R.string.videos), R.drawable.ic_settings_black, CategoryType.VIDEOS)

    override fun audio(): Category =
        Category(app.getString(R.string.audio), R.drawable.ic_settings_black, CategoryType.AUDIO)

    override fun ducuments(): Category =
        Category(app.getString(R.string.documents), R.drawable.ic_settings_black, CategoryType.DOCUMENTS)

    override fun apks(): Category =
        Category(app.getString(R.string.apks), R.drawable.ic_settings_black, CategoryType.APKS)

    override fun appManager(): InstalledApps =
        InstalledApps(app.getString(R.string.app_manager), R.drawable.ic_settings_black)

    override fun settings(): Settings =
        Settings(app.getString(R.string.settings), R.drawable.ic_settings_black)

    private fun extractNameFromPath(path: String): String =
        path.substring(path.lastIndexOf(File.separator) + 1, path.length)
}