package com.example.app.practicalprojects

enum class ProjectDifficulty { BEGINNER, INTERMEDIATE, ADVANCED }
enum class ProjectStatus { NOT_STARTED, IN_PROGRESS, COMPLETED }

data class PracticalProject(
    val id: String,
    val careerCode: String,
    val title: String,
    val description: String,
    val difficulty: ProjectDifficulty,
    val requiredSkills: List<String>,
    val objectives: List<String>,
    val tasks: List<ProjectTask>,
    val milestones: List<ProjectMilestone>
)

data class ProjectTask(
    val id: String,
    val title: String,
    val description: String,
    val requiredSkills: List<String>,
    val completed: Boolean = false
)

data class ProjectMilestone(
    val id: String,
    val title: String,
    val taskIds: List<String>,
    val completed: Boolean = false
)

data class ProjectEvidence(
    val projectId: String,
    val title: String,
    val description: String,
    val submitted: Boolean = false
)

data class ProjectEvaluation(
    val projectId: String,
    val score: Int,
    val feedback: String,
    val strengths: List<String>,
    val weaknesses: List<String>,
    val improvedSkills: List<String>
)

data class ProjectProgress(
    val projectId: String,
    val completedTasks: Int,
    val totalTasks: Int,
    val completionPercentage: Int,
    val status: ProjectStatus,
    val completedSkills: List<String>,
    val remainingSkills: List<String>
)

data class PracticalReadiness(
    val projectCompletion: Int,
    val evaluatedProjectScore: Int,
    val skillCoverage: Int,
    val readinessScore: Int
)

data class ProjectRecommendation(
    val projectId: String,
    val title: String,
    val difficulty: ProjectDifficulty,
    val matchedSkills: List<String>,
    val missingSkills: List<String>,
    val reason: String
)

data class ProjectProgressResult(
    val projects: List<PracticalProject>,
    val progress: List<ProjectProgress>,
    val evaluations: List<ProjectEvaluation>,
    val recommendations: List<ProjectRecommendation>,
    val overallReadiness: PracticalReadiness
)
