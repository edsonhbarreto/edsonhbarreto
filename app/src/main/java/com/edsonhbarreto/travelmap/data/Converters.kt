package com.edsonhbarreto.travelmap.data

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class Converters {
    @TypeConverter
    fun fromStringList(value: List<String>): String = Json.encodeToString(value)

    @TypeConverter
    fun toStringList(value: String): List<String> =
        if (value.isBlank()) emptyList() else Json.decodeFromString(value)

    @TypeConverter
    fun fromChecklistType(value: ChecklistType): String = value.name

    @TypeConverter
    fun toChecklistType(value: String): ChecklistType = ChecklistType.valueOf(value)
}
