package com.example.app.skillassessment

data class SkillOption(
    val id: String = "",
    val text: String = "",
    val score: Int = 0
)

data class SkillQuestion(
    val id: String = "",
    val skillId: String = "",
    val question: String = "",
    val options: List<SkillOption> = emptyList()
)
