package com.example.formula1.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.formula1.data.repository.DriverRepository
import com.example.formula1.data.repository.RefreshResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
        viewModelScope.launch {
            _state.update { it.copy(loading = true, error = false) }
            val result = repository.refreshTeamDrivers(teamId)
            _state.update {
                it.copy(
                    loading = false,
                    error = result is RefreshResult.Error && it.drivers.isEmpty()
                )
            }
        }
    }
}
