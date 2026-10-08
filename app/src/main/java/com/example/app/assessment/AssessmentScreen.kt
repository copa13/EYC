package com.example.app.assessment

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AssessmentScreen(
    googleId: String,
    viewModel: AssessmentViewModel
) {

    val state by
        viewModel.uiState.collectAsState()

    if (
        state.completed &&
        state.result != null
    ) {

        AssessmentResultScreen(
            result = state.result!!,
            onRetake = {
                viewModel.reset()
            }
        )

        return
    }

    val questions =
        AssessmentQuestions.questions

    val question =
        questions[state.currentQuestion]

    val selected =
        state.answers[question.id]

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .verticalScroll(
                    rememberScrollState()
                )
                .padding(20.dp)
    ) {

        Text(
            text =
                "Interest & Aptitude Assessment",

            style =
                MaterialTheme
                    .typography
                    .headlineSmall
        )

        Spacer(
            modifier =
                Modifier.height(16.dp)
        )

        Text(
            text =
                "Question ${state.currentQuestion + 1} of ${questions.size}"
        )

        Spacer(
            modifier =
                Modifier.height(8.dp)
        )

        LinearProgressIndicator(
            progress = {
                (
                    state.currentQuestion + 1
                ).toFloat() / questions.size
            },

            modifier =
                Modifier.fillMaxWidth()
        )

        Spacer(
            modifier =
                Modifier.height(24.dp)
        )

        Text(
            text =
                if (
                    question.type ==
                    AssessmentType.INTEREST
                )
                    "Interest Assessment"
                else
                    "Aptitude Assessment",

            style =
                MaterialTheme
                    .typography
                    .titleMedium
        )

        Spacer(
            modifier =
                Modifier.height(12.dp)
        )

        Text(
            text =
                question.question,

            style =
                MaterialTheme
                    .typography
                    .headlineSmall
        )

        Spacer(
            modifier =
                Modifier.height(20.dp)
        )

        question.options.forEachIndexed {
                index,
                option ->

            Card(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(
                            vertical = 5.dp
                        )
            ) {

                Row(
                    modifier =
                        Modifier.fillMaxWidth()
                ) {

                    RadioButton(
                        selected =
                            selected == index,

                        onClick = {

                            viewModel.selectAnswer(
                                question.id,
                                index
                            )
                        }
                    )

                    Text(
                        text =
                            option.text,

                        modifier =
                            Modifier
                                .padding(
                                    vertical = 14.dp
                                )
                    )
                }
            }
        }

        Spacer(
            modifier =
                Modifier.height(24.dp)
        )

        Row(
            modifier =
                Modifier.fillMaxWidth(),

            horizontalArrangement =
                Arrangement.SpaceBetween
        ) {

            OutlinedButton(
                onClick = {
                    viewModel.previousQuestion()
                },

                enabled =
                    state.currentQuestion > 0
            ) {

                Text("Previous")
            }

            Button(
                onClick = {

                    if (
                        state.currentQuestion ==
                        questions.lastIndex
                    ) {

                        viewModel.submit(
                            googleId
                        )

                    } else {

                        viewModel.nextQuestion()
                    }
                },

                enabled =
                    selected != null
            ) {

                Text(
                    if (
                        state.currentQuestion ==
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
private fun AssessmentResultScreen(
    result: AssessmentResult,
    onRetake: () -> Unit
) {

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .verticalScroll(
                    rememberScrollState()
                )
                .padding(20.dp)
    ) {

        Text(
            text =
                "Assessment Result",

            style =
                MaterialTheme
                    .typography
                    .headlineMedium
        )

        Spacer(
            modifier =
                Modifier.height(24.dp)
        )

        ResultCard(
            title =
                "Interest",

            score =
                result.interestPercentage,

            level =
                result.interestLevel
        )

        Spacer(
            modifier =
                Modifier.height(12.dp)
        )

        ResultCard(
            title =
                "Aptitude",

            score =
                result.aptitudePercentage,

            level =
                result.aptitudeLevel
        )

        Spacer(
            modifier =
                Modifier.height(12.dp)
        )

        ResultCard(
            title =
                "Overall",

            score =
                result.overallPercentage,

            level =
                result.overallLevel
        )

        Spacer(
            modifier =
                Modifier.height(24.dp)
        )

        Text(
            text =
                "Attempt ${result.attempt}",

            style =
                MaterialTheme
                    .typography
                    .titleMedium
        )

        Spacer(
            modifier =
                Modifier.height(24.dp)
        )

        Button(
            onClick = onRetake,

            modifier =
                Modifier.fillMaxWidth()
        ) {

            Text(
                "Retake Assessment"
            )
        }
    }
}


@Composable
private fun ResultCard(
    title: String,
    score: Int,
    level: String
) {

    Card(
        modifier =
            Modifier.fillMaxWidth()
    ) {

        Column(
            modifier =
                Modifier.padding(20.dp)
        ) {

            Text(
                text = title,

                style =
                    MaterialTheme
                        .typography
                        .titleLarge
            )

            Spacer(
                modifier =
                    Modifier.height(8.dp)
            )

            Text(
                text = "$score%",

                style =
                    MaterialTheme
                        .typography
                        .headlineMedium
            )

            Spacer(
                modifier =
                    Modifier.height(4.dp)
            )

            Text(
                text = level
            )
        }
    }
}
