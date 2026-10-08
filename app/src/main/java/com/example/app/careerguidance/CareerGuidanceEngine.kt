package com.example.app.careerguidance

import com.example.app.careermatching.CareerCandidate
import com.example.app.careermatching.CareerExplanationResult
import com.example.app.careermatching.CareerRankingResult
import com.example.app.careermatching.EycCareerInput
import kotlin.math.roundToInt

class CareerGuidanceEngine {

    fun buildGuidance(
        input: EycCareerInput,
        candidates: List<CareerCandidate>,
        rankingResult: CareerRankingResult,
        explanationResult: CareerExplanationResult
    ): CareerGuidance {

        val rankedCareers = rankingResult.rankedCareers

        val bestRankedCareer = rankedCareers.firstOrNull()

        val bestExplanation =
            explanationResult.bestMatch

        val bestCandidate =
            bestRankedCareer?.let { ranked ->
                candidates.firstOrNull {
                    it.code == ranked.careerCode
                }
            }

        val bestCareer =
            bestRankedCareer?.let {
                CareerGuidanceCareer(
                    careerCode = it.careerCode,
                    careerTitle = it.careerTitle,
                    score = it.score,
                    rank = it.rank
                )
            }

        val alternatives =
            rankedCareers
                .drop(1)
                .map {
                    CareerGuidanceCareer(
                        careerCode = it.careerCode,
                        careerTitle = it.careerTitle,
                        score = it.score,
                        rank = it.rank
                    )
                }

        val strengths =
            buildStrengths(
                input = input,
                candidate = bestCandidate
            )

        val improvementAreas =
            buildImprovementAreas(
                input = input,
                candidate = bestCandidate
            )

        val missingSkills =
            bestExplanation
                ?.missingSkills
                ?.distinct()
                ?: bestCandidate
                    ?.missingSkills
                    ?.distinct()
                    ?: emptyList()

        val educationGuidance =
            buildEducationGuidance(
                input = input,
                candidate = bestCandidate
            )

        val preparation =
            buildCareerPreparation(
                careerTitle = bestCareer?.careerTitle,
                missingSkills = missingSkills
            )

        val readiness =
            calculateReadiness(
                input = input,
                candidate = bestCandidate,
                careerScore = bestRankedCareer?.score ?: 0,
                missingSkills = missingSkills
            )

        val nextSteps =
            buildNextSteps(
                career = bestCareer,
                missingSkills = missingSkills,
                readiness = readiness
            )

        val profilePersonalization =
            buildProfilePersonalization(input)

        val assessmentPersonalization =
            buildAssessmentPersonalization(input)

        val skillsPersonalization =
            buildSkillsPersonalization(
                input = input,
                candidate = bestCandidate
            )

        val summary =
            buildRecommendationSummary(
                bestCareer = bestCareer,
                alternatives = alternatives,
                readiness = readiness
            )

        val whyCareer =
            buildWhyCareer(
                bestCareer = bestCareer,
                candidate = bestCandidate,
                explanation = bestExplanation
            )

        return CareerGuidance(
            studentId = input.studentId,
            bestCareer = bestCareer,
            alternativeCareers = alternatives,
            recommendationSummary = summary,
            whyThisCareer = whyCareer,
            strengths = strengths,
            improvementAreas = improvementAreas,
            missingSkillsToLearn = missingSkills,
            educationGuidance = educationGuidance,
            careerPreparation = preparation,
            careerReadiness = readiness,
            nextSteps = nextSteps,
            profilePersonalization = profilePersonalization,
            assessmentPersonalization = assessmentPersonalization,
            skillsPersonalization = skillsPersonalization
        )
    }

    private fun buildRecommendationSummary(
        bestCareer: CareerGuidanceCareer?,
        alternatives: List<CareerGuidanceCareer>,
        readiness: CareerReadiness
    ): String {

        if (bestCareer == null) {
            return "No suitable career could be identified from the current student data."
        }

        return if (alternatives.isEmpty()) {
            "The current best career match is ${bestCareer.careerTitle} with a ${bestCareer.score}/100 match score. Current readiness is ${readiness.score}/100."
        } else {
            "The strongest career match is ${bestCareer.careerTitle} with a ${bestCareer.score}/100 score. ${alternatives.size} alternative career option(s) are also available."
        }
    }

