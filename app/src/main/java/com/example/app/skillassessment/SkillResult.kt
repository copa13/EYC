package com.example.app.skillassessment

data class SkillResult(
    val skillId: String = "",
    val skillName: String = "",
    val score: Int = 0,
    val maxScore: Int = 0,
    val percentage: Int = 0,
    val level: String = ""
)
