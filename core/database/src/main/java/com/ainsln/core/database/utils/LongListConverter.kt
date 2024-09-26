package com.ainsln.core.database.utils

import androidx.room.TypeConverter

internal class LongListConverter {

    @TypeConverter
    internal fun toList(ids: String): List<Long>{
        return ids.split(";").map { it.toLong() }
    }

    @TypeConverter
    internal fun fromList(ids: List<Long>): String{
        return ids.joinToString(";")
    }

}
