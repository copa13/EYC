package com.example.app.skillgap

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app.careermatching.CareerCandidate
import com.example.app.careermatching.EycCareerInput
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class SkillGapUiState(
    val isLoading: Boolean = false,
    val result: SkillGapResult? = null,
    val comparison: SkillGapComparison? = null,
    val error: String? = null
)

class SkillGapViewModel(
    private val repository: SkillGapRepository = SkillGapRepository()
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(SkillGapUiState())

    val uiState: StateFlow<SkillGapUiState> =
        _uiState.asStateFlow()

    fun analyzeCareer(
        input: EycCareerInput,
        career: CareerCandidate
    ) {

        viewModelScope.launch {

            _uiState.value =
                SkillGapUiState(
                    isLoading = true
                )

            try {

                val result =
                    repository.analyzeCareer(
                        input = input,
                        career = career
                    )

                _uiState.value =
                    SkillGapUiState(
                        isLoading = false,
                        result = result
                    )

            } catch (exception: Exception) {

                _uiState.value =
                    SkillGapUiState(
                        isLoading = false,
                        error =
                            exception.message
                                ?: "Unable to analyze skill gap."
                    )
            }
        }
    }

    fun analyzeCareers(
        input: EycCareerInput,
        careers: List<CareerCandidate>
    ) {

        viewModelScope.launch {

            _uiState.value =
                SkillGapUiState(
                    isLoading = true
                )

            try {

                val comparison =
                    repository.analyzeCareers(
                        input = input,
                        careers = careers
                    )

                _uiState.value =
                    SkillGapUiState(
                        isLoading = false,
                        comparison = comparison
                    )

            } catch (exception: Exception) {

                _uiState.value =
                    SkillGapUiState(
                        isLoading = false,
                        error =
                            exception.message
                                ?: "Unable to analyze career skill gaps."
                    )
            }
        }
    }

    fun clearError() {

        val current =
            _uiState.value

        _uiState.value =
            current.copy(
                error = null
            )
    }

    fun clearResult() {

        _uiState.value =
            SkillGapUiState()
    }
}
