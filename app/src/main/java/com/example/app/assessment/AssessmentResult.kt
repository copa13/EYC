package com.example.app.assessment

import androidx.room.Entity

@Entity(
    tableName = "assessment_results",
    primaryKeys = ["googleId", "attempt"]
)
data class AssessmentResult(

    val googleId: String,

    val attempt: Int,

    val interestScore: Int,

    val aptitudeScore: Int,

    val interestPercentage: Int,

    val aptitudePercentage: Int,

    val overallPercentage: Int,

    val interestLevel: String,

    val aptitudeLevel: String,

    val overallLevel: String,

    val completedAt: Long
)
