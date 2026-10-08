package com.example.app.careerguidance

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface

class CareerGuidanceActivity :
    ComponentActivity() {

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(
            savedInstanceState
        )

        setContent {

            MaterialTheme {

                Surface {

                    /*
                     * Real EYC #1, #2 and #3 data
                     * will be supplied here when
                     * the main EYC navigation is connected.
                     *
                     * These are only sample values
                     * for this standalone activity.
                     */

                    val profile =
                        CareerProfileInput(
                            name = "Student",
                            education = "",
                            courseTrade = "",
                            interests =
                                emptyList()
                        )

                    val assessment =
                        CareerAssessmentInput()

                    val skills =
                        emptyList<CareerSkillInput>()

                    CareerGuidanceScreen(
                        profile = profile,
                        assessment = assessment,
                        skills = skills,
                        apiKey =
                            BuildConfig.GEMINI_API_KEY
                    )
                }
            }
        }
    }
}
