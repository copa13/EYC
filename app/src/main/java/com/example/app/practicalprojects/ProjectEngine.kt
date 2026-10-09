package com.example.app.practicalprojects

class ProjectEngine {

    fun recommendProjects(
        careerCode: String,
        currentSkills: List<String>,
        projects: List<PracticalProject>
    ): List<ProjectRecommendation> {
        val skillSet = currentSkills.map(::normalize).toSet()

        return projects
            .filter { it.careerCode == careerCode }
            .map { project ->
                val matched = project.requiredSkills.filter { normalize(it) in skillSet }
                val missing = project.requiredSkills.filter { normalize(it) !in skillSet }

                ProjectRecommendation(
                    projectId = project.id,
                    title = project.title,
                    difficulty = project.difficulty,
                    matchedSkills = matched,
                    missingSkills = missing,
                    reason = when {
                        missing.isEmpty() -> "Your current skills match the listed requirements."
                        matched.isEmpty() -> "This project can help you build foundational skills."
                        else -> "This project applies existing skills and provides practice for missing skills."
                    }
                )
            }
            .sortedWith(
                compareByDescending<ProjectRecommendation> { it.matchedSkills.size }
                    .thenBy { it.missingSkills.size }
                    .thenBy { it.difficulty.ordinal }
            )
    }

    fun updateTask(
        project: PracticalProject,
        taskId: String,
        status: ProjectTaskStatus
    ): PracticalProject {
        val updatedTasks = project.tasks.map { task ->
            if (task.id == taskId) task.copy(status = status) else task
        }

        val updatedMilestones = project.milestones.map { milestone ->
            val done = milestone.taskIds.isNotEmpty() &&
                milestone.taskIds.all { id ->
                    updatedTasks.any { it.id == id && it.completed }
                }
            milestone.copy(completed = done)
        }

        return project.copy(tasks = updatedTasks, milestones = updatedMilestones)
    }

    fun calculateProgress(project: PracticalProject): ProjectProgress {
        val total = project.tasks.size
        val completed = project.tasks.count { it.completed }
        val percentage = if (total == 0) 0 else completed * 100 / total

        val completedSkills = project.tasks
            .filter { it.completed }
            .flatMap { it.requiredSkills }
            .distinct()

        val remainingSkills = project.requiredSkills.filter { skill ->
            completedSkills.none { normalize(it) == normalize(skill) }
        }.distinct()

        val status = when {
            total > 0 && completed == total -> ProjectStatus.COMPLETED
            completed > 0 -> ProjectStatus.IN_PROGRESS
            else -> ProjectStatus.NOT_STARTED
        }

        return ProjectProgress(
            projectId = project.id,
            completedTasks = completed,
            totalTasks = total,
            completionPercentage = percentage,
            status = status,
            completedSkills = completedSkills,
            remainingSkills = remainingSkills,
            completedMilestones = project.milestones.count { it.completed },
            totalMilestones = project.milestones.size
        )
    }

    fun submitEvidence(
        project: PracticalProject,
        evidence: ProjectEvidence
    ): PracticalProject {
        require(evidence.projectId == project.id) {
            "Evidence project ID must match the project."
        }

        return project.copy(
            evidence = evidence.copy(status = EvidenceStatus.SUBMITTED)
        )
    }

    fun evaluateProject(
        project: PracticalProject,
        evaluation: ProjectEvaluation
    ): PracticalProject {
        require(evaluation.projectId == project.id) {
            "Evaluation project ID must match the project."
        }

        return project.copy(
            evaluation = evaluation.copy(score = evaluation.score.coerceIn(0, 100))
        )
    }

    fun calculateReadiness(
        projects: List<PracticalProject>
    ): PracticalReadiness {
        if (projects.isEmpty()) return PracticalReadiness(0, 0, 0, 0)

        val progress = projects.map(::calculateProgress)
        val completion = progress.map { it.completionPercentage }.average().toInt()

        val evaluations = projects.mapNotNull { it.evaluation }
        val evaluationScore = if (evaluations.isEmpty()) 0
            else evaluations.map { it.score.coerceIn(0, 100) }.average().toInt()

        val allSkills = projects.flatMap { it.requiredSkills }
            .map(::normalize).distinct()
        val completedSkills = progress.flatMap { it.completedSkills }
            .map(::normalize).distinct()

        val coverage = if (allSkills.isEmpty()) 0
            else completedSkills.count { it in allSkills } * 100 / allSkills.size

        val readiness = if (evaluations.isEmpty()) {
            (completion * 0.5 + coverage * 0.5).toInt()
        } else {
            (completion * 0.35 + evaluationScore * 0.35 + coverage * 0.30).toInt()
        }

        return PracticalReadiness(
            projectCompletion = completion.coerceIn(0, 100),
            evaluatedProjectScore = evaluationScore.coerceIn(0, 100),
            skillCoverage = coverage.coerceIn(0, 100),
            readinessScore = readiness.coerceIn(0, 100)
        )
    }

    fun createResult(
        projects: List<PracticalProject>,
        careerCode: String,
        currentSkills: List<String>
    ): ProjectProgressResult {
        val progress = projects.map(::calculateProgress)
        val evaluations = projects.mapNotNull { it.evaluation }
        val recommendations = recommendProjects(careerCode, currentSkills, projects)
        val readiness = calculateReadiness(projects)

        val explanation = when {
            projects.isEmpty() -> "No practical projects have been added yet."
            readiness.readinessScore >= 80 -> "Your recorded project progress indicates strong practical progress."
            readiness.readinessScore >= 50 -> "You have made progress; continue completing tasks and reviewing missing skills."
            else -> "Start with a suitable beginner project and complete its tasks step by step."
        }

        return ProjectProgressResult(
            projects = projects,
            progress = progress,
            evaluations = evaluations,
            recommendations = recommendations,
            overallReadiness = readiness,
            explanation = explanation
        )
    }

    private fun normalize(value: String): String =
        value.trim().lowercase()
            .replace("-", " ")
            .replace("_", " ")
            .replace(Regex("\\s+"), " ")
}
