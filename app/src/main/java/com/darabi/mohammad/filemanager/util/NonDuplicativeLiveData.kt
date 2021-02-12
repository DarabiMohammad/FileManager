package com.darabi.mohammad.filemanager.util

import androidx.lifecycle.LiveData

class NonDuplicativeLiveData <T> : LiveData<T>() {

    fun setValueOrNull(value: T) {
        val finalValue = if (getValue() == value) null else value
        super.setValue(finalValue)
    }

    override fun postValue(value: T) {
        val finalValue = if (getValue() == value) null else value
        super.postValue(finalValue)
    }
}