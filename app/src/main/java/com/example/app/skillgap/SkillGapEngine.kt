package com.example.app.skillgap

import kotlin.math.roundToInt

class SkillGapEngine {

    fun analyze(input: SkillGapInput): SkillGapResult {

        val requiredSkills = normalizeSkills(input.requiredSkills)
        val currentSkills = normalizeSkills(input.currentSkills)

        if (requiredSkills.isEmpty()) {
            return SkillGapResult(
                studentId = input.studentId,
                careerCode = input.careerCode,
                careerTitle = input.careerTitle,
                requiredSkills = emptyList(),
                currentSkills = currentSkills,
                matchedSkills = emptyList(),
                missingSkills = emptyList(),
                partialSkills = emptyList(),
                skillGapScore = 0.0,
                skillGapPercentage = 0.0,
                criticalSkills = emptyList(),
                improvementAreas = emptyList(),
                skillItems = emptyList(),
                explanation = "No required skills are available for this career.",
                learningPathSkills = emptyList()
            )
        }

        val matchedSkills = mutableListOf<String>()
        val missingSkills = mutableListOf<String>()
        val partialSkills = mutableListOf<String>()
        val skillItems = mutableListOf<SkillGapItem>()

        for (requiredSkill in requiredSkills) {

            val exactMatch = currentSkills.any {
                skillsMatch(it, requiredSkill)
            }

            if (exactMatch) {

                matchedSkills.add(requiredSkill)

                skillItems.add(
                    SkillGapItem(
                        skill = requiredSkill,
                        status = SkillGapStatus.MATCHED,
                        priority = SkillPriority.LOW,
                        matchPercentage = 100.0,
                        explanation = "The student already has this skill."
                    )
                )

                continue
            }

            val partialMatch = currentSkills.firstOrNull {
                partialSkillsMatch(it, requiredSkill)
            }

            if (partialMatch != null) {

                partialSkills.add(requiredSkill)

                skillItems.add(
                    SkillGapItem(
                        skill = requiredSkill,
                        status = SkillGapStatus.PARTIAL,
                        priority = SkillPriority.MEDIUM,
                        matchPercentage = 50.0,
                        explanation =
                            "The student has a related skill: $partialMatch."
                    )
                )

                continue
            }

            missingSkills.add(requiredSkill)

            skillItems.add(
                SkillGapItem(
                    skill = requiredSkill,
                    status = SkillGapStatus.MISSING,
                    priority = SkillPriority.HIGH,
                    matchPercentage = 0.0,
                    explanation =
                        "This skill is required for the selected career but is not present in the student's current skills."
                )
            )
        }

        val criticalSkills = identifyCriticalSkills(
            requiredSkills = requiredSkills,
            missingSkills = missingSkills,
            partialSkills = partialSkills
        )

        val improvementAreas = buildImprovementAreas(
            missingSkills = missingSkills,
            partialSkills = partialSkills
        )

        val skillGapPercentage =
            calculateGapPercentage(
                requiredSkills = requiredSkills,
                missingSkills = missingSkills,
                partialSkills = partialSkills
            )

        val skillGapScore =
            (100.0 - skillGapPercentage).coerceIn(0.0, 100.0)

        val explanation =
            buildExplanation(
                careerTitle = input.careerTitle,
                matchedSkills = matchedSkills,
                missingSkills = missingSkills,
                partialSkills = partialSkills,
                skillGapPercentage = skillGapPercentage
            )

        val learningPathSkills =
            buildLearningPathSkills(
                criticalSkills = criticalSkills,
                missingSkills = missingSkills,
                partialSkills = partialSkills
            )

        return SkillGapResult(
            studentId = input.studentId,
            careerCode = input.careerCode,
            careerTitle = input.careerTitle,
            requiredSkills = requiredSkills,
            currentSkills = currentSkills,
            matchedSkills = matchedSkills,
            missingSkills = missingSkills,
            partialSkills = partialSkills,
            skillGapScore = round(skillGapScore),
            skillGapPercentage = round(skillGapPercentage),
            criticalSkills = criticalSkills,
            improvementAreas = improvementAreas,
            skillItems = skillItems,
            explanation = explanation,
            learningPathSkills = learningPathSkills
        )
    }

    fun analyzeCareers(
        studentId: String,
        careers: List<SkillGapInput>
    ): SkillGapComparison {

        if (careers.isEmpty()) {
            return SkillGapComparison(
                studentId = studentId,
                bestCareer = null,
                alternativeCareers = emptyList()
            )
        }

        val results = careers.map {
            analyze(it)
        }

        val ranked = results.sortedWith(
            compareBy<SkillGapResult> {
                it.skillGapPercentage
            }.thenByDescending {
                it.skillGapScore
            }
        )

        val careerResults = ranked.map {
            CareerSkillGap(
                careerCode = it.careerCode,
                careerTitle = it.careerTitle,
                skillGapPercentage = it.skillGapPercentage,
                missingSkills = it.missingSkills,
                criticalSkills = it.criticalSkills
            )
        }

        return SkillGapComparison(
            studentId = studentId,
            bestCareer = careerResults.firstOrNull(),
            alternativeCareers = careerResults.drop(1)
        )
    }

