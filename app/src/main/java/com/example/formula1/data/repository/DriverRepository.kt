package com.example.formula1.data.repository

import com.example.formula1.data.local.DriverDao
import com.example.formula1.data.local.TeamDao
import com.example.formula1.data.remote.F1ApiService
import com.example.formula1.data.remote.toDomain
import com.example.formula1.data.remote.toEntity
import com.example.formula1.data.remote.toStanding
import com.example.formula1.domain.model.Driver
import com.example.formula1.domain.model.TeamStanding
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DriverRepository(
    private val api: F1ApiService,
    private val teamDao: TeamDao,
    private val driverDao: DriverDao
) {
    fun standing(teamId: String): Flow<TeamStanding?> =
        teamDao.observeTeam(teamId).map { it?.toStanding() }

    fun drivers(teamId: String): Flow<List<Driver>> =
        driverDao.observeDrivers(teamId).map { list -> list.map { it.toDomain() } }

    suspend fun refreshTeamDrivers(teamId: String): RefreshResult =
        try {
            val response = api.getTeamDrivers(teamId)
            val season = response.season ?: 0
            response.team?.let { t ->
                teamDao.updateStanding(
                    teamId = teamId,
                    points = t.points ?: 0,
                    position = t.position ?: 0,
                    wins = t.wins ?: 0,
                    season = season
                )
            }
            driverDao.replaceForTeam(
                teamId = teamId,
                drivers = response.drivers.map { it.driver.toEntity(teamId) }
            )
            RefreshResult.Success
        } catch (e: Exception) {
            RefreshResult.Error(e)
        }
}
