package com.hackvlc.cropcompanion.data.api

import com.hackvlc.cropcompanion.data.Alarm
import com.hackvlc.cropcompanion.data.Command
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


interface ApiService {

    @GET("/api/v1/alarms")
    fun getAlarms(): List<Alarm>

    @POST("/api/v1/command")
    fun sendCommand(@Body command: Command)

    companion object {

        val INSTANCE: ApiService by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { create() }

        private fun create(): ApiService {
            val retrofit: Retrofit = Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}