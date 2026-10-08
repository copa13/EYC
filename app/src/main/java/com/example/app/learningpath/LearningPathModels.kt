package com.example.app.learningpath

enum class LearningLevel {
    BEGINNER,
    INTERMEDIATE,
    ADVANCED
}

enum class LearningPriority {
    CRITICAL,
    HIGH,
    MEDIUM,
    LOW
}

enum class LearningItemType {
    FOUNDATION,
    SKILL,
    PRACTICE,
    PROJECT,
    ASSESSMENT,
    REASSESSMENT
}

data class LearningResource(
    val title: String,
    val description: String,
    val resourceType: String,
    val skill: String,
    val url: String? = null
)

data class LearningTask(
    val id: String,
    val title: String,
    val description: String,
    val skill: String,
    val level: LearningLevel,
    val priority: LearningPriority,
    val itemType: LearningItemType,
    val estimatedHours: Int,
    val resources: List<LearningResource> = emptyList(),
    val completed: Boolean = false
)

data class LearningMilestone(
    val id: String,
    val title: String,
    val description: String,
    val order: Int,
    val requiredTaskIds: List<String>,
    val completed: Boolean = false
)

data class LearningCheckpoint(
    val id: String,
    val title: String,
    val description: String,
    val afterTaskIds: List<String>,
    val completed: Boolean = false
)

data class LearningCareerInput(
    val studentId: String,
    val careerCode: String,
    val careerTitle: String,
    val missingSkills: List<String>,
    val partialSkills: List<String>,
    val criticalSkills: List<String>,
    val currentSkills: List<String>,
    val skillGapPercentage: Double
)

data class LearningPathResult(
    val studentId: String,
    val careerCode: String,
    val careerTitle: String,

    val careerGoal: String,

    val requiredLearningSkills: List<String>,
    val criticalSkills: List<String>,
    val prioritizedSkills: List<String>,

    val learningSequence: List<String>,

    val tasks: List<LearningTask>,
    val milestones: List<LearningMilestone>,
    val checkpoints: List<LearningCheckpoint>,

    val beginnerTasks: List<LearningTask>,
    val intermediateTasks: List<LearningTask>,
    val advancedTasks: List<LearningTask>,

    val alternativePath: List<String>,

    val totalEstimatedHours: Int,

    val completedTaskCount: Int,
    val totalTaskCount: Int,

    val progressPercentage: Double,
    val careerReadinessPercentage: Double,

    val explanation: String
)
