package com.example.app.careerguidance

data class CareerProfileInput(
    val name: String = "",
    val age: Int = 0,
    val education: String = "",
    val courseTrade: String = "",
    val educationLevel: String = "",
    val interests: List<String> = emptyList(),
    val preferredCareerAreas: List<String> = emptyList(),
    val learningPreferences: List<String> = emptyList(),
    val currentCareerGoal: String = "",
    val experienceProjects: List<String> = emptyList(),
    val languagesKnown: List<String> = emptyList(),
    val selfConfidenceLevel: String = ""
)
