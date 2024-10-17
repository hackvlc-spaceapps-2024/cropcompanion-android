package com.hackvlc.cropcompanion.data.api

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.hackvlc.cropcompanion.data.Command
import com.hackvlc.cropcompanion.data.ReadingResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


interface ApiService {

    @GET("/v1/get_status")
    fun readData(): Call<ReadingResponse>

    @POST("/v1/set_orders")
    fun sendCommand(@Body command: Command): Call<Void>

    companion object {

        val INSTANCE: ApiService by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { create() }

        private var gson: Gson = GsonBuilder()
            .registerTypeAdapter(ReadingResponse::class.java, ReadingResponseTypeAdapter())
            .create()

        val logger =
            HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(logger)
            .build()

        private fun create(): ApiService {
            val retrofit: Retrofit = Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("http://192.168.230.130:8000")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }


}