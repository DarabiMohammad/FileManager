package com.darabi.mohammad.filemanager.util

const val EMPTY_STRING = ""

enum class TransferAction { COPY, MOVE }

enum class SelectedTheme(val theme: String) {
    LIGHT("Light"),
    DARK("Dark"),
    BATTRY("Depending On Battry"),
    SYSTEM_FOLLOW("Follow System's Theme")
}
