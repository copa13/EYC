package com.example.app.careermatching

class CareerMatchEngineV2 {

    fun createDetails(
        input: EycCareerInput,
        candidates: List<CareerCandidate>,
        ranking: CareerRankingResult
    ): CareerMatchComparison {

        val candidateMap =
            candidates.associateBy { it.code }

        val details =
            ranking.rankedCareers.map { ranked ->

                val candidate =
                    candidateMap[ranked.careerCode]

                createDetail(
                    input = input,
                    candidate = candidate,
                    ranked = ranked
                )
            }

        return CareerMatchComparison(
            careers = details
        )
    }

    private fun createDetail(
        input: EycCareerInput,
        candidate: CareerCandidate?,
        ranked: CareerScore
    ): CareerMatchDetail {

        if (candidate == null) {

            return CareerMatchDetail(
                careerCode = ranked.careerCode,
                careerTitle = ranked.careerTitle,
                rank = ranked.rank,
                score = ranked.score,

                matchedSkills = emptyList(),
                missingSkills = emptyList(),

                matchedInterests = emptyList(),
                aptitudeMatches = emptyList(),
                knowledgeMatches = emptyList(),
                workStyleMatches = emptyList(),

                educationCompatible = false,

                skillScore = 0,
                interestScore = 0,
                aptitudeScore = 0,
                knowledgeScore = 0,
                workStyleScore = 0,

                educationExplanation =
                    "Education compatibility could not be calculated.",

                studentFriendlyExplanation =
                    "Detailed information is not available for this career."
            )
        }

        val matchedInterests =
            findMatchedValues(
                input.interests,
                candidate.interestSimilarity
            )

        val aptitudeMatches =
            findMatchedValues(
                input.aptitude,
                candidate.aptitudeSimilarity
            )

        val knowledgeMatches =
            findMatchedValues(
                input.knowledge,
                candidate.knowledgeSimilarity
            )

        val workStyleMatches =
            findMatchedValues(
                input.workStyles,
                candidate.workStyleSimilarity
            )

        val skillScore =
            toPercent(candidate.skillSimilarity)

        val interestScore =
            toPercent(candidate.interestSimilarity)

        val aptitudeScore =
            toPercent(candidate.aptitudeSimilarity)

        val knowledgeScore =
            toPercent(candidate.knowledgeSimilarity)

        val workStyleScore =
            toPercent(candidate.workStyleSimilarity)

        val educationExplanation =
            if (candidate.educationCompatible) {
                "Your current education background is compatible with this career."
            } else {
                "Your current education background may require additional qualification or training for this career."
            }

        val explanation =
            buildStudentExplanation(
                careerTitle = candidate.title,
                skillScore = skillScore,
                interestScore = interestScore,
                aptitudeScore = aptitudeScore,
                knowledgeScore = knowledgeScore,
                workStyleScore = workStyleScore,
                educationCompatible = candidate.educationCompatible,
                matchedSkills = candidate.matchedSkills
            )

        return CareerMatchDetail(
            careerCode = candidate.code,
            careerTitle = candidate.title,
            rank = ranked.rank,
            score = ranked.score,

            matchedSkills =
                candidate.matchedSkills.distinct(),

            missingSkills =
                candidate.missingSkills.distinct(),

            matchedInterests =
                matchedInterests,

            aptitudeMatches =
                aptitudeMatches,

            knowledgeMatches =
                knowledgeMatches,

            workStyleMatches =
                workStyleMatches,

            educationCompatible =
                candidate.educationCompatible,

            skillScore =
                skillScore,

            interestScore =
                interestScore,

            aptitudeScore =
                aptitudeScore,

            knowledgeScore =
                knowledgeScore,

            workStyleScore =
                workStyleScore,

            educationExplanation =
                educationExplanation,

            studentFriendlyExplanation =
                explanation
        )
    }

    private fun findMatchedValues(
        values: List<String>,
        similarity: Double
    ): List<String> {

        if (values.isEmpty()) {
            return emptyList()
        }

        if (similarity <= 0.0) {
            return emptyList()
        }

        return values
            .filter { it.isNotBlank() }
            .distinct()
            .take(8)
    }

    private fun buildStudentExplanation(
        careerTitle: String,
        skillScore: Int,
        interestScore: Int,
        aptitudeScore: Int,
        knowledgeScore: Int,
        workStyleScore: Int,
        educationCompatible: Boolean,
        matchedSkills: List<String>
    ): String {

        val reasons = mutableListOf<String>()

        if (skillScore >= 60) {
            reasons.add("your skills fit the career requirements")
        }

        if (interestScore >= 60) {
            reasons.add("your interests are aligned with the career")
        }

        if (aptitudeScore >= 60) {
            reasons.add("your aptitude profile supports the career")
        }

        if (knowledgeScore >= 60) {
            reasons.add("your existing knowledge is relevant")
        }

        if (workStyleScore >= 60) {
            reasons.add("your work-style profile is compatible")
        }

        if (educationCompatible) {
            reasons.add("your education background is compatible")
        }

        if (matchedSkills.isNotEmpty()) {
            reasons.add(
                "you already have ${matchedSkills.size} matched skill area(s)"
            )
        }

        if (reasons.isEmpty()) {
            return "$careerTitle is currently a potential career match based on the available EYC data."
        }

        return "$careerTitle is recommended because ${reasons.joinToString(", ")}."
    }

    private fun toPercent(
        value: Double
    ): Int {
        return (value * 100.0)
            .coerceIn(0.0, 100.0)
            .toInt()
    }
}
