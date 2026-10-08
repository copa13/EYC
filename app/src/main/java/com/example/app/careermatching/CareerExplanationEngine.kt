package com.example.app.careermatching

import kotlin.math.roundToInt

class CareerExplanationEngine {

    fun explain(
        studentId: String,
        candidates: List<CareerCandidate>,
        rankedScores: List<CareerScore>
    ): CareerExplanationResult {

        if (candidates.isEmpty()) {
            return CareerExplanationResult(
                studentId = studentId,
                explanations = emptyList()
            )
        }

        val scoreMap = rankedScores.associateBy {
            it.careerCode
        }

        val candidateMap = candidates.associateBy {
            it.code
        }

        val explanations = rankedScores.mapNotNull { careerScore ->

            val candidate = candidateMap[careerScore.careerCode]
                ?: return@mapNotNull null

            createExplanation(
                candidate = candidate,
                careerScore = careerScore
            )
        }

        return CareerExplanationResult(
            studentId = studentId,
            explanations = explanations
                .sortedBy { scoreMap[it.careerCode]?.rank ?: Int.MAX_VALUE }
        )
    }

    private fun createExplanation(
        candidate: CareerCandidate,
        careerScore: CareerScore
    ): CareerRecommendationExplanation {

        val skillScore = toPercent(
            candidate.skillSimilarity
        )

        val aptitudeScore = toPercent(
            candidate.aptitudeSimilarity
        )

        val interestScore = toPercent(
            candidate.interestSimilarity
        )

        val knowledgeScore = toPercent(
            candidate.knowledgeSimilarity
        )

        val workStyleScore = toPercent(
            candidate.workStyleSimilarity
        )

        val matchedSkills = cleanList(
            candidate.matchedSkills
        )

        val missingSkills = cleanList(
            candidate.missingSkills
        )

        val strengths = buildStrengths(
            skillScore = skillScore,
            aptitudeScore = aptitudeScore,
            interestScore = interestScore,
            knowledgeScore = knowledgeScore,
            workStyleScore = workStyleScore,
            educationCompatible = candidate.educationCompatible,
            matchedSkills = matchedSkills
        )

        val improvementAreas = buildImprovementAreas(
            skillScore = skillScore,
            aptitudeScore = aptitudeScore,
            interestScore = interestScore,
            knowledgeScore = knowledgeScore,
            workStyleScore = workStyleScore,
            educationCompatible = candidate.educationCompatible,
            missingSkills = missingSkills
        )

        val reason = buildRecommendationReason(
            careerTitle = candidate.title,
            score = careerScore.score,
            skillScore = skillScore,
            aptitudeScore = aptitudeScore,
            interestScore = interestScore,
            knowledgeScore = knowledgeScore,
            workStyleScore = workStyleScore,
            educationCompatible = candidate.educationCompatible,
            matchedSkills = matchedSkills,
            missingSkills = missingSkills
        )

        return CareerRecommendationExplanation(
            careerCode = candidate.code,
            careerTitle = candidate.title,
            overallScore = careerScore.score,
            recommendationReason = reason,

            skillMatchScore = skillScore,
            aptitudeMatchScore = aptitudeScore,
            interestMatchScore = interestScore,
            knowledgeMatchScore = knowledgeScore,
            workStyleMatchScore = workStyleScore,

            educationCompatible = candidate.educationCompatible,

            matchedSkills = matchedSkills,
            missingSkills = missingSkills,

            strengths = strengths,
            improvementAreas = improvementAreas
        )
    }

    private fun buildRecommendationReason(
        careerTitle: String,
        score: Int,
        skillScore: Int,
        aptitudeScore: Int,
        interestScore: Int,
        knowledgeScore: Int,
        workStyleScore: Int,
        educationCompatible: Boolean,
        matchedSkills: List<String>,
        missingSkills: List<String>
    ): String {

        val reason = StringBuilder()

        when {
            score >= 80 -> {
                reason.append(
                    "$careerTitle is a very strong match for your current profile."
                )
            }

            score >= 60 -> {
                reason.append(
                    "$careerTitle is a good match for your current profile."
                )
            }

            score >= 40 -> {
                reason.append(
                    "$careerTitle has a moderate match with your current profile."
                )
            }

            else -> {
                reason.append(
                    "$careerTitle currently has a lower match with your profile."
                )
            }
        }

        val strongestFactor = strongestFactor(
            skillScore = skillScore,
            aptitudeScore = aptitudeScore,
            interestScore = interestScore,
            knowledgeScore = knowledgeScore,
            workStyleScore = workStyleScore
        )

        if (strongestFactor != null) {
            reason.append(
                " The strongest matching factor is $strongestFactor."
            )
        }

        if (matchedSkills.isNotEmpty()) {
            reason.append(
                " ${matchedSkills.size} relevant skill(s) already match the career requirements."
            )
        }

        if (missingSkills.isNotEmpty()) {
            reason.append(
                " ${missingSkills.size} skill area(s) can still be improved."
            )
        }

        if (educationCompatible) {
            reason.append(
                " Your current education level is compatible with this career."
            )
        } else {
            reason.append(
                " The education requirement for this career needs further review."
            )
        }

        return reason.toString()
    }

