package com.example.formula1.ui.detail

import com.example.formula1.domain.model.Driver
import com.example.formula1.domain.model.TeamStanding

data class DetailUiState(
    val standing: TeamStanding? = null,
    val drivers: List<Driver> = emptyList(),
    val loading: Boolean = true
)

sealed interface DetailEvent {
    data object RefreshError : DetailEvent
}
