package com.example.app.learningpath

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app.skillgap.SkillGapResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class LearningPathUiState(
    val isLoading: Boolean = false,
    val result: LearningPathResult? = null,
    val error: String? = null
)

class LearningPathViewModel(
    private val repository: LearningPathRepository =
        LearningPathRepository()
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(
            LearningPathUiState()
        )

    val uiState: StateFlow<LearningPathUiState> =
        _uiState.asStateFlow()

    fun createFromSkillGap(
        skillGapResult: SkillGapResult
    ) {

        viewModelScope.launch {

            _uiState.value =
                LearningPathUiState(
                    isLoading = true
                )

            try {

                val result =
                    repository.createFromSkillGap(
                        skillGapResult
                    )

                _uiState.value =
                    LearningPathUiState(
                        isLoading = false,
                        result = result
                    )

            } catch (exception: Exception) {

                _uiState.value =
                    LearningPathUiState(
                        isLoading = false,
                        error =
                            exception.message
                                ?: "Unable to create learning pathway."
                    )
            }
        }
    }

    fun createPath(
        input: LearningCareerInput
    ) {

        viewModelScope.launch {

            _uiState.value =
                LearningPathUiState(
                    isLoading = true
                )

            try {

                val result =
                    repository.createPath(
                        input
                    )

                _uiState.value =
                    LearningPathUiState(
                        isLoading = false,
                        result = result
                    )

            } catch (exception: Exception) {

                _uiState.value =
                    LearningPathUiState(
                        isLoading = false,
                        error =
                            exception.message
                                ?: "Unable to create learning pathway."
                    )
            }
        }
    }

    fun markTaskCompleted(
        taskId: String,
        completed: Boolean
    ) {

        val currentResult =
            _uiState.value.result
                ?: return

        viewModelScope.launch {

            try {

                val updatedResult =
                    repository.updateTaskCompletion(
                        result = currentResult,
                        taskId = taskId,
                        completed = completed
                    )

                _uiState.value =
                    _uiState.value.copy(
                        result = updatedResult,
                        error = null
                    )

            } catch (exception: Exception) {

                _uiState.value =
                    _uiState.value.copy(
                        error =
                            exception.message
                                ?: "Unable to update learning progress."
                    )
            }
        }
    }

    fun clearError() {

        _uiState.value =
            _uiState.value.copy(
                error = null
            )
    }

    fun clearPath() {

        _uiState.value =
            LearningPathUiState()
    }
}
