package com.ainsln.feature.notes.state

import com.ainsln.core.data.result.Result

sealed interface ActionState <out T>{
    data object Idle : ActionState<Nothing>
    data object Loading: ActionState<Nothing>
    data class Error(val errorMessage: String): ActionState<Nothing>
    data class Success<T>(val data: T): ActionState<T>
}

fun <T> Result<T>.toActionState(): ActionState<T> {
    return when(this){
        is Result.Loading -> ActionState.Loading
        is Result.Error -> ActionState.Error(e.message ?: "")
        is Result.Success -> ActionState.Success(data)
    }
}
