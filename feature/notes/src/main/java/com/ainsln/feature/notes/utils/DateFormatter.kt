package com.ainsln.feature.notes.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun convertMillisToDate(millis: Long): String {
    return formatDate(Date(millis))
}

fun formatDate(date: Date): String{
    val formatter = SimpleDateFormat("E, dd MMM. yyyy", Locale.getDefault())
    return formatter.format(date)
}