    private fun buildWhyCareer(
        bestCareer: CareerGuidanceCareer?,
        candidate: CareerCandidate?,
        explanation: com.example.app.careermatching.CareerRecommendationExplanation?
    ): String {

        if (bestCareer == null) {
            return "A career explanation is not available because no career candidate was generated."
        }

        val parts = mutableListOf<String>()

        explanation?.let {
            if (it.skillMatchScore >= 60) {
                parts.add("your current skills match the career requirements")
            }

            if (it.interestMatchScore >= 60) {
                parts.add("your interests align with the career")
            }

            if (it.aptitudeMatchScore >= 60) {
                parts.add("your aptitude profile supports the career")
            }

            if (it.knowledgeMatchScore >= 60) {
                parts.add("your knowledge profile is relevant")
            }

            if (it.workStyleMatchScore >= 60) {
                parts.add("your work-style preferences fit the career")
            }

            if (it.educationCompatible) {
                parts.add("your education background is compatible")
            }
        }

        candidate?.let {
            if (it.matchedSkills.isNotEmpty()) {
                parts.add(
                    "you already match ${it.matchedSkills.size} relevant skill area(s)"
                )
            }
        }

        return if (parts.isEmpty()) {
            "${bestCareer.careerTitle} is currently the highest-ranked career based on the combined EYC matching and scoring results."
        } else {
            "${bestCareer.careerTitle} is recommended because ${parts.joinToString(", ")}."
        }
    }

    private fun buildStrengths(
        input: EycCareerInput,
        candidate: CareerCandidate?
    ): List<String> {

        val result = mutableListOf<String>()

        candidate?.matchedSkills
            ?.take(5)
            ?.forEach {
                result.add("Existing strength: $it")
            }

        if (candidate != null &&
            candidate.interestSimilarity >= 0.60
        ) {
            result.add("Strong interest alignment")
        }

        if (candidate != null &&
            candidate.aptitudeSimilarity >= 0.60
        ) {
            result.add("Strong aptitude alignment")
        }

        if (candidate != null &&
            candidate.knowledgeSimilarity >= 0.60
        ) {
            result.add("Relevant knowledge foundation")
        }

        if (candidate != null &&
            candidate.workStyleSimilarity >= 0.60
        ) {
            result.add("Compatible work-style profile")
        }

        if (input.skills.isNotEmpty() && result.isEmpty()) {
            result.add("Student has already identified relevant skills")
        }

        return result.distinct().take(8)
    }

    private fun buildImprovementAreas(
        input: EycCareerInput,
        candidate: CareerCandidate?
    ): List<String> {

        val result = mutableListOf<String>()

        candidate?.missingSkills
            ?.take(8)
            ?.forEach {
                result.add("Improve: $it")
            }

        if (candidate != null &&
            candidate.aptitudeSimilarity < 0.50
        ) {
            result.add("Strengthen aptitude-related abilities")
        }

        if (candidate != null &&
            candidate.interestSimilarity < 0.50
        ) {
            result.add("Explore the career domain through practical activities")
        }

        if (candidate != null &&
            candidate.knowledgeSimilarity < 0.50
        ) {
            result.add("Build career-specific knowledge")
        }

        if (candidate != null &&
            candidate.workStyleSimilarity < 0.50
        ) {
            result.add("Develop work habits suited to the target career")
        }

        return result.distinct().take(10)
    }

    private fun buildEducationGuidance(
        input: EycCareerInput,
        candidate: CareerCandidate?
    ): List<String> {

        val result = mutableListOf<String>()

        val education =
            input.educationLevel
                .trim()
                .ifBlank { "current education level" }

        result.add(
            "Current education: $education."
        )

        if (candidate?.educationCompatible == true) {
            result.add(
                "Your current education level is compatible with this career match."
            )
        } else if (candidate != null) {
            result.add(
                "Check the target career's required qualification before committing to the path."
            )
        }

        if (input.courseOrTrade.isNotBlank()) {
            result.add(
                "Current course/trade: ${input.courseOrTrade}."
            )
        }

        result.add(
            "Use the missing-skill list to identify additional certifications, subjects, or practical training that may be useful."
        )

        return result
    }

    private fun buildCareerPreparation(
        careerTitle: String?,
        missingSkills: List<String>
    ): List<String> {

        val result = mutableListOf<String>()

        careerTitle?.let {
            result.add("Study the core requirements of $it.")
        }

        result.add(
            "Build practical projects related to the target career."
        )

        if (missingSkills.isNotEmpty()) {
            result.add(
                "Prioritize these missing skills: ${missingSkills.take(5).joinToString(", ")}."
            )
        }

        result.add(
            "Create evidence of your skills through projects, assignments, or portfolio work."
        )

        result.add(
            "Practice career-specific interview and problem-solving questions."
        )

        result.add(
            "Reassess your skills after completing the learning stages."
        )

        return result
    }