    private fun strongestFactor(
        skillScore: Int,
        aptitudeScore: Int,
        interestScore: Int,
        knowledgeScore: Int,
        workStyleScore: Int
    ): String? {

        val factors = listOf(
            "skills" to skillScore,
            "aptitude" to aptitudeScore,
            "interests" to interestScore,
            "knowledge" to knowledgeScore,
            "work style" to workStyleScore
        )

        return factors
            .maxByOrNull { it.second }
            ?.takeIf { it.second > 0 }
            ?.first
    }

    private fun buildStrengths(
        skillScore: Int,
        aptitudeScore: Int,
        interestScore: Int,
        knowledgeScore: Int,
        workStyleScore: Int,
        educationCompatible: Boolean,
        matchedSkills: List<String>
    ): List<String> {

        val strengths = mutableListOf<String>()

        if (skillScore >= 70) {
            strengths.add(
                "Your current skills strongly align with this career."
            )
        } else if (skillScore >= 50) {
            strengths.add(
                "Your current skills provide a useful foundation."
            )
        }

        if (aptitudeScore >= 70) {
            strengths.add(
                "Your aptitude profile strongly supports this career."
            )
        } else if (aptitudeScore >= 50) {
            strengths.add(
                "Your aptitude shows useful alignment with this career."
            )
        }

        if (interestScore >= 70) {
            strengths.add(
                "Your interests strongly align with this career."
            )
        } else if (interestScore >= 50) {
            strengths.add(
                "Your interests show a reasonable connection with this career."
            )
        }

        if (knowledgeScore >= 70) {
            strengths.add(
                "Your existing knowledge is highly relevant."
            )
        } else if (knowledgeScore >= 50) {
            strengths.add(
                "Your existing knowledge provides a useful foundation."
            )
        }

        if (workStyleScore >= 70) {
            strengths.add(
                "Your work-style profile is strongly compatible."
            )
        } else if (workStyleScore >= 50) {
            strengths.add(
                "Your work-style preferences show useful compatibility."
            )
        }

        if (educationCompatible) {
            strengths.add(
                "Your education level is compatible with the career."
            )
        }

        if (matchedSkills.isNotEmpty()) {
            strengths.add(
                "${matchedSkills.size} required skill(s) are already matched."
            )
        }

        if (strengths.isEmpty()) {
            strengths.add(
                "More profile development may improve the career match."
            )
        }

        return strengths.distinct()
    }

    private fun buildImprovementAreas(
        skillScore: Int,
        aptitudeScore: Int,
        interestScore: Int,
        knowledgeScore: Int,
        workStyleScore: Int,
        educationCompatible: Boolean,
        missingSkills: List<String>
    ): List<String> {

        val improvements = mutableListOf<String>()

        if (missingSkills.isNotEmpty()) {
            missingSkills.forEach { skill ->
                improvements.add(
                    "Develop $skill."
                )
            }
        }

        if (skillScore < 50) {
            improvements.add(
                "Strengthen the skills required for this career."
            )
        }

        if (aptitudeScore < 50) {
            improvements.add(
                "Improve the aptitude areas related to this career."
            )
        }

        if (interestScore < 50) {
            improvements.add(
                "Explore whether this career genuinely matches your interests."
            )
        }

        if (knowledgeScore < 50) {
            improvements.add(
                "Build more knowledge in the career's subject areas."
            )
        }

        if (workStyleScore < 50) {
            improvements.add(
                "Review the work environment and style expected by this career."
            )
        }

        if (!educationCompatible) {
            improvements.add(
                "Review the education or qualification requirements."
            )
        }

        if (improvements.isEmpty()) {
            improvements.add(
                "Continue developing your current skills and knowledge."
            )
        }

        return improvements.distinct()
    }

    private fun cleanList(
        values: List<String>
    ): List<String> {

        return values
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .distinct()
    }

    private fun toPercent(
        value: Double
    ): Int {

        return (value * 100.0)
            .coerceIn(0.0, 100.0)
            .roundToInt()
    }
}
