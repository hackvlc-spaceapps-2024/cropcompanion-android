package com.hackvlc.cropcompanion.data.api

import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.google.gson.JsonParser
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import com.hackvlc.cropcompanion.data.ReadingResponse
import com.hackvlc.cropcompanion.data.ReadingResponse.AlertReadingResponse
import com.hackvlc.cropcompanion.data.ReadingResponse.SensorReadingResponse

class ReadingResponseTypeAdapter : TypeAdapter<ReadingResponse>() {

    override fun write(out: JsonWriter?, value: ReadingResponse?) {

    }

    override fun read(reader: JsonReader): ReadingResponse {
        val jsonObject = JsonParser.parseReader(reader).asJsonObject
        return when (val type = jsonObject.get("type").asInt) {
            0 -> Gson().fromJson(jsonObject, AlertReadingResponse::class.java)
            1 -> Gson().fromJson(jsonObject, SensorReadingResponse::class.java)
            else -> throw JsonParseException("Unknown type: $type")
        }
    }
}