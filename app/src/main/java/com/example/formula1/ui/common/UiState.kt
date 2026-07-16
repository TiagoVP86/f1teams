package com.example.formula1.ui.common

import com.example.formula1.domain.model.Team

data class TeamsUiState(
    val teams: List<Team> = emptyList(),
    val loading: Boolean = true,
    val errorEmpty: Boolean = false
)

sealed interface TeamsEvent {
    data class FavoriteChanged(val nowFavorite: Boolean) : TeamsEvent
    data object RefreshError : TeamsEvent
}
