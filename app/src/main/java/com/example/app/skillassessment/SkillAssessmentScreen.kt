package com.example.app.skillassessment

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun SkillAssessmentScreen(
    viewModel: SkillAssessmentViewModel =
        viewModel()
) {

    val state by
        viewModel.uiState.collectAsState()

    when {

        state.loading -> {
            LoadingScreen()
        }

        state.error != null -> {
            ErrorScreen(
                message = state.error!!,
                onRetry = {
                    viewModel.reset()
                }
            )
        }

        state.finished -> {
            SkillFinalResultScreen(
                results = state.results,
                onRestart = {
                    viewModel.reset()
                }
            )
        }

        state.questions.isNotEmpty() &&
                state.currentSkill != null -> {

            SkillQuestionScreen(
                skill = state.currentSkill,
                questions = state.questions,
                currentQuestionIndex =
                    state.currentQuestionIndex,
                selectedAnswer =
                    state.selectedAnswer,
                onAnswerSelected =
                    viewModel::selectAnswer,
                onNext =
                    viewModel::nextQuestion,
                onPrevious =
                    viewModel::previousQuestion
            )
        }

        else -> {

            SkillSelectionScreen(
                skills = state.skills,
                selectedSkills =
                    state.selectedSkills,
                results =
                    state.results,
                onSkillToggle =
                    viewModel::toggleSkill,
                onStartSkill =
                    viewModel::startSkillAssessment,
                onFinish =
                    viewModel::finishAssessment
            )
        }
    }
}

@Composable
private fun LoadingScreen() {

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(24.dp),
        horizontalAlignment =
            Alignment.CenterHorizontally,
        verticalArrangement =
            Arrangement.Center
    ) {

        CircularProgressIndicator()

        Spacer(
            modifier =
                Modifier.height(16.dp)
        )

        Text(
            "Loading Skill Assessment..."
        )
    }
}

@Composable
private fun ErrorScreen(
    message: String,
    onRetry: () -> Unit
) {

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(24.dp),
        horizontalAlignment =
            Alignment.CenterHorizontally,
        verticalArrangement =
            Arrangement.Center
    ) {

        Text(
            text = "Something went wrong",
            style =
                MaterialTheme.typography.headlineSmall
        )

        Spacer(
            modifier =
                Modifier.height(8.dp)
        )

        Text(message)

        Spacer(
            modifier =
                Modifier.height(20.dp)
        )

        Button(
            onClick = onRetry
        ) {
            Text("Retry")
        }
    }
}

