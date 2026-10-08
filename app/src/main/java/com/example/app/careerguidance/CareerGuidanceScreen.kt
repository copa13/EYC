package com.example.app.careerguidance

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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun CareerGuidanceScreen(
    profile: CareerProfileInput,
    assessment: CareerAssessmentInput,
    skills: List<CareerSkillInput>,
    apiKey: String,
    viewModel: CareerGuidanceViewModel =
        viewModel()
) {

    val state by
        viewModel.uiState.collectAsState()

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .verticalScroll(
                    rememberScrollState()
                )
                .padding(16.dp)
    ) {

        Text(
            text = "AI Career Guidance",
            style =
                MaterialTheme.typography.headlineMedium
        )

        Spacer(
            modifier =
                Modifier.height(12.dp)
        )

        if (!state.loading &&
            state.result == null
        ) {

            Text(
                text =
                    "EYC will analyze your profile, assessment and skills."
            )

            Spacer(
                modifier =
                    Modifier.height(16.dp)
            )

            Button(
                modifier =
                    Modifier.fillMaxWidth(),
                onClick = {

                    viewModel.predictCareer(
                        profile = profile,
                        assessment = assessment,
                        skills = skills,
                        apiKey = apiKey
                    )
                }
            ) {

                Text(
                    text =
                        "Predict My Career"
                )
            }
        }

        if (state.loading) {

            Column(
                modifier =
                    Modifier.fillMaxWidth(),
                horizontalAlignment =
                    androidx.compose.ui.Alignment.CenterHorizontally
            ) {

                CircularProgressIndicator()

                Spacer(
                    modifier =
                        Modifier.height(12.dp)
                )

                Text(
                    text =
                        "Analyzing your career profile..."
                )
            }
        }

        state.error?.let { error ->

            Card(
                modifier =
                    Modifier.fillMaxWidth()
            ) {

                Column(
                    modifier =
                        Modifier.padding(16.dp)
                ) {

                    Text(
                        text = "Error",
                        style =
                            MaterialTheme.typography.titleMedium
                    )

                    Spacer(
                        modifier =
                            Modifier.height(8.dp)
                    )

                    Text(
                        text = error
                    )

                    Spacer(
                        modifier =
                            Modifier.height(12.dp)
                    )

                    Button(
                        onClick = {

                            viewModel.predictCareer(
                                profile,
                                assessment,
                                skills,
                                apiKey
                            )
                        }
                    ) {

                        Text(
                            text = "Retry"
                        )
                    }
                }
            }
        }

        state.result?.let { result ->

            CareerPredictionResultView(
                result = result,
                onNewPrediction = {
                    viewModel.clearResult()
                }
            )
        }
    }
}

@Composable
private fun CareerPredictionResultView(
    result: CareerPredictionResult,
    onNewPrediction: () -> Unit
) {

    Text(
        text = "Career Prediction",
        style =
            MaterialTheme.typography.headlineSmall
    )

    Spacer(
        modifier =
            Modifier.height(12.dp)
    )

    if (result.summary.isNotBlank()) {

        Card(
            modifier =
                Modifier.fillMaxWidth()
        ) {

            Column(
                modifier =
                    Modifier.padding(16.dp)
            ) {

                Text(
                    text = "Overall Analysis",
                    style =
                        MaterialTheme.typography.titleMedium
                )

                Spacer(
                    modifier =
                        Modifier.height(8.dp)
                )

                Text(
                    text = result.summary
                )
            }
        }

        Spacer(
            modifier =
                Modifier.height(16.dp)
        )
    }

    result.recommendations.forEachIndexed {
            index,
            career ->

        Card(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(
                        bottom = 12.dp
                    )
        ) {

            Column(
                modifier =
                    Modifier.padding(16.dp)
            ) {

                Row(
                    modifier =
                        Modifier.fillMaxWidth(),
                    horizontalArrangement =
                        Arrangement.SpaceBetween
                ) {

                    Text(
                        text =
                            "${index + 1}. ${career.careerName}",
                        style =
                            MaterialTheme.typography.titleMedium
                    )

                    Text(
                        text =
                            "${career.matchPercentage}%"
                    )
                }

                Spacer(
                    modifier =
                        Modifier.height(10.dp)
                )

                Text(
                    text = career.reason
                )

                if (
                    career.strengths.isNotEmpty()
                ) {

                    Spacer(
                        modifier =
                            Modifier.height(10.dp)
                    )

                    Text(
                        text = "Your strengths"
                    )

                    career.strengths.forEach {
                        Text(
                            text = "• $it"
                        )
                    }
                }

                if (
                    career.importantSkills.isNotEmpty()
                ) {

                    Spacer(
                        modifier =
                            Modifier.height(10.dp)
                    )

                    Text(
                        text = "Important skills"
                    )

                    career.importantSkills.forEach {
                        Text(
                            text = "• $it"
                        )
                    }
                }
            }
        }
    }

    Button(
        modifier =
            Modifier.fillMaxWidth(),
        onClick = onNewPrediction
    ) {

        Text(
            text = "New Prediction"
        )
    }
}
