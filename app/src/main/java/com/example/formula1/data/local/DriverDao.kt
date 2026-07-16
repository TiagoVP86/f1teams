package com.example.formula1.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface DriverDao {

    @Query("SELECT * FROM drivers WHERE teamId = :teamId ORDER BY points DESC")
    fun observeDrivers(teamId: String): Flow<List<DriverEntity>>

    @Query("DELETE FROM drivers WHERE teamId = :teamId")
    suspend fun deleteForTeam(teamId: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(drivers: List<DriverEntity>)

    @Transaction
    suspend fun replaceForTeam(teamId: String, drivers: List<DriverEntity>) {
        deleteForTeam(teamId)
        insert(drivers)
    }
}
