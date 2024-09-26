package com.ainsln.core.database.utils

import androidx.room.TypeConverter
import java.util.Date


internal class DateConverter {

    @TypeConverter
    fun toDate(millis: Long): Date {
        return Date(millis)
    }

    @TypeConverter
    fun fromDate(date: Date): Long {
        return date.time
    }

}
