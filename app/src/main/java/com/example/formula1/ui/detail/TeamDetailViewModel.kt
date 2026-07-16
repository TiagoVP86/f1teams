package com.example.formula1.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.formula1.data.repository.DriverRepository
import com.example.formula1.data.repository.RefreshResult
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TeamDetailViewModel(
    private val teamId: String,
    private val repository: DriverRepository
) : ViewModel() {

    private val _state = MutableStateFlow(DetailUiState())
    val state: StateFlow<DetailUiState> = _state.asStateFlow()

    private val _events = MutableSharedFlow<DetailEvent>()
    val events: SharedFlow<DetailEvent> = _events.asSharedFlow()

    private var refreshJob: Job? = null

    init {
        observeData()
        refresh()
    }

    private fun observeData() {
        viewModelScope.launch {
            combine(
                repository.standing(teamId),
                repository.drivers(teamId)
            ) { standing, drivers -> standing to drivers }
                .collect { (standing, drivers) ->
                    _state.update { it.copy(standing = standing, drivers = drivers) }
                }
        }
    }

    fun refresh() {
        if (refreshJob?.isActive == true) return
        refreshJob = viewModelScope.launch {
            _state.update { it.copy(loading = true) }
            val result = repository.refreshTeamDrivers(teamId)
            _state.update { it.copy(loading = false) }
            if (result is RefreshResult.Error && _state.value.drivers.isEmpty()) {
                _events.emit(DetailEvent.RefreshError)
            }
        }
    }
}
