package com.hackvlc.cropcompanion.data

sealed class Alarm {
    data object SolarIsolationAlarm : Alarm()
    data object HeavyRainAlarm : Alarm()
    data object StrongWindAlarm : Alarm()
}