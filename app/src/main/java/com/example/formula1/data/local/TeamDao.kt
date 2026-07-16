package com.example.formula1.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface TeamDao {

    @Query("SELECT * FROM teams ORDER BY teamName COLLATE NOCASE ASC")
    fun observeTeams(): Flow<List<TeamEntity>>

    @Query("SELECT * FROM teams WHERE teamId = :teamId")
    fun observeTeam(teamId: String): Flow<TeamEntity?>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIgnore(teams: List<TeamEntity>)

    @Query(
        """
        UPDATE teams SET
            teamName = :teamName,
            teamNationality = :teamNationality,
            firstAppeareance = :firstAppeareance,
            constructorsChampionships = :constructorsChampionships,
            driversChampionships = :driversChampionships,
            url = :url
        WHERE teamId = :teamId
        """
    )
    suspend fun updateData(
        teamId: String,
        teamName: String,
        teamNationality: String,
        firstAppeareance: Int?,
        constructorsChampionships: Int?,
        driversChampionships: Int?,
        url: String?
    )

    /** Upsert que preserva isFavorite e as colunas de standing. */
    @Transaction
    suspend fun upsertTeams(teams: List<TeamEntity>) {
        insertIgnore(teams) // novos entram completos (favorite=false)
        teams.forEach {
            updateData(
                teamId = it.teamId,
                teamName = it.teamName,
                teamNationality = it.teamNationality,
                firstAppeareance = it.firstAppeareance,
                constructorsChampionships = it.constructorsChampionships,
                driversChampionships = it.driversChampionships,
                url = it.url
            )
        }
    }

    @Query(
        """
        UPDATE teams SET points = :points, position = :position,
            wins = :wins, season = :season
        WHERE teamId = :teamId
        """
    )
    suspend fun updateStanding(teamId: String, points: Int, position: Int, wins: Int, season: Int)

    @Query("UPDATE teams SET isFavorite = :favorite WHERE teamId = :teamId")
    suspend fun setFavorite(teamId: String, favorite: Boolean)
}
