package com.example.app.learningpath

import com.example.app.skillgap.SkillGapResult
import kotlin.math.roundToInt

class LearningPathEngine {

    fun createPath(
        input: LearningCareerInput
    ): LearningPathResult {

        val prioritizedSkills =
            prioritizeSkills(
                criticalSkills = input.criticalSkills,
                missingSkills = input.missingSkills,
                partialSkills = input.partialSkills
            )

        val learningSequence =
            createLearningSequence(
                prioritizedSkills
            )

        val tasks =
            createTasks(
                skills = learningSequence,
                partialSkills = input.partialSkills
            )

        val milestones =
            createMilestones(tasks)

        val checkpoints =
            createCheckpoints(tasks)

        val beginnerTasks =
            tasks.filter {
                it.level == LearningLevel.BEGINNER
            }

        val intermediateTasks =
            tasks.filter {
                it.level == LearningLevel.INTERMEDIATE
            }

        val advancedTasks =
            tasks.filter {
                it.level == LearningLevel.ADVANCED
            }

        val totalHours =
            tasks.sumOf {
                it.estimatedHours
            }

        val progress =
            calculateProgress(tasks)

        val readiness =
            calculateReadiness(
                skillGapPercentage = input.skillGapPercentage,
                progressPercentage = progress
            )

        val alternativePath =
            createAlternativePath(
                prioritizedSkills
            )

        val explanation =
            createExplanation(
                input = input,
                prioritizedSkills = prioritizedSkills,
                tasks = tasks
            )

        return LearningPathResult(
            studentId = input.studentId,
            careerCode = input.careerCode,
            careerTitle = input.careerTitle,

            careerGoal = input.careerTitle,

            requiredLearningSkills = prioritizedSkills,
            criticalSkills = input.criticalSkills,
            prioritizedSkills = prioritizedSkills,

            learningSequence = learningSequence,

            tasks = tasks,
            milestones = milestones,
            checkpoints = checkpoints,

            beginnerTasks = beginnerTasks,
            intermediateTasks = intermediateTasks,
            advancedTasks = advancedTasks,

            alternativePath = alternativePath,

            totalEstimatedHours = totalHours,

            completedTaskCount =
                tasks.count { it.completed },

            totalTaskCount =
                tasks.size,

            progressPercentage = progress,

            careerReadinessPercentage = readiness,

            explanation = explanation
        )
    }

    fun createFromSkillGap(
        skillGapResult: SkillGapResult
    ): LearningPathResult {

        val input =
            LearningCareerInput(
                studentId = skillGapResult.studentId,
                careerCode = skillGapResult.careerCode,
                careerTitle = skillGapResult.careerTitle,

                missingSkills =
                    skillGapResult.missingSkills,

                partialSkills =
                    skillGapResult.partialSkills,

                criticalSkills =
                    skillGapResult.criticalSkills,

                currentSkills =
                    skillGapResult.currentSkills,

                skillGapPercentage =
                    skillGapResult.skillGapPercentage
            )

        return createPath(input)
    }

    private fun prioritizeSkills(
        criticalSkills: List<String>,
        missingSkills: List<String>,
        partialSkills: List<String>
    ): List<String> {

        val result =
            mutableListOf<String>()

        criticalSkills.forEach {
            addUnique(result, it)
        }

        missingSkills.forEach {
            addUnique(result, it)
        }

        partialSkills.forEach {
            addUnique(result, it)
        }

        return result
    }

    private fun addUnique(
        list: MutableList<String>,
        value: String
    ) {

        if (
            value.isNotBlank() &&
            list.none {
                normalize(it) == normalize(value)
            }
        ) {
            list.add(value.trim())
        }
    }

    private fun createLearningSequence(
        prioritizedSkills: List<String>
    ): List<String> {

        if (prioritizedSkills.isEmpty()) {
            return emptyList()
        }

        val foundation =
            prioritizedSkills.filter {
                isFoundationSkill(it)
            }

        val normal =
            prioritizedSkills.filter {
                !isFoundationSkill(it)
            }

        return (
            foundation +
                normal
            ).distinctBy {
                normalize(it)
            }
    }

