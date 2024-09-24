package com.ainsln.core.data.result

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

public sealed interface Result<out T> {
    public data object Loading : Result<Nothing>
    public data class Success<T>(val data: T) : Result<T>
    public data class Error(val e: Throwable) : Result<Nothing>
}

public fun <T> Flow<T>.asFlowResult(): Flow<Result<T>> {
    return map<T, Result<T>> { data -> Result.Success(data) }
        .onStart { emit(Result.Loading) }
        .catch { e -> emit(Result.Error(e)) }
}

public fun <T, R> Result<T>.map(mapper: (T) -> R): Result<R>{
    return when(this) {
        is Result.Loading -> this
        is Result.Success -> Result.Success(data = mapper(data))
        is Result.Error -> this
    }
}
