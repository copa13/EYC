package com.example.app.careermatching

object CareerExplanationFormatter {

    fun format(
        explanation: CareerRecommendationExplanation
    ): String {

        val output = StringBuilder()

        output.appendLine(
            "Why ${explanation.careerTitle}?"
        )

        output.appendLine()

        output.appendLine(
            "Match Score: ${explanation.overallScore}/100"
        )

        output.appendLine()

        output.appendLine(
            explanation.recommendationReason
        )

        output.appendLine()

        output.appendLine("Match Breakdown:")

        output.appendLine(
            "Skills: ${explanation.skillMatchScore}%"
        )

        output.appendLine(
            "Aptitude: ${explanation.aptitudeMatchScore}%"
        )

        output.appendLine(
            "Interests: ${explanation.interestMatchScore}%"
        )

        output.appendLine(
            "Knowledge: ${explanation.knowledgeMatchScore}%"
        )

        output.appendLine(
            "Work Style: ${explanation.workStyleMatchScore}%"
        )

        output.appendLine(
            "Education: ${
                if (explanation.educationCompatible) {
                    "Compatible"
                } else {
                    "Needs Review"
                }
            }"
        )

        output.appendLine()

        if (explanation.strengths.isNotEmpty()) {

            output.appendLine("Your Strengths:")

            explanation.strengths.forEach { strength ->
                output.appendLine("• $strength")
            }

            output.appendLine()
        }

        if (explanation.matchedSkills.isNotEmpty()) {

            output.appendLine("Matched Skills:")

            explanation.matchedSkills.forEach { skill ->
                output.appendLine("• $skill")
            }

            output.appendLine()
        }

        if (explanation.missingSkills.isNotEmpty()) {

            output.appendLine("Skills to Improve:")

            explanation.missingSkills.forEach { skill ->
                output.appendLine("• $skill")
            }

            output.appendLine()
        }

        if (explanation.improvementAreas.isNotEmpty()) {

            output.appendLine("Improvement Areas:")

            explanation.improvementAreas.forEach { area ->
                output.appendLine("• $area")
            }
        }

        return output
            .toString()
            .trim()
    }
}
