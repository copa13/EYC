package com.example.app.practicalprojects

class ProjectRepository(
    private val engine: ProjectEngine = ProjectEngine()
) {
    private val projects = linkedMapOf<String, PracticalProject>()

    @Synchronized
    fun addProject(project: PracticalProject) {
        require(project.id.isNotBlank()) { "Project ID cannot be blank." }
        projects[project.id] = project
    }

    @Synchronized
    fun addProjects(items: List<PracticalProject>) {
        items.forEach(::addProject)
    }

    @Synchronized
    fun getProjects(): List<PracticalProject> = projects.values.toList()

    @Synchronized
    fun getProject(projectId: String): PracticalProject? = projects[projectId]

    @Synchronized
    fun updateTask(
        projectId: String,
        taskId: String,
        status: ProjectTaskStatus
    ): PracticalProject? {
        val project = projects[projectId] ?: return null
        if (project.tasks.none { it.id == taskId }) return null

        val updated = engine.updateTask(project, taskId, status)
        projects[projectId] = updated
        return updated
    }

    @Synchronized
    fun submitEvidence(
        projectId: String,
        evidence: ProjectEvidence
    ): PracticalProject? {
        val project = projects[projectId] ?: return null
        val updated = engine.submitEvidence(project, evidence)
        projects[projectId] = updated
        return updated
    }

    @Synchronized
    fun evaluateProject(
        projectId: String,
        evaluation: ProjectEvaluation
    ): PracticalProject? {
        val project = projects[projectId] ?: return null
        val updated = engine.evaluateProject(project, evaluation)
        projects[projectId] = updated
        return updated
    }

    @Synchronized
    fun getResult(
        careerCode: String,
        currentSkills: List<String>
    ): ProjectProgressResult =
        engine.createResult(getProjects(), careerCode, currentSkills)
}
