package com.example.formula1.domain.model

data class Team(
    val teamId: String,
    val teamName: String,
    val teamNationality: String,
    val firstAppeareance: Int?,
    val isFavorite: Boolean
)

data class TeamStanding(
    val teamName: String,
    val points: Int,
    val position: Int,
    val wins: Int,
    val season: Int
)

data class Driver(
    val driverId: String,
    val fullName: String,
    val number: Int?,
    val nationality: String,
    val age: Int?,
    val points: Int,
    val position: Int
)
