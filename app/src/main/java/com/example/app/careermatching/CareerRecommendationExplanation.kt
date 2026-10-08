package com.example.app.careermatching

data class CareerRecommendationExplanation(
    val careerCode: String,
    val careerTitle: String,
    val overallScore: Int,
    val recommendationReason: String,

    val skillMatchScore: Int,
    val aptitudeMatchScore: Int,
    val interestMatchScore: Int,
    val knowledgeMatchScore: Int,
    val workStyleMatchScore: Int,

    val educationCompatible: Boolean,

    val matchedSkills: List<String>,
    val missingSkills: List<String>,

    val strengths: List<String>,
    val improvementAreas: List<String>
)
