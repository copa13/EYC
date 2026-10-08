package com.example.app.assessment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class AssessmentUiState(

    val currentQuestion: Int = 0,

    val answers: Map<Int, Int> = emptyMap(),

    val completed: Boolean = false,

    val result: AssessmentResult? = null
)

class AssessmentViewModel(
    private val repository: AssessmentRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(
            AssessmentUiState()
        )

    val uiState: StateFlow<AssessmentUiState> =
        _uiState

    fun selectAnswer(
        questionId: Int,
        optionIndex: Int
    ) {

        val current =
            _uiState.value

        _uiState.value =
            current.copy(
                answers =
                    current.answers +
                            (questionId to optionIndex)
            )
    }

    fun nextQuestion() {

        val current =
            _uiState.value

        if (
            current.currentQuestion <
            AssessmentQuestions.questions.lastIndex
        ) {

            _uiState.value =
                current.copy(
                    currentQuestion =
                        current.currentQuestion + 1
                )

        } else {

            _uiState.value =
                current.copy(
                    completed = true
                )
        }
    }

    fun previousQuestion() {

        val current =
            _uiState.value

        if (current.currentQuestion > 0) {

            _uiState.value =
                current.copy(
                    currentQuestion =
                        current.currentQuestion - 1
                )
        }
    }

    fun submit(
        googleId: String
    ) {

        viewModelScope.launch {

            val state =
                _uiState.value

            repository.clearCurrentAnswers(
                googleId
            )

            var interestScore = 0
            var aptitudeScore = 0

            AssessmentQuestions.questions.forEach { question ->

                val selected =
                    state.answers[question.id]
                        ?: return@forEach

                val option =
                    question.options[selected]

                val score =
                    option.score

                if (
                    question.type ==
                    AssessmentType.INTEREST
                ) {
                    interestScore += score
                } else {
                    aptitudeScore += score
                }

                repository.saveAnswer(

                    AssessmentAnswer(

                        googleId =
                            googleId,

                        questionId =
                            question.id,

                        selectedOption =
                            selected,

                        score =
                            score,

                        assessmentType =
                            question.type.name,

                        timestamp =
                            System.currentTimeMillis()
                    )
                )
            }

            val interestMax =
                AssessmentQuestions.questions
                    .count {
                        it.type ==
                                AssessmentType.INTEREST
                    } * 5

            val aptitudeMax =
                AssessmentQuestions.questions
                    .count {
                        it.type ==
                                AssessmentType.APTITUDE
                    } * 5

            val interestPercentage =
                if (interestMax == 0)
                    0
                else
                    (
                        interestScore * 100
                    ) / interestMax

            val aptitudePercentage =
                if (aptitudeMax == 0)
                    0
                else
                    (
                        aptitudeScore * 100
                    ) / aptitudeMax

            val overallPercentage =
                (
                    interestPercentage +
                            aptitudePercentage
                    ) / 2

            val attempt =
                repository.getLatestAttempt(
                    googleId
                ) + 1

            val result =
                AssessmentResult(

                    googleId =
                        googleId,

                    attempt =
                        attempt,

                    interestScore =
                        interestScore,

                    aptitudeScore =
                        aptitudeScore,

                    interestPercentage =
                        interestPercentage,

                    aptitudePercentage =
                        aptitudePercentage,

                    overallPercentage =
                        overallPercentage,

                    interestLevel =
                        level(
                            interestPercentage
                        ),

                    aptitudeLevel =
                        level(
                            aptitudePercentage
                        ),

                    overallLevel =
                        level(
                            overallPercentage
                        ),

                    completedAt =
                        System.currentTimeMillis()
                )

            repository.saveResult(
                result
            )

            _uiState.value =
                state.copy(
                    result = result,
                    completed = true
                )
        }
    }

    fun reset() {

        _uiState.value =
            AssessmentUiState()
    }

    private fun level(
        percentage: Int
    ): String {

        return when {

            percentage >= 80 ->
                "Strong"

            percentage >= 60 ->
                "Good"

            percentage >= 40 ->
                "Moderate"

            else ->
                "Needs Improvement"
        }
    }
}