    private fun calculateReadiness(
        input: EycCareerInput,
        candidate: CareerCandidate?,
        careerScore: Int,
        missingSkills: List<String>
    ): CareerReadiness {

        if (candidate == null) {
            return CareerReadiness(
                score = 0,
                level = "Not Ready",
                strengths = emptyList(),
                gaps = listOf("No career candidate available"),
                message = "Complete the required EYC assessments first."
            )
        }

        val skill =
            candidate.skillSimilarity * 100.0

        val aptitude =
            candidate.aptitudeSimilarity * 100.0

        val interest =
            candidate.interestSimilarity * 100.0

        val knowledge =
            candidate.knowledgeSimilarity * 100.0

        val workStyle =
            candidate.workStyleSimilarity * 100.0

        val education =
            if (candidate.educationCompatible) 100.0 else 40.0

        val base =
            (
                skill * 0.35 +
                aptitude * 0.20 +
                interest * 0.15 +
                knowledge * 0.10 +
                workStyle * 0.05 +
                education * 0.05 +
                careerScore * 0.10
            )

        val missingPenalty =
            (missingSkills.size.coerceAtMost(10) * 1.5)

        val finalScore =
            (base - missingPenalty)
                .coerceIn(0.0, 100.0)
                .roundToInt()

        val level =
            when {
                finalScore >= 80 -> "Career Ready"
                finalScore >= 65 -> "Nearly Ready"
                finalScore >= 45 -> "Developing"
                else -> "Early Stage"
            }

        val strengths = mutableListOf<String>()
        val gaps = mutableListOf<String>()

        if (skill >= 60) {
            strengths.add("Skills")
        } else {
            gaps.add("Skills")
        }

        if (aptitude >= 60) {
            strengths.add("Aptitude")
        } else {
            gaps.add("Aptitude")
        }

        if (interest >= 60) {
            strengths.add("Interests")
        } else {
            gaps.add("Career interest alignment")
        }

        if (knowledge >= 60) {
            strengths.add("Knowledge")
        } else {
            gaps.add("Knowledge")
        }

        if (workStyle >= 60) {
            strengths.add("Work style")
        } else {
            gaps.add("Work style")
        }

        if (!candidate.educationCompatible) {
            gaps.add("Education compatibility")
        }

        if (missingSkills.isNotEmpty()) {
            gaps.add("Missing career skills")
        }

        val message =
            when {
                finalScore >= 80 ->
                    "You have a strong foundation for this career. Focus on practical experience and final preparation."

                finalScore >= 65 ->
                    "You are close to career readiness. Focus on the identified skill gaps and practical projects."

                finalScore >= 45 ->
                    "You have a developing foundation. Follow the learning path and reassess your progress."

                else ->
                    "Start with the recommended fundamentals and build your skills step by step."
            }

        return CareerReadiness(
            score = finalScore,
            level = level,
            strengths = strengths,
            gaps = gaps,
            message = message
        )
    }

    private fun buildNextSteps(
        career: CareerGuidanceCareer?,
        missingSkills: List<String>,
        readiness: CareerReadiness
    ): List<String> {

        val result = mutableListOf<String>()

        career?.let {
            result.add(
                "1. Confirm ${it.careerTitle} as your primary career target."
            )
        }

        if (missingSkills.isNotEmpty()) {
            result.add(
                "2. Start learning: ${missingSkills.take(3).joinToString(", ")}."
            )
        } else {
            result.add(
                "2. Continue strengthening your existing career skills."
            )
        }

        result.add(
            "3. Build at least one practical project related to the target career."
        )

        result.add(
            "4. Practice career-specific interview and problem-solving tasks."
        )

        result.add(
            "5. Reassess your skills and career readiness."
        )

        result.add(
            "6. Update your career plan using the new assessment results."
        )

        if (readiness.score >= 80) {
            result.add(
                "7. Start preparing for real-world opportunities such as internships, apprenticeships, or entry-level roles."
            )
        }

        return result
    }

    private fun buildProfilePersonalization(
        input: EycCareerInput
    ): List<String> {

        val result = mutableListOf<String>()

        if (input.educationLevel.isNotBlank()) {
            result.add(
                "Education level considered: ${input.educationLevel}."
            )
        }

        if (input.courseOrTrade.isNotBlank()) {
            result.add(
                "Course/trade considered: ${input.courseOrTrade}."
            )
        }

        if (input.interests.isNotEmpty()) {
            result.add(
                "Profile interests considered: ${input.interests.take(5).joinToString(", ")}."
            )
        }

        return result
    }

    private fun buildAssessmentPersonalization(
        input: EycCareerInput
    ): List<String> {

        val result = mutableListOf<String>()

        if (input.aptitude.isNotEmpty()) {
            result.add(
                "Aptitude assessment data was included in career matching."
            )
        }

        if (input.interests.isNotEmpty()) {
            result.add(
                "Interest assessment data was included in career matching."
            )
        }

        if (result.isEmpty()) {
            result.add(
                "Complete the interest and aptitude assessment for stronger personalization."
            )
        }

        return result
    }

    private fun buildSkillsPersonalization(
        input: EycCareerInput,
        candidate: CareerCandidate?
    ): List<String> {

        val result = mutableListOf<String>()

        if (input.skills.isNotEmpty()) {
            result.add(
                "Your selected skills were included in the career comparison."
            )
        }

        candidate?.matchedSkills
            ?.take(5)
            ?.let { matched ->
                if (matched.isNotEmpty()) {
                    result.add(
                        "Matched skill areas: ${matched.joinToString(", ")}."
                    )
                }
            }

        candidate?.missingSkills
            ?.take(5)
            ?.let { missing ->
                if (missing.isNotEmpty()) {
                    result.add(
                        "Priority skill gaps: ${missing.joinToString(", ")}."
                    )
                }
            }

        return result
    }
}
