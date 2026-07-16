package com.example.formula1.data.repository

sealed interface RefreshResult {
    data object Success : RefreshResult
    data class Error(val throwable: Throwable) : RefreshResult
}
