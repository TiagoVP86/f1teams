package com.example.formula1.ui.teams

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.formula1.data.repository.RefreshResult
import com.example.formula1.data.repository.TeamRepository
import com.example.formula1.domain.model.Team
import com.example.formula1.ui.common.TeamsEvent
import com.example.formula1.ui.common.TeamsUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TeamsViewModel(
    private val repository: TeamRepository
) : ViewModel() {

    private val _state = MutableStateFlow(TeamsUiState())
    val state: StateFlow<TeamsUiState> = _state.asStateFlow()

    private val _events = MutableSharedFlow<TeamsEvent>()
    val events: SharedFlow<TeamsEvent> = _events.asSharedFlow()

    private var refreshJob: Job? = null

    init {
        observeTeams()
        refresh()
    }

    private fun observeTeams() {
        viewModelScope.launch {
            repository.teams.collect { teams ->
                _state.update { it.copy(teams = teams, errorEmpty = false) }
            }
        }
    }

    fun refresh() {
        if (refreshJob?.isActive == true) return
        refreshJob = viewModelScope.launch {
            _state.update { it.copy(loading = true) }
            val result = repository.refreshTeams()
            _state.update { it.copy(loading = false) }
            if (result is RefreshResult.Error) {
                if (_state.value.teams.isEmpty()) {
                    _state.update { it.copy(errorEmpty = true) }
                } else {
                    _events.emit(TeamsEvent.RefreshError)
                }
            }
        }
    }

    fun onFavoriteClick(team: Team) {
        viewModelScope.launch {
            repository.toggleFavorite(team.teamId, team.isFavorite)
            _events.emit(TeamsEvent.FavoriteChanged(nowFavorite = !team.isFavorite))
        }
    }
}
