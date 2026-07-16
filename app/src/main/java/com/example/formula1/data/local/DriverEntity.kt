package com.example.formula1.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "drivers",
    indices = [Index("teamId")]
)
data class DriverEntity(
    @PrimaryKey val driverId: String,
    val teamId: String,
    val name: String,
    val surname: String,
    val nationality: String,
    val birthday: String,
    val number: Int?,
    val shortName: String?,
    val points: Int,
    val position: Int,
    val wins: Int
)
