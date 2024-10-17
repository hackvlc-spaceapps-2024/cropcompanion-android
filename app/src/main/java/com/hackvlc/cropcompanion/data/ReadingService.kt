package com.hackvlc.cropcompanion.data

import android.util.Log
import com.hackvlc.cropcompanion.data.ReadingResponse.*
import com.hackvlc.cropcompanion.data.api.ApiService
import retrofit2.Call


interface ReadingService {

    fun getAlarms(): Alarm?

    fun getSensorData(): List<Sensor>

    class Impl(private val api: ApiService) : ReadingService {

        private var alert: Alarm? = null

        private val sensor = mutableListOf<Sensor>()

        override fun getAlarms(): Alarm? {
            read()
            val nAlert = alert
            alert = null
            return nAlert
        }

        override fun getSensorData(): List<Sensor> {
            read()
            val data = sensor.toList()
            sensor.clear()
            return data
        }

        private fun read() {
            try {
                val reading = api.readData().execute().body()
                if(reading==null) return
                when (reading) {
                    is AlertReadingResponse -> {
                        alert = Alarm.SolarIsolationAlarm
                    }

                    is SensorReadingResponse -> {
                        sensor.add(reading.sensor)
                    }
                }
            } catch (e: Exception) {
                Log.e("ReadingService", e.message ?: "")
            }
        }
    }
}