package com.example.app.careermatching

import kotlin.math.max
import kotlin.math.min
import kotlin.math.round

class CareerScoringEngine {

    companion object {
        private const val SKILL_WEIGHT = 0.38
        private const val APTITUDE_WEIGHT = 0.22
        private const val INTEREST_WEIGHT = 0.20
        private const val KNOWLEDGE_WEIGHT = 0.10
        private const val WORK_STYLE_WEIGHT = 0.05
        private const val EDUCATION_WEIGHT = 0.05
    }

    fun rank(
        studentId: String,
        candidates: List<CareerCandidate>
    ): CareerRankingResult {

        if (candidates.isEmpty()) {
            return CareerRankingResult(
                studentId = studentId,
                rankedCareers = emptyList()
            )
        }

        val scoredCandidates = candidates.map { candidate ->
            candidate to calculateRawScore(candidate)
        }

        val minRaw = scoredCandidates.minOf { it.second }
        val maxRaw = scoredCandidates.maxOf { it.second }

        val rankedCareers = scoredCandidates
            .sortedByDescending { it.second }
            .mapIndexed { index, pair ->

                val candidate = pair.first
                val rawScore = pair.second

                CareerScore(
                    careerCode = candidate.code,
                    careerTitle = candidate.title,
                    rawScore = rawScore,
                    score = normalizeScore(
                        rawScore = rawScore,
                        minRaw = minRaw,
                        maxRaw = maxRaw
                    ),
                    rank = index + 1,
                    isBestMatch = index == 0
                )
            }

        return CareerRankingResult(
            studentId = studentId,
            rankedCareers = rankedCareers
        )
    }

    private fun calculateRawScore(
        candidate: CareerCandidate
    ): Double {

        val skillScore = clamp(candidate.skillSimilarity)
        val aptitudeScore = clamp(candidate.aptitudeSimilarity)
        val interestScore = clamp(candidate.interestSimilarity)
        val knowledgeScore = clamp(candidate.knowledgeSimilarity)
        val workStyleScore = clamp(candidate.workStyleSimilarity)

        val educationScore =
            if (candidate.educationCompatible) {
                1.0
            } else {
                0.0
            }

        return (
            skillScore * SKILL_WEIGHT +
            aptitudeScore * APTITUDE_WEIGHT +
            interestScore * INTEREST_WEIGHT +
            knowledgeScore * KNOWLEDGE_WEIGHT +
            workStyleScore * WORK_STYLE_WEIGHT +
            educationScore * EDUCATION_WEIGHT
        )
    }

    private fun normalizeScore(
        rawScore: Double,
        minRaw: Double,
        maxRaw: Double
    ): Int {

        if (maxRaw <= minRaw) {
            return 100
        }

        val normalized =
            ((rawScore - minRaw) / (maxRaw - minRaw)) * 100.0

        return round(
            normalized.coerceIn(0.0, 100.0)
        ).toInt()
    }

    private fun clamp(value: Double): Double {
        return min(
            1.0,
            max(0.0, value)
        )
    }
}
