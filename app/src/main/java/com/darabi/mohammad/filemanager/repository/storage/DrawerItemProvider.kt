package com.darabi.mohammad.filemanager.repository.storage

import com.darabi.mohammad.filemanager.model.*

interface DrawerItemProvider {

    fun divider(): Divider

    fun dcimDirectory(): Document

    fun downloadDirectory(): Document

    fun moviesDirectory(): Document

    fun musicsDirectory(): Document

    fun picturesDirectory(): Document

    fun quickAccess(): Category

    fun recentFiles(): Category

    fun images(): Category

    fun videos(): Category

    fun audio(): Category

    fun ducuments(): Category

    fun apks(): Category

    fun appManager(): InstalledApps

    fun settings(): Settings
}