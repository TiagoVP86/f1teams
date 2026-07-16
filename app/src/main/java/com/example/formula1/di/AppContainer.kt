package com.example.formula1.di

import android.content.Context
import com.example.formula1.data.local.AppDatabase
import com.example.formula1.data.remote.F1ApiService
import com.example.formula1.data.repository.DriverRepository
import com.example.formula1.data.repository.TeamRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class AppContainer(context: Context) {

    private val moshi: Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val okHttp: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        })
        .build()

    private val api: F1ApiService = Retrofit.Builder()
        .baseUrl("https://f1api.dev/api/")
        .client(okHttp)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
        .create(F1ApiService::class.java)

    private val database: AppDatabase = AppDatabase.build(context.applicationContext)

    val teamRepository: TeamRepository = TeamRepository(api, database.teamDao())
    val driverRepository: DriverRepository =
        DriverRepository(api, database.teamDao(), database.driverDao())
}
