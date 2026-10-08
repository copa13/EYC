package com.example.app.careerguidance

data class CareerAssessmentInput(
    val interestScore: Int = 0,
    val interestPercentage: Int = 0,
    val interestLevel: String = "",
    val aptitudeScore: Int = 0,
    val aptitudePercentage: Int = 0,
    val aptitudeLevel: String = "",
    val overallPercentage: Int = 0
)
