package com.example.app.practicalprojects

data class ProjectReportData(
    val studentId: String,
    val careerCode: String,
    val totalProjects: Int,
    val completedProjects: Int,
    val completedTasks: Int,
    val totalTasks: Int,
    val completedMilestones: Int,
    val totalMilestones: Int,
    val averageEvaluationScore: Int,
    val demonstratedSkills: List<String>,
    val improvementSkills: List<String>,
    val projectCompletionPercentage: Int,
    val practicalReadinessPercentage: Int
)

object ProjectReportFormatter {

    fun format(data: ProjectReportData): String {
        val completion = data.projectCompletionPercentage.coerceIn(0, 100)
        val readiness = data.practicalReadinessPercentage.coerceIn(0, 100)
        val evaluation = data.averageEvaluationScore.coerceIn(0, 100)

        return buildString {
            appendLine("PRACTICAL PROJECT PROGRESS REPORT")
            appendLine("---------------------------------")
            appendLine("Student ID: ${data.studentId}")
            appendLine("Career: ${data.careerCode}")
            appendLine()

            appendLine("PROJECT SUMMARY")
            appendLine("Projects completed: ${data.completedProjects.coerceAtLeast(0)}")
            appendLine("Projects tracked: ${data.totalProjects.coerceAtLeast(0)}")
            appendLine()

            appendLine("TASK PROGRESS")
            appendLine(
                "Tasks completed: " +
                    "${data.completedTasks.coerceAtLeast(0)} / " +
                    "${data.totalTasks.coerceAtLeast(0)}"
            )
            appendLine("Task completion: ${taskPercentage(data)}%")
            appendLine()

            appendLine("MILESTONES")
            appendLine(
                "Completed: ${data.completedMilestones.coerceAtLeast(0)} / " +
                    "${data.totalMilestones.coerceAtLeast(0)}"
            )
            appendLine()

            appendLine("EVALUATION")
            appendLine("Average evaluation score: $evaluation / 100")
            appendLine()

            appendLine("DEMONSTRATED SKILLS")
            appendLine(formatSkills(data.demonstratedSkills))
            appendLine()

            appendLine("SKILLS TO IMPROVE")
            appendLine(formatSkills(data.improvementSkills))
            appendLine()

            appendLine("OVERALL PROGRESS")
            appendLine("Project completion: $completion%")
            appendLine("Practical readiness estimate: $readiness%")
            appendLine()

            appendLine("NOTE")
            appendLine(
                "The readiness percentage is an estimate based on supplied " +
                    "project data. It is not a guarantee of employment or " +
                    "professional competence."
            )
        }
    }

    private fun taskPercentage(data: ProjectReportData): Int {
        val total = data.totalTasks.coerceAtLeast(0)
        if (total == 0) return 0

        val completed = data.completedTasks.coerceIn(0, total)

        return (completed.toDouble() / total.toDouble() * 100.0).toInt()
    }

    private fun formatSkills(skills: List<String>): String {
        val cleaned = skills
            .map(String::trim)
            .filter(String::isNotBlank)
            .distinctBy { it.lowercase() }

        return if (cleaned.isEmpty()) {
            "No skill data recorded."
        } else {
            cleaned.joinToString(separator = "\n") { "- $it" }
        }
    }
}
