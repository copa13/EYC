package com.example.app.careermatching

data class CareerExplanationResult(
    val studentId: String,
    val explanations: List<CareerRecommendationExplanation>
) {

    val bestMatch: CareerRecommendationExplanation?
        get() = explanations.firstOrNull()
}
