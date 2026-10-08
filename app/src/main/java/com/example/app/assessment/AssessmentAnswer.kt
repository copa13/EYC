package com.example.app.assessment

import androidx.room.Entity

@Entity(
    tableName = "assessment_answers",
    primaryKeys = ["googleId", "questionId"]
)
data class AssessmentAnswer(

    val googleId: String,

    val questionId: Int,

    val selectedOption: Int,

    val score: Int,

    val assessmentType: String,

    val timestamp: Long
)
