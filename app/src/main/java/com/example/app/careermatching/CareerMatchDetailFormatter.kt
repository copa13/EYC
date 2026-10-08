package com.example.app.careermatching

class CareerMatchDetailFormatter {

    fun format(detail: CareerMatchDetail): String {

        val output = StringBuilder()

        output.appendLine("CAREER MATCH")
        output.appendLine("${detail.careerTitle} — ${detail.score}/100")
        output.appendLine("Rank: ${detail.rank}")
        output.appendLine()

        output.appendLine("MATCHED SKILLS")
        appendList(output, detail.matchedSkills)

        output.appendLine()
        output.appendLine("MISSING SKILLS")
        appendList(output, detail.missingSkills)

        output.appendLine()
        output.appendLine("MATCHED INTERESTS")
        appendList(output, detail.matchedInterests)

        output.appendLine()
        output.appendLine("APTITUDE MATCH")
        appendList(output, detail.aptitudeMatches)

        output.appendLine()
        output.appendLine("KNOWLEDGE MATCH")
        appendList(output, detail.knowledgeMatches)

        output.appendLine()
        output.appendLine("WORK-STYLE MATCH")
        appendList(output, detail.workStyleMatches)

        output.appendLine()
        output.appendLine("SCORES")
        output.appendLine("Skills: ${detail.skillScore}/100")
        output.appendLine("Interests: ${detail.interestScore}/100")
        output.appendLine("Aptitude: ${detail.aptitudeScore}/100")
        output.appendLine("Knowledge: ${detail.knowledgeScore}/100")
        output.appendLine("Work Style: ${detail.workStyleScore}/100")

        output.appendLine()
        output.appendLine("EDUCATION")
        output.appendLine(detail.educationExplanation)

        output.appendLine()
        output.appendLine("EXPLANATION")
        output.appendLine(detail.studentFriendlyExplanation)

        return output.toString()
    }

    private fun appendList(
        output: StringBuilder,
        values: List<String>
    ) {
        if (values.isEmpty()) {
            output.appendLine("No specific matches identified.")
            return
        }

        values.forEach {
            output.appendLine("• $it")
        }
    }
}
