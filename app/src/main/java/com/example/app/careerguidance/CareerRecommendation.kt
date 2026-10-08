package com.example.app.careerguidance

data class CareerRecommendation(
    val careerName: String = "",
    val matchPercentage: Int = 0,
    val reason: String = "",
    val strengths: List<String> = emptyList(),
    val importantSkills: List<String> = emptyList()
)

data class CareerPredictionResult(
    val summary: String = "",
    val recommendations: List<CareerRecommendation> = emptyList()
)
