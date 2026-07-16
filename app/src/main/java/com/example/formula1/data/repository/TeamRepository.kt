package com.example.formula1.data.repository

import com.example.formula1.data.local.TeamDao
import com.example.formula1.data.remote.F1ApiService
import com.example.formula1.data.remote.toDomain
import com.example.formula1.data.remote.toEntity
import com.example.formula1.domain.model.Team
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TeamRepository(
    private val api: F1ApiService,
    private val teamDao: TeamDao
) {
    val teams: Flow<List<Team>> =
        teamDao.observeTeams().map { list -> list.map { it.toDomain() } }

    suspend fun refreshTeams(): RefreshResult =
        try {
            val response = api.getTeams()
            teamDao.upsertTeams(response.teams.map { it.toEntity() })
            RefreshResult.Success
        } catch (e: Exception) {
            RefreshResult.Error(e)
        }

    suspend fun toggleFavorite(teamId: String, current: Boolean) {
        teamDao.setFavorite(teamId, !current)
    }
}
