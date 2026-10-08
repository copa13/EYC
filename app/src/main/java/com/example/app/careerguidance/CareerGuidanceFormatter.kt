package com.example.app.careerguidance

class CareerGuidanceFormatter {

    fun format(guidance: CareerGuidance): String {

        val output = StringBuilder()

        output.appendLine("=== EYC CAREER GUIDANCE ===")
        output.appendLine()

        guidance.bestCareer?.let {
            output.appendLine("BEST CAREER")
            output.appendLine("${it.careerTitle} — ${it.score}/100")
            output.appendLine()
        }

        output.appendLine("RECOMMENDATION SUMMARY")
        output.appendLine(guidance.recommendationSummary)
        output.appendLine()

        output.appendLine("WHY THIS CAREER")
        output.appendLine(guidance.whyThisCareer)
        output.appendLine()

        output.appendLine("CAREER STRENGTHS")
        appendList(
            output,
            guidance.strengths
        )

        output.appendLine()
        output.appendLine("IMPROVEMENT AREAS")
        appendList(
            output,
            guidance.improvementAreas
        )

        output.appendLine()
        output.appendLine("MISSING SKILLS → WHAT TO LEARN")
        appendList(
            output,
            guidance.missingSkillsToLearn
        )

        output.appendLine()
        output.appendLine("EDUCATION / QUALIFICATION GUIDANCE")
        appendList(
            output,
            guidance.educationGuidance
        )

        output.appendLine()
        output.appendLine("CAREER PREPARATION")
        appendList(
            output,
            guidance.careerPreparation
        )

        output.appendLine()
        output.appendLine("CAREER READINESS")
        output.appendLine(
            "${guidance.careerReadiness.score}/100 — ${guidance.careerReadiness.level}"
        )
        output.appendLine(
            guidance.careerReadiness.message
        )

        output.appendLine()
        output.appendLine("READINESS STRENGTHS")
        appendList(
            output,
            guidance.careerReadiness.strengths
        )

        output.appendLine()
        output.appendLine("READINESS GAPS")
        appendList(
            output,
            guidance.careerReadiness.gaps
        )

        output.appendLine()
        output.appendLine("NEXT STEPS")
        appendList(
            output,
            guidance.nextSteps
        )

        output.appendLine()
        output.appendLine("ALTERNATIVE CAREERS")

        if (guidance.alternativeCareers.isEmpty()) {
            output.appendLine("No alternative career was generated.")
        } else {
            guidance.alternativeCareers.forEach {
                output.appendLine(
                    "${it.rank}. ${it.careerTitle} — ${it.score}/100"
                )
            }
        }

        output.appendLine()
        output.appendLine("PROFILE PERSONALIZATION")
        appendList(
            output,
            guidance.profilePersonalization
        )

        output.appendLine()
        output.appendLine("ASSESSMENT PERSONALIZATION")
        appendList(
            output,
            guidance.assessmentPersonalization
        )

        output.appendLine()
        output.appendLine("SKILLS PERSONALIZATION")
        appendList(
            output,
            guidance.skillsPersonalization
        )

        return output.toString()
    }

    private fun appendList(
        output: StringBuilder,
        values: List<String>
    ) {

        if (values.isEmpty()) {
            output.appendLine("No data available.")
            return
        }

        values.forEach {
            output.appendLine("• $it")
        }
    }
}
