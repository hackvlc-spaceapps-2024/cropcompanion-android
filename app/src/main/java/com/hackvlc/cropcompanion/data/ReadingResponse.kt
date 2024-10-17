package com.hackvlc.cropcompanion.data

sealed class ReadingResponse {
    data class AlertReadingResponse(
        val type: Int = 0,
        val Prevition: Prevition,
        val action: Action
    ) : ReadingResponse()

    data class SensorReadingResponse(val type: Int = 1, val sensor: Sensor) : ReadingResponse()
}


data class Prevition(
    val T2M: Int,
    val SI_EF_TILTED_HORIZONTAL: Int,
    val GWETPROF: Int,
    val QV2M: Int,
    val PRECTOTCORR: Int
)

data class Action(val ok: Boolean, val description: String, val cover: Boolean, val irrigate:Boolean, val light: Boolean)