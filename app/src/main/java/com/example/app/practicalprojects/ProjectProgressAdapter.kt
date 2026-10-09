package com.example.app.practicalprojects

data class LearningPathProjectInput(
    val careerCode: String,
    val careerTitle: String,
    val skills: List<String>,
    val projects: List<String>
)

class ProjectProgressAdapter {

    fun createProjectsFromLearningPath(
        input: LearningPathProjectInput
    ): List<PracticalProject> {
        val skills = input.skills.distinct()

        return input.projects.mapIndexed { index, title ->
            val projectId = "${input.careerCode}_project_${index + 1}"

            val tasks = skills.mapIndexed { skillIndex, skill ->
                ProjectTask(
                    id = "${projectId}_task_${skillIndex + 1}",
                    title = "Practice $skill",
                    description = "Complete a practical task using $skill.",
                    requiredSkills = listOf(skill)
                )
            }

            val milestones = tasks.chunked(3).mapIndexed { milestoneIndex, group ->
                ProjectMilestone(
                    id = "${projectId}_milestone_${milestoneIndex + 1}",
                    title = "Milestone ${milestoneIndex + 1}",
                    taskIds = group.map { it.id }
                )
            }

            val difficulty = when (index) {
                0 -> ProjectDifficulty.BEGINNER
                1 -> ProjectDifficulty.INTERMEDIATE
                else -> ProjectDifficulty.ADVANCED
            }

            PracticalProject(
                id = projectId,
                careerCode = input.careerCode,
                title = title,
                description = "Practical project for ${input.careerTitle}.",
                difficulty = difficulty,
                requiredSkills = skills,
                objectives = listOf(
                    "Apply career-related skills",
                    "Complete the practical tasks",
                    "Demonstrate the project's learning outcomes"
                ),
                requirements = listOf(
                    "Review the project objectives",
                    "Complete the listed tasks",
                    "Submit project evidence for review"
                ),
                tasks = tasks,
                milestones = milestones
            )
        }
    }
}