    private fun createTasks(
        skills: List<String>,
        partialSkills: List<String>
    ): List<LearningTask> {

        val tasks =
            mutableListOf<LearningTask>()

        var counter = 1

        skills.forEach { skill ->

            val partial =
                partialSkills.any {
                    normalize(it) == normalize(skill)
                }

            val baseLevel =
                if (partial) {
                    LearningLevel.INTERMEDIATE
                } else {
                    LearningLevel.BEGINNER
                }

            tasks.add(
                LearningTask(
                    id = "LP-$counter",
                    title = "Learn $skill",
                    description =
                        "Build the required foundation and practical understanding of $skill.",
                    skill = skill,
                    level = baseLevel,
                    priority =
                        if (partial) {
                            LearningPriority.MEDIUM
                        } else {
                            LearningPriority.HIGH
                        },
                    itemType = LearningItemType.SKILL,
                    estimatedHours =
                        if (partial) 8 else 12,
                    resources =
                        createResources(skill)
                )
            )

            counter++

            tasks.add(
                LearningTask(
                    id = "LP-$counter",
                    title = "Practice $skill",
                    description =
                        "Complete practical exercises to improve your ability in $skill.",
                    skill = skill,
                    level =
                        nextLevel(baseLevel),
                    priority =
                        if (partial) {
                            LearningPriority.MEDIUM
                        } else {
                            LearningPriority.HIGH
                        },
                    itemType = LearningItemType.PRACTICE,
                    estimatedHours = 6,
                    resources =
                        createPracticeResources(skill)
                )
            )

            counter++

            tasks.add(
                LearningTask(
                    id = "LP-$counter",
                    title = "Build a $skill Project",
                    description =
                        "Apply $skill through a practical project related to the selected career.",
                    skill = skill,
                    level =
                        nextLevel(
                            nextLevel(baseLevel)
                        ),
                    priority = LearningPriority.HIGH,
                    itemType = LearningItemType.PROJECT,
                    estimatedHours = 15,
                    resources =
                        createProjectResources(skill)
                )
            )

            counter++
        }

        if (tasks.isNotEmpty()) {

            tasks.add(
                LearningTask(
                    id = "LP-$counter",
                    title = "Career Skill Assessment",
                    description =
                        "Evaluate the skills learned so far before career readiness reassessment.",
                    skill = "Career Readiness",
                    level = LearningLevel.ADVANCED,
                    priority = LearningPriority.HIGH,
                    itemType = LearningItemType.ASSESSMENT,
                    estimatedHours = 2
                )
            )

            counter++

            tasks.add(
                LearningTask(
                    id = "LP-$counter",
                    title = "Career Re-Assessment",
                    description =
                        "Re-assess skills and readiness after completing the learning pathway.",
                    skill = "Career Readiness",
                    level = LearningLevel.ADVANCED,
                    priority = LearningPriority.CRITICAL,
                    itemType = LearningItemType.REASSESSMENT,
                    estimatedHours = 2
                )
            )
        }

        return tasks
    }

    private fun createResources(
        skill: String
    ): List<LearningResource> {

        return listOf(
            LearningResource(
                title = "$skill Fundamentals",
                description =
                    "Study the fundamentals required to begin learning $skill.",
                resourceType = "Learning",
                skill = skill
            )
        )
    }

    private fun createPracticeResources(
        skill: String
    ): List<LearningResource> {

        return listOf(
            LearningResource(
                title = "$skill Practice",
                description =
                    "Practice real-world exercises related to $skill.",
                resourceType = "Practice",
                skill = skill
            )
        )
    }

    private fun createProjectResources(
        skill: String
    ): List<LearningResource> {

        return listOf(
            LearningResource(
                title = "$skill Project Ideas",
                description =
                    "Use practical projects to demonstrate $skill.",
                resourceType = "Project",
                skill = skill
            )
        )
    }

