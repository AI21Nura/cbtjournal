package com.ainsln.core.datastore.info.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class GuideData(
    val intro: String,
    val outro: String,
    @SerialName("examples_list") val examplesList: List<String>,
    @SerialName("examples_colors") val examplesColors: List<String>,
    val steps: List<Step>,
    val examples: List<Map<String, List<String>>>
){
    @Serializable
    public data class Step(
        val tag: String,
        val name: String,
        val instruction: String
    )
}
