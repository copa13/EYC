package com.example.app.careerguidance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class CareerGuidanceUiState(
    val loading: Boolean = false,
    val result: CareerPredictionResult? = null,
    val error: String? = null
)

class CareerGuidanceViewModel :
    ViewModel() {

    private val adapter =
        CareerAdapter()

    private val repository =
        GeminiCareerRepository()

    private val _uiState =
        MutableStateFlow(
            CareerGuidanceUiState()
        )

    val uiState: StateFlow<CareerGuidanceUiState> =
        _uiState.asStateFlow()

    fun predictCareer(
        profile: CareerProfileInput,
        assessment: CareerAssessmentInput,
        skills: List<CareerSkillInput>,
        apiKey: String
    ) {

        viewModelScope.launch {

            _uiState.value =
                CareerGuidanceUiState(
                    loading = true
                )

            try {

                val prompt =
                    adapter.buildCareerPrompt(
                        profile = profile,
                        assessment = assessment,
                        skills = skills
                    )

                val result =
                    withContext(
                        Dispatchers.IO
                    ) {
                        repository.generateCareerPrediction(
                            prompt = prompt,
                            apiKey = apiKey
                        )
                    }

                _uiState.value =
                    CareerGuidanceUiState(
                        loading = false,
                        result = result
                    )

            } catch (e: Exception) {

                _uiState.value =
                    CareerGuidanceUiState(
                        loading = false,
                        error =
                            e.message
                                ?: "Career prediction failed."
                    )
            }
        }
    }

    fun clearResult() {

        _uiState.value =
            CareerGuidanceUiState()
    }
}
