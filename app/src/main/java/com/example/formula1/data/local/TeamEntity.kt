package com.example.formula1.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "teams")
data class TeamEntity(
    @PrimaryKey val teamId: String,
    val teamName: String,
    val teamNationality: String,
    val firstAppeareance: Int?,
    val constructorsChampionships: Int?,
    val driversChampionships: Int?,
    val url: String?,
    // standing (preenchido ao abrir o detalhe)
    val points: Int? = null,
    val position: Int? = null,
    val wins: Int? = null,
    val season: Int? = null,
    val isFavorite: Boolean = false
)
