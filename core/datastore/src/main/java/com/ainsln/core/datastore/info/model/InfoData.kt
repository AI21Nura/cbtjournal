package com.ainsln.core.datastore.info.model

import kotlinx.serialization.Serializable

@Serializable
public data class InfoData(
    val intro: String,
    val guide: String,
    val faq: List<Question>,
    val feedback: String,
    val feedbackEmail: String
){
    @Serializable
    public data class Question(
        val text: String,
        val answer: String
    )
}

