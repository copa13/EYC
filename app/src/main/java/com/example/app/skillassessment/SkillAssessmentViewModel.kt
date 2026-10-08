package com.example.app.skillassessment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class SkillAssessmentUiState(
    val loading: Boolean = false,
    val skills: List<Skill> = emptyList(),
    val selectedSkills: List<Skill> = emptyList(),
    val currentSkill: Skill? = null,
    val questions: List<SkillQuestion> = emptyList(),
    val currentQuestionIndex: Int = 0,
    val selectedAnswer: String? = null,
    val results: List<SkillResult> = emptyList(),
    val error: String? = null,
    val finished: Boolean = false
)

class SkillAssessmentViewModel : ViewModel() {

    private val repository =
        SkillFirebaseRepository()

    private val _uiState =
        MutableStateFlow(
            SkillAssessmentUiState()
        )

    val uiState: StateFlow<SkillAssessmentUiState> =
        _uiState.asStateFlow()

    private val answers =
        mutableMapOf<String, Int>()

    init {
        loadSkills()
    }

    private fun loadSkills() {

        viewModelScope.launch {

            _uiState.value =
                _uiState.value.copy(
                    loading = true,
                    error = null
                )

            try {

                val skills =
                    repository.getSkills()

                _uiState.value =
                    _uiState.value.copy(
                        loading = false,
                        skills = skills
                    )

            } catch (e: Exception) {

                _uiState.value =
                    _uiState.value.copy(
                        loading = false,
                        error =
                            e.message
                                ?: "Unable to load skills"
                    )
            }
        }
    }

    fun toggleSkill(
        skill: Skill
    ) {

        val selected =
            _uiState.value
                .selectedSkills
                .toMutableList()

        if (
            selected.any {
                it.id == skill.id
            }
        ) {
            selected.removeAll {
                it.id == skill.id
            }
        } else {
            selected.add(skill)
        }

        _uiState.value =
            _uiState.value.copy(
                selectedSkills = selected
            )
    }

    fun startSkillAssessment(
        skill: Skill
    ) {

        viewModelScope.launch {

            _uiState.value =
                _uiState.value.copy(
                    loading = true,
                    error = null,
                    currentSkill = skill
                )

            try {

                val questions =
                    repository.getQuestions(
                        skill.id
                    )

                answers.clear()

                _uiState.value =
                    _uiState.value.copy(
                        loading = false,
                        questions = questions,
                        currentQuestionIndex = 0,
                        selectedAnswer = null,
                        currentSkill = skill
                    )

            } catch (e: Exception) {

                _uiState.value =
                    _uiState.value.copy(
                        loading = false,
                        error =
                            e.message
                                ?: "Unable to load questions"
                    )
            }
        }
    }

    fun selectAnswer(
        option: SkillOption
    ) {

        val question =
            _uiState.value.questions
                .getOrNull(
                    _uiState.value.currentQuestionIndex
                )
                ?: return

        answers[question.id] =
            option.score

        _uiState.value =
            _uiState.value.copy(
                selectedAnswer = option.id
            )
    }

    fun nextQuestion() {

        val state =
            _uiState.value

        if (
            state.currentQuestionIndex <
            state.questions.lastIndex
        ) {

            _uiState.value =
                state.copy(
                    currentQuestionIndex =
                        state.currentQuestionIndex + 1,
                    selectedAnswer = null
                )

        } else {

            finishCurrentSkill()
        }
    }

    fun previousQuestion() {

        val state =
            _uiState.value

        if (
            state.currentQuestionIndex > 0
        ) {

            _uiState.value =
                state.copy(
                    currentQuestionIndex =
                        state.currentQuestionIndex - 1,
                    selectedAnswer = null
                )
        }
    }

    private fun finishCurrentSkill() {

        viewModelScope.launch {

            val state =
                _uiState.value

            val skill =
                state.currentSkill
                    ?: return@launch

            val questions =
                state.questions

            val score =
                answers.values.sum()

            val maxScore =
                questions.sumOf { question ->
                    question.options.maxOfOrNull {
                        it.score
                    } ?: 0
                }

            val percentage =
                if (maxScore == 0) {
                    0
                } else {
                    ((score * 100f) / maxScore)
                        .toInt()
                }

            val level =
                when {
                    percentage >= 80 ->
                        "Advanced"

                    percentage >= 60 ->
                        "Good"

                    percentage >= 40 ->
                        "Intermediate"

                    else ->
                        "Beginner"
                }

            val result =
                SkillResult(
                    skillId = skill.id,
                    skillName = skill.name,
                    score = score,
                    maxScore = maxScore,
                    percentage = percentage,
                    level = level
                )

            repository.saveResult(result)

            val updatedResults =
                state.results
                    .filterNot {
                        it.skillId == result.skillId
                    }
                    .toMutableList()

            updatedResults.add(result)

            _uiState.value =
                state.copy(
                    results = updatedResults,
                    currentSkill = null,
                    questions = emptyList(),
                    currentQuestionIndex = 0,
                    selectedAnswer = null
                )
        }
    }

    fun finishAssessment() {

        _uiState.value =
            _uiState.value.copy(
                finished = true
            )
    }

    fun reset() {

        answers.clear()

        _uiState.value =
            SkillAssessmentUiState(
                skills =
                    _uiState.value.skills
            )
    }
}
