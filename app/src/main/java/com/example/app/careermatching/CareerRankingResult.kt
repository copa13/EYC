package com.example.app.careermatching

data class CareerRankingResult(
    val studentId: String,
    val rankedCareers: List<CareerScore>
) {
    val bestMatch: CareerScore?
        get() = rankedCareers.firstOrNull()
}
