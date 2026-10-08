package com.example.app.careermatching

data class CareerScore(
    val careerCode: String,
    val careerTitle: String,
    val rawScore: Double,
    val score: Int,
    val rank: Int,
    val isBestMatch: Boolean
)
