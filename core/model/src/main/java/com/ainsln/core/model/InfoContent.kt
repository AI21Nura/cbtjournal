package com.ainsln.core.model

data class InfoContent(
    val intro: String,
    val guide: String,
    val faq: List<Question>,
    val feedback: String,
    val feedbackEmail: String
){
    data class Question(
        val text: String,
        val answer: String
    )
}
