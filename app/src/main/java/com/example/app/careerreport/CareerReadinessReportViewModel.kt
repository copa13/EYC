
package com.example.app.careerreport

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

sealed interface CareerReportUiState {
    data object Loading : CareerReportUiState
    data class Success(
        val report: CareerReadinessReport
    ) : CareerReportUiState
    data class Error(
        val message: String
    ) : CareerReportUiState
}

class CareerReadinessReportViewModel(
    private val repository: CareerReadinessReportRepository
) : ViewModel() {

    private val _state =
        MutableStateFlow<CareerReportUiState>(
            CareerReportUiState.Loading
        )

    val state: StateFlow<CareerReportUiState> =
        _state.asStateFlow()

    fun loadReport() {
        viewModelScope.launch {
            _state.value = CareerReportUiState.Loading

            try {
                _state.value = CareerReportUiState.Success(
                    repository.generateReport()
                )
            } catch (exception: Exception) {
                _state.value = CareerReportUiState.Error(
                    exception.message ?: "Unable to generate report."
                )
            }
        }
    }
}