@Composable
private fun SkillSelectionScreen(
    skills: List<Skill>,
    selectedSkills: List<Skill>,
    results: List<SkillResult>,
    onSkillToggle: (Skill) -> Unit,
    onStartSkill: (Skill) -> Unit,
    onFinish: () -> Unit
) {

    LazyColumn(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(16.dp),
        verticalArrangement =
            Arrangement.spacedBy(12.dp)
    ) {

        item {

            Text(
                text = "Skill Assessment",
                style =
                    MaterialTheme.typography.headlineMedium
            )

            Spacer(
                modifier =
                    Modifier.height(8.dp)
            )

            Text(
                text =
                    "Select skills you want to assess. EYC can also suggest skills based on your profile and interests."
            )
        }

        item {
            HorizontalDivider()
        }

        items(skills) { skill ->

            val selected =
                selectedSkills.any {
                    it.id == skill.id
                }

            val result =
                results.firstOrNull {
                    it.skillId == skill.id
                }

            Card(
                modifier =
                    Modifier.fillMaxWidth()
            ) {

                Column(
                    modifier =
                        Modifier.padding(16.dp)
                ) {

                    Row(
                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {

                        RadioButton(
                            selected = selected,
                            onClick = {
                                onSkillToggle(skill)
                            }
                        )

                        Column(
                            modifier =
                                Modifier.weight(1f)
                        ) {

                            Text(
                                text = skill.name,
                                style =
                                    MaterialTheme
                                        .typography
                                        .titleMedium
                            )

                            Text(
                                text =
                                    skill.description
                            )

                            Text(
                                text =
                                    "Category: ${skill.category}"
                            )
                        }
                    }

                    if (result != null) {

                        Spacer(
                            modifier =
                                Modifier.height(8.dp)
                        )

                        Text(
                            text =
                                "Result: ${result.level} — ${result.percentage}%"
                        )
                    }

                    if (selected) {

                        Spacer(
                            modifier =
                                Modifier.height(8.dp)
                        )

                        Button(
                            onClick = {
                                onStartSkill(skill)
                            },
                            modifier =
                                Modifier.fillMaxWidth()
                        ) {

                            Text(
                                if (result == null)
                                    "Start Assessment"
                                else
                                    "Retake Assessment"
                            )
                        }
                    }
                }
            }
        }

        item {

            Button(
                onClick = onFinish,
                modifier =
                    Modifier.fillMaxWidth(),
                enabled =
                    results.isNotEmpty()
            ) {

                Text(
                    "Finish Skill Assessment"
                )
            }
        }
    }
}

@Composable
private fun SkillQuestionScreen(
    skill: Skill?,
    questions: List<SkillQuestion>,
    currentQuestionIndex: Int,
    selectedAnswer: String?,
    onAnswerSelected:
        (SkillOption) -> Unit,
    onNext: () -> Unit,
    onPrevious: () -> Unit
) {

    val question =
        questions[currentQuestionIndex]

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(20.dp)
    ) {

        Text(
            text = skill?.name ?: "",
            style =
                MaterialTheme.typography.headlineSmall
        )

        Spacer(
            modifier =
                Modifier.height(8.dp)
        )

        Text(
            text =
                "Question ${currentQuestionIndex + 1} of ${questions.size}"
        )

        Spacer(
            modifier =
                Modifier.height(24.dp)
        )

        Text(
            text = question.question,
            style =
                MaterialTheme.typography.titleLarge
        )

        Spacer(
            modifier =
                Modifier.height(20.dp)
        )

        question.options.forEach { option ->

            Card(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp)
            ) {

                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    RadioButton(
                        selected =
                            selectedAnswer == option.id,
                        onClick = {
                            onAnswerSelected(option)
                        }
                    )

                    Text(
                        text = option.text,
                        modifier =
                            Modifier.padding(
                                start = 8.dp
                            )
                    )
                }
            }
        }

        Spacer(
            modifier =
                Modifier.weight(1f)
        )

        Row(
            modifier =
                Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.spacedBy(10.dp)
        ) {

            OutlinedButton(
                onClick = onPrevious,
                enabled =
                    currentQuestionIndex > 0,
                modifier =
                    Modifier.weight(1f)
            ) {
                Text("Previous")
            }

            Button(
                onClick = onNext,
                enabled =
                    selectedAnswer != null,
                modifier =
                    Modifier.weight(1f)
            ) {

                Text(
                    if (
                        currentQuestionIndex ==
                        questions.lastIndex
                    )
                        "Finish"
                    else
                        "Next"
                )
            }
        }
    }
}

@Composable
private fun SkillFinalResultScreen(
    results: List<SkillResult>,
    onRestart: () -> Unit
) {

    LazyColumn(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(20.dp),
        verticalArrangement =
            Arrangement.spacedBy(12.dp)
    ) {

        item {

            Text(
                text = "Skill Assessment Result",
                style =
                    MaterialTheme.typography.headlineMedium
            )
        }

        items(results) { result ->

            Card(
                modifier =
                    Modifier.fillMaxWidth()
            ) {

                Column(
                    modifier =
                        Modifier.padding(16.dp)
                ) {

                    Text(
                        text = result.skillName,
                        style =
                            MaterialTheme.typography.titleLarge
                    )

                    Text(
                        text =
                            "Level: ${result.level}"
                    )

                    Text(
                        text =
                            "Score: ${result.score}/${result.maxScore}"
                    )

                    Text(
                        text =
                            "Percentage: ${result.percentage}%"
                    )
                }
            }
        }

        item {

            Button(
                onClick = onRestart,
                modifier =
                    Modifier.fillMaxWidth()
            ) {

                Text("Assess Again")
            }
        }
    }
}
