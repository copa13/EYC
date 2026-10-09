package com.example.app.practicalprojects

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class ProjectProgressUiState(
    val loading: Boolean = false,
    val result: ProjectProgressResult? = null,
    val error: String? = null
)

class ProjectProgressViewModel(
    private val repository: ProjectRepository = ProjectRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProjectProgressUiState())
    val uiState: StateFlow<ProjectProgressUiState> = _uiState.asStateFlow()

    fun loadProjects(
        projects: List<PracticalProject>,
        careerCode: String,
        currentSkills: List<String>
    ) {
        viewModelScope.launch {
            _uiState.value = ProjectProgressUiState(loading = true)

            try {
                val result = withContext(Dispatchers.Default) {
                    repository.addProjects(projects)
                    repository.getResult(careerCode, currentSkills)
                }
                _uiState.value = ProjectProgressUiState(result = result)
            } catch (e: Exception) {
                _uiState.value = ProjectProgressUiState(
                    error = e.message ?: "Unable to load projects."
                )
            }
        }
    }

    fun setTaskStatus(
        projectId: String,
        taskId: String,
        status: ProjectTaskStatus,
        careerCode: String,
        currentSkills: List<String>
    ) {
        viewModelScope.launch {
            try {
                val result = withContext(Dispatchers.Default) {
                    checkNotNull(repository.updateTask(projectId, taskId, status)) {
                        "Project or task not found."
                    }
                    repository.getResult(careerCode, currentSkills)
                }
                _uiState.value = ProjectProgressUiState(result = result)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Unable to update task."
                )
            }
        }
    }

    fun submitEvidence(
        projectId: String,
        evidence: ProjectEvidence,
        careerCode: String,
        currentSkills: List<String>
    ) {
        viewModelScope.launch {
            try {
                val result = withContext(Dispatchers.Default) {
                    checkNotNull(repository.submitEvidence(projectId, evidence)) {
                        "Project not found."
                    }
                    repository.getResult(careerCode, currentSkills)
                }
                _uiState.value = ProjectProgressUiState(result = result)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Unable to submit evidence."
                )
            }
        }
    }

    fun evaluateProject(
        projectId: String,
        evaluation: ProjectEvaluation,
        careerCode: String,
        currentSkills: List<String>
    ) {
        viewModelScope.launch {
            try {
                val result = withContext(Dispatchers.Default) {
                    checkNotNull(repository.evaluateProject(projectId, evaluation)) {
                        "Project not found."
                    }
                    repository.getResult(careerCode, currentSkills)
                }
                _uiState.value = ProjectProgressUiState(result = result)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Unable to evaluate project."
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
