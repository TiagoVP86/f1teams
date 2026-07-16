package com.example.formula1.ui.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/** Factory genérica que cria um ViewModel a partir de um lambda. */
inline fun <VM : ViewModel> viewModelFactory(crossinline create: () -> VM): ViewModelProvider.Factory =
    object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T = create() as T
    }
