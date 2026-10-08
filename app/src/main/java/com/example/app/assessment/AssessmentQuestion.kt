package com.example.app.assessment

enum class AssessmentType {
    INTEREST,
    APTITUDE
}

data class AssessmentOption(
    val text: String,
    val score: Int
)

data class AssessmentQuestion(
    val id: Int,
    val type: AssessmentType,
    val question: String,
    val options: List<AssessmentOption>
)
