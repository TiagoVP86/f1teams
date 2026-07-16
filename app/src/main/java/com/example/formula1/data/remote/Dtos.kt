package com.example.formula1.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TeamsResponseDto(
    val season: Int?,
    val teams: List<TeamDto> = emptyList()
)

@JsonClass(generateAdapter = true)
data class TeamDto(
    val teamId: String,
    val teamName: String,
    val teamNationality: String?,
    val firstAppeareance: Int?,
    val constructorsChampionships: Int?,
    val driversChampionships: Int?,
    val url: String?
)

@JsonClass(generateAdapter = true)
data class TeamDriversResponseDto(
    val season: Int?,
    val teamId: String?,
    val team: TeamStandingDto?,
    val drivers: List<DriverWrapperDto> = emptyList()
)

@JsonClass(generateAdapter = true)
data class TeamStandingDto(
    val teamName: String?,
    val points: Int?,
    val position: Int?,
    val wins: Int?
)

@JsonClass(generateAdapter = true)
data class DriverWrapperDto(
    val driver: DriverDto
)

@JsonClass(generateAdapter = true)
data class DriverDto(
    val driverId: String,
    val name: String,
    val surname: String,
    val nationality: String?,
    val birthday: String?,
    val number: Int?,
    val shortName: String?,
    val points: Int?,
    val position: Int?,
    val wins: Int?
)
