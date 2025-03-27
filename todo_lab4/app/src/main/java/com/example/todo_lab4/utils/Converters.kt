package com.example.todo_lab4.utils

import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.ZoneId

class Converters {
    @TypeConverter
    fun fromLocalDate(date: LocalDate?): Long? {
        return date?.atStartOfDay(ZoneId.systemDefault())?.toEpochSecond()
    }

    @TypeConverter
    fun toLocalDate(epochSecond: Long?): LocalDate? {
        return epochSecond?.let {
            LocalDate.ofEpochDay(it)
        }
    }
}