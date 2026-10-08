package com.example.app.careermatching

data class CareerMatchComparison(
    val careers: List<CareerMatchDetail>
) {
    val bestMatch: CareerMatchDetail?
        get() = careers.firstOrNull()

    val alternatives: List<CareerMatchDetail>
        get() = careers.drop(1)
}
