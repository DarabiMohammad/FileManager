package com.darabi.mohammad.filemanager.repository

import com.darabi.mohammad.filemanager.model.BaseResult
import com.darabi.mohammad.filemanager.model.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal suspend inline fun <T> safeSuspendCall(crossinline function: suspend () -> BaseResult<T>): Result<T> = try {
    withContext(Dispatchers.Default) { Result.success(function().result!!) }
} catch (exception: Exception) {
    withContext(Dispatchers.Main) { Result.error(exception) }
}