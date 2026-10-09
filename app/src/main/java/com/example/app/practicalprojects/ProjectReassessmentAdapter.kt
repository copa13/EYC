package com.example.app.practicalprojects

data class ProjectReassessmentInput(
    val studentId: String,
    val careerCode: String,
    val completedProjectIds: List<String>,
    val completedTasks: Int,
    val totalTasks: Int,
    val completedMilestones: Int,
    val totalMilestones: Int,
    val evaluationScores: List<Int>,
    val demonstratedSkills: List<String>,
    val improvementSkills: List<String>,
    val projectCompletionPercentage: Int,
    val practicalReadinessPercentage: Int
)

object ProjectReassessmentAdapter {

    fun createInput(
        studentId: String,
        careerCode: String,
        completedProjectIds: List<String>,
        completedTasks: Int,
        totalTasks: Int,
        completedMilestones: Int,
        totalMilestones: Int,
        evaluationScores: List<Int>,
        demonstratedSkills: List<String>,
        improvementSkills: List<String>,
        projectCompletionPercentage: Int,
        practicalReadinessPercentage: Int
    ): ProjectReassessmentInput {
        return ProjectReassessmentInput(
            studentId = studentId.trim(),
            careerCode = careerCode.trim(),
            completedProjectIds = completedProjectIds
                .map(String::trim)
                .filter(String::isNotBlank)
                .distinct(),
            completedTasks = completedTasks.coerceAtLeast(0),
            totalTasks = totalTasks.coerceAtLeast(0),
            completedMilestones = completedMilestones.coerceAtLeast(0),
            totalMilestones = totalMilestones.coerceAtLeast(0),
            evaluationScores = evaluationScores.map { it.coerceIn(0, 100) },
            demonstratedSkills = demonstratedSkills
                .map(String::trim)
                .filter(String::isNotBlank)
                .distinctBy { it.lowercase() },
            improvementSkills = improvementSkills
                .map(String::trim)
                .filter(String::isNotBlank)
                .distinctBy { it.lowercase() },
            projectCompletionPercentage =
                projectCompletionPercentage.coerceIn(0, 100),
            practicalReadinessPercentage =
                practicalReadinessPercentage.coerceIn(0, 100)
        )
    }

    fun calculateTaskCompletion(
        completedTasks: Int,
        totalTasks: Int
    ): Int {
        if (totalTasks <= 0) return 0

        return (
            completedTasks.coerceIn(0, totalTasks).toDouble() /
                totalTasks.toDouble() * 100.0
            ).toInt()
    }

    fun calculateAverageEvaluation(scores: List<Int>): Int {
        if (scores.isEmpty()) return 0

        return scores
            .map { it.coerceIn(0, 100) }
            .average()
            .toInt()
    }
}
