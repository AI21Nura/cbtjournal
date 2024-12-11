package com.ainsln.core.model


data class GuideContent(
    val intro: String,
    val outro: String,
    val examplesList: List<String>,
    val exampleColors: List<String>,
    val steps: List<Step>
) {
    data class Step(
        val tag: String,
        val name: String,
        val instruction: String,
        val examples: List<ExampleForStep>
    )

    data class ExampleForStep(
        val name: String,
        val points: List<String>
    )
}
