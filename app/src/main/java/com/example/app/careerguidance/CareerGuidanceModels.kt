package com.example.app.careerguidance

data class CareerGuidance(
    val studentId: String,
    val bestCareer: CareerGuidanceCareer?,
    val alternativeCareers: List<CareerGuidanceCareer>,
    val recommendationSummary: String,
    val whyThisCareer: String,
    val strengths: List<String>,
    val improvementAreas: List<String>,
    val missingSkillsToLearn: List<String>,
    val educationGuidance: List<String>,
    val careerPreparation: List<String>,
    val careerReadiness: CareerReadiness,
    val nextSteps: List<String>,
    val profilePersonalization: List<String>,
    val assessmentPersonalization: List<String>,
    val skillsPersonalization: List<String>
)

data class CareerGuidanceCareer(
    val careerCode: String,
    val careerTitle: String,
    val score: Int,
    val rank: Int
)

data class CareerReadiness(
    val score: Int,
    val level: String,
    val strengths: List<String>,
    val gaps: List<String>,
    val message: String
)
