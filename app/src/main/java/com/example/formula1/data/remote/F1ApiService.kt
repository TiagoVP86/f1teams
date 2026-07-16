package com.example.formula1.data.remote

import retrofit2.http.GET
import retrofit2.http.Path

interface F1ApiService {

    @GET("current/teams")
    suspend fun getTeams(): TeamsResponseDto

    @GET("current/teams/{teamId}/drivers")
    suspend fun getTeamDrivers(@Path("teamId") teamId: String): TeamDriversResponseDto
}
