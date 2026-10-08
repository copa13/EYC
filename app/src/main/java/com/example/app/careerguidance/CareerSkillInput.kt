package com.example.app.careerguidance

data class CareerSkillInput(
    val skillId: String = "",
    val skillName: String = "",
    val score: Int = 0,
    val maxScore: Int = 0,
    val percentage: Int = 0,
    val level: String = ""
)
