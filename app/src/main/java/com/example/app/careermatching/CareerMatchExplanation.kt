package com.example.app.careermatching

data class CareerMatchExplanation(
    val careerCode: String,
    val careerTitle: String,
    val score: Int,

    val summary: String,
    val whyThisCareer: String,

    val matchedSkillsExplanation: String,
    val matchedInterestsExplanation: String,
    val aptitudeExplanation: String,
    val knowledgeExplanation: String,
    val workStyleExplanation: String,
    val educationExplanation: String,

    val strengths: List<String>,
    val improvementAreas: List<String>
)