    private fun nextLevel(
        level: LearningLevel
    ): LearningLevel {

        return when (level) {

            LearningLevel.BEGINNER ->
                LearningLevel.INTERMEDIATE

            LearningLevel.INTERMEDIATE ->
                LearningLevel.ADVANCED

            LearningLevel.ADVANCED ->
                LearningLevel.ADVANCED
        }
    }

    private fun createMilestones(
        tasks: List<LearningTask>
    ): List<LearningMilestone> {

        if (tasks.isEmpty()) {
            return emptyList()
        }

        val groups =
            tasks.chunked(
                size = 6
            )

        return groups.mapIndexed { index, group ->

            LearningMilestone(
                id = "M-${index + 1}",
                title =
                    "Learning Milestone ${index + 1}",
                description =
                    "Complete the learning tasks in this stage.",
                order = index + 1,
                requiredTaskIds =
                    group.map {
                        it.id
                    }
            )
        }
    }

    private fun createCheckpoints(
        tasks: List<LearningTask>
    ): List<LearningCheckpoint> {

        if (tasks.isEmpty()) {
            return emptyList()
        }

        val checkpoints =
            mutableListOf<LearningCheckpoint>()

        tasks
            .filterIndexed { index, _ ->
                (index + 1) % 5 == 0
            }
            .forEachIndexed { index, task ->

                checkpoints.add(
                    LearningCheckpoint(
                        id = "C-${index + 1}",
                        title =
                            "Progress Checkpoint ${index + 1}",
                        description =
                            "Review progress after completing this stage.",
                        afterTaskIds =
                            listOf(task.id)
                    )
                )
            }

        return checkpoints
    }

    private fun calculateProgress(
        tasks: List<LearningTask>
    ): Double {

        if (tasks.isEmpty()) {
            return 0.0
        }

        val completed =
            tasks.count {
                it.completed
            }

        return round(
            completed.toDouble() /
                tasks.size.toDouble() *
                100.0
        )
    }

    private fun calculateReadiness(
        skillGapPercentage: Double,
        progressPercentage: Double
    ): Double {

        val skillCoverage =
            100.0 -
                skillGapPercentage.coerceIn(
                    0.0,
                    100.0
                )

        return round(
            (
                skillCoverage * 0.60
                    +
                    progressPercentage * 0.40
                ).coerceIn(
                    0.0,
                    100.0
                )
        )
    }

    private fun createAlternativePath(
        skills: List<String>
    ): List<String> {

        if (skills.isEmpty()) {
            return emptyList()
        }

        return skills
            .asReversed()
            .take(
                minOf(
                    5,
                    skills.size
                )
            )
    }

    private fun createExplanation(
        input: LearningCareerInput,
        prioritizedSkills: List<String>,
        tasks: List<LearningTask>
    ): String {

        return buildString {

            append(
                "Learning pathway created for ${input.careerTitle}. "
            )

            if (input.criticalSkills.isNotEmpty()) {

                append(
                    "Critical skills are prioritized first: "
                )

                append(
                    input.criticalSkills.joinToString(", ")
                )

                append(". ")
            }

            if (prioritizedSkills.isNotEmpty()) {

                append(
                    "The pathway follows a progressive learning sequence "
                )

                append(
                    "from fundamentals to practice and projects. "
                )
            }

            append(
                "The pathway contains ${tasks.size} learning tasks."
            )
        }
    }

    private fun isFoundationSkill(
        skill: String
    ): Boolean {

        val value =
            normalize(skill)

        val foundations =
            listOf(
                "computer fundamentals",
                "programming",
                "problem solving",
                "logical reasoning",
                "digital literacy",
                "communication",
                "database",
                "database management"
            )

        return foundations.any {
            value.contains(it)
        }
    }

    private fun normalize(
        value: String
    ): String {

        return value
            .lowercase()
            .replace(
                Regex("[^a-z0-9 ]"),
                " "
            )
            .replace(
                Regex("\\s+"),
                " "
            )
            .trim()
    }

    private fun round(
        value: Double
    ): Double {

        return (
            value * 100.0
        ).roundToInt() / 100.0
    }
}
