package com.example.app.learningpath

import com.example.app.skillgap.SkillGapResult

class LearningPathRepository(
    private val engine: LearningPathEngine =
        LearningPathEngine()
) {

    fun createFromSkillGap(
        skillGapResult: SkillGapResult
    ): LearningPathResult {

        return engine.createFromSkillGap(
            skillGapResult
        )
    }

    fun createPath(
        input: LearningCareerInput
    ): LearningPathResult {

        return engine.createPath(
            input
        )
    }

    fun updateTaskCompletion(
        result: LearningPathResult,
        taskId: String,
        completed: Boolean
    ): LearningPathResult {

        val updatedTasks =
            result.tasks.map { task ->

                if (task.id == taskId) {
                    task.copy(
                        completed = completed
                    )
                } else {
                    task
                }
            }

        val updatedMilestones =
            result.milestones.map { milestone ->

                val milestoneCompleted =
                    milestone.requiredTaskIds
                        .all { requiredTaskId ->

                            updatedTasks.any {
                                it.id == requiredTaskId &&
                                    it.completed
                            }
                        }

                milestone.copy(
                    completed =
                        milestoneCompleted
                )
            }

        val updatedCheckpoints =
            result.checkpoints.map { checkpoint ->

                val checkpointCompleted =
                    checkpoint.afterTaskIds
                        .all { requiredTaskId ->

                            updatedTasks.any {
                                it.id == requiredTaskId &&
                                    it.completed
                            }
                        }

                checkpoint.copy(
                    completed =
                        checkpointCompleted
                )
            }

        val completedCount =
            updatedTasks.count {
                it.completed
            }

        val progress =
            if (updatedTasks.isEmpty()) {
                0.0
            } else {
                (
                    completedCount.toDouble() /
                        updatedTasks.size.toDouble()
                    ) * 100.0
            }

        val readiness =
            (
                (
                    100.0 -
                        result.tasks
                            .count {
                                !it.completed
                            }
                            .toDouble()
                            .coerceAtMost(100.0)
                ) * 0.40
                    +
                    progress * 0.60
                ).coerceIn(
                    0.0,
                    100.0
                )

        return result.copy(
            tasks = updatedTasks,
            milestones = updatedMilestones,
            checkpoints = updatedCheckpoints,
            completedTaskCount = completedCount,
            progressPercentage = progress,
            careerReadinessPercentage = readiness
        )
    }
}
