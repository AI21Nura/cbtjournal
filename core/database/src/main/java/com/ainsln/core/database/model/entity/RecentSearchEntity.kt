package com.ainsln.core.database.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "RecentSearch")
public data class RecentSearchEntity(
    @PrimaryKey val query: String,
    @ColumnInfo("queried_date") val queriedDate: Date
)