    private fun normalizeSkills(
        skills: List<String>
    ): List<String> {

        return skills
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .distinctBy { normalize(it) }
    }

    private fun normalize(value: String): String {
        return value
            .lowercase()
            .replace("&", "and")
            .replace("-", " ")
            .replace("_", " ")
            .replace("/", " ")
            .replace(Regex("\\s+"), " ")
            .trim()
    }

    private fun skillsMatch(
        studentSkill: String,
        requiredSkill: String
    ): Boolean {

        val student = normalize(studentSkill)
        val required = normalize(requiredSkill)

        if (student == required) {
            return true
        }

        val aliases = skillAliases(required)

        return aliases.any {
            normalize(it) == student
        }
    }

    private fun partialSkillsMatch(
        studentSkill: String,
        requiredSkill: String
    ): Boolean {

        val student = normalize(studentSkill)
        val required = normalize(requiredSkill)

        if (student == required) {
            return false
        }

        if (student.contains(required) || required.contains(student)) {
            return true
        }

        val studentWords =
            student.split(" ").filter { it.length > 2 }.toSet()

        val requiredWords =
            required.split(" ").filter { it.length > 2 }.toSet()

        if (studentWords.isEmpty() || requiredWords.isEmpty()) {
            return false
        }

        val commonWords =
            studentWords.intersect(requiredWords)

        return commonWords.isNotEmpty()
    }

    private fun skillAliases(
        skill: String
    ): List<String> {

        return when (normalize(skill)) {

            "android development",
            "android app development" ->
                listOf(
                    "android",
                    "android development",
                    "android app development",
                    "mobile app development"
                )

            "machine learning" ->
                listOf(
                    "machine learning",
                    "ml",
                    "artificial intelligence",
                    "ai"
                )

            "artificial intelligence" ->
                listOf(
                    "artificial intelligence",
                    "ai",
                    "machine learning",
                    "ml"
                )

            "database management" ->
                listOf(
                    "database",
                    "database management",
                    "sql",
                    "dbms"
                )

            "web development" ->
                listOf(
                    "web development",
                    "web",
                    "frontend",
                    "backend",
                    "full stack development"
                )

            "problem solving" ->
                listOf(
                    "problem solving",
                    "problem-solving",
                    "logical reasoning"
                )

            "data analysis" ->
                listOf(
                    "data analysis",
                    "data analytics",
                    "analytics"
                )

            "software testing" ->
                listOf(
                    "software testing",
                    "testing",
                    "qa",
                    "quality assurance"
                )

            else ->
                listOf(skill)
        }
    }

    private fun calculateGapPercentage(
        requiredSkills: List<String>,
        missingSkills: List<String>,
        partialSkills: List<String>
    ): Double {

        if (requiredSkills.isEmpty()) {
            return 0.0
        }

        val weightedGap =
            missingSkills.size +
                (partialSkills.size * 0.5)

        return (weightedGap / requiredSkills.size) * 100.0
    }

    private fun identifyCriticalSkills(
        requiredSkills: List<String>,
        missingSkills: List<String>,
        partialSkills: List<String>
    ): List<String> {

        if (requiredSkills.isEmpty()) {
            return emptyList()
        }

        val criticalThreshold =
            (requiredSkills.size * 0.30)
                .roundToInt()
                .coerceAtLeast(1)

        val criticalMissing =
            missingSkills.take(criticalThreshold)

        if (criticalMissing.isNotEmpty()) {
            return criticalMissing
        }

        return partialSkills.take(criticalThreshold)
    }

    private fun buildImprovementAreas(
        missingSkills: List<String>,
        partialSkills: List<String>
    ): List<String> {

        val result = mutableListOf<String>()

        missingSkills.forEach {
            result.add("Learn $it")
        }

        partialSkills.forEach {
            result.add("Improve $it")
        }

        return result.distinct()
    }

    private fun buildLearningPathSkills(
        criticalSkills: List<String>,
        missingSkills: List<String>,
        partialSkills: List<String>
    ): List<String> {

        return (
            criticalSkills +
                missingSkills +
                partialSkills
            )
            .distinct()
    }

    private fun buildExplanation(
        careerTitle: String,
        matchedSkills: List<String>,
        missingSkills: List<String>,
        partialSkills: List<String>,
        skillGapPercentage: Double
    ): String {

        return buildString {

            append("Skill-gap analysis for $careerTitle. ")

            if (matchedSkills.isNotEmpty()) {
                append(
                    "Matched skills: ${matchedSkills.joinToString(", ")}. "
                )
            }

            if (partialSkills.isNotEmpty()) {
                append(
                    "Partial skills: ${partialSkills.joinToString(", ")}. "
                )
            }

            if (missingSkills.isNotEmpty()) {
                append(
                    "Missing skills: ${missingSkills.joinToString(", ")}. "
                )
            }

            append(
                "Overall skill gap is ${round(skillGapPercentage)}%."
            )
        }
    }

    private fun round(
        value: Double
    ): Double {
        return (value * 100.0).roundToInt() / 100.0
    }
}
