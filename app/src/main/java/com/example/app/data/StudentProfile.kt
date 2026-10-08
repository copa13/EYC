package com.example.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "student_profile")
data class StudentProfile(

    @PrimaryKey
    val googleId: String,

    val name: String,

    val email: String,

    val profilePhotoUri: String?,

    val age: String,

    val education: String,

    val courseTrade: String,

    val educationLevel: String,

    val interests: String,

    val basicSkills: String,

    val preferredCareerAreas: String,

    val learningPreferences: String,

    val currentCareerGoal: String,

    val experienceProjects: String,

    val languagesKnown: String,

    val selfConfidenceLevel: String
)
