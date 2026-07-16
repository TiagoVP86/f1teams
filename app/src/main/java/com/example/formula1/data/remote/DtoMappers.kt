package com.example.formula1.data.remote

import com.example.formula1.data.local.DriverEntity
import com.example.formula1.data.local.TeamEntity
import com.example.formula1.domain.model.Driver
import com.example.formula1.domain.model.Team
import com.example.formula1.domain.model.TeamStanding
import com.example.formula1.util.AgeCalculator

fun TeamDto.toEntity(): TeamEntity = TeamEntity(
    teamId = teamId,
    teamName = teamName,
    teamNationality = teamNationality.orEmpty(),
    firstAppeareance = firstAppeareance,
    constructorsChampionships = constructorsChampionships,
    driversChampionships = driversChampionships,
    url = url
)

fun DriverDto.toEntity(teamId: String): DriverEntity = DriverEntity(
    driverId = driverId,
    teamId = teamId,
    name = name,
    surname = surname,
    nationality = nationality.orEmpty(),
    birthday = birthday.orEmpty(),
    number = number,
    shortName = shortName,
    points = points ?: 0,
    position = position ?: 0,
    wins = wins ?: 0
)

fun TeamEntity.toDomain(): Team = Team(
    teamId = teamId,
    teamName = teamName,
    teamNationality = teamNationality,
    firstAppeareance = firstAppeareance,
    isFavorite = isFavorite
)

fun TeamEntity.toStanding(): TeamStanding? {
    val p = points; val pos = position; val w = wins; val s = season
    return if (p != null && pos != null && w != null && s != null)
        TeamStanding(teamName = teamName, points = p, position = pos, wins = w, season = s)
    else null
}

fun DriverEntity.toDomain(): Driver = Driver(
    driverId = driverId,
    fullName = "$name $surname".trim(),
    number = number,
    nationality = nationality,
    age = AgeCalculator.ageFromBirthday(birthday),
    points = points,
    position = position
)
