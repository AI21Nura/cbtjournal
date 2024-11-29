package com.ainsln.feature.notes.navigation

import android.content.Context
import android.content.Intent
import kotlinx.serialization.Serializable

sealed interface NotesDestinations {
    @Serializable
    data object List : NotesDestinations
    @Serializable
    data class Detail(val id: Long) : NotesDestinations
    @Serializable
    data class Entry(val id: Long? = null) : NotesDestinations
    @Serializable
    data object DetailPlaceholder : NotesDestinations
}


internal fun shareNote(
    context: Context,
    subject: String,
    summary: String
){
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, subject)
        putExtra(Intent.EXTRA_TEXT, summary)
    }

    context.startActivity(
        Intent.createChooser(
            intent,
            "Share note"
        )
    )
}
