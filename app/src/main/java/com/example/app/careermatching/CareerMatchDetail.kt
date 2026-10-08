package com.example.app.careermatching

data class CareerMatchDetail(
    val careerCode: String,
    val careerTitle: String,
    val rank: Int,
    val score: Int,

    val matchedSkills: List<String>,
    val missingSkills: List<String>,

    val matchedInterests: List<String>,
    val aptitudeMatches: List<String>,
    val knowledgeMatches: List<String>,
    val workStyleMatches: List<String>,

    val educationCompatible: Boolean,

    val skillScore: Int,
    val interestScore: Int,
    val aptitudeScore: Int,
    val knowledgeScore: Int,
    val workStyleScore: Int,

    val educationExplanation: String,
    val studentFriendlyExplanation: String
)
