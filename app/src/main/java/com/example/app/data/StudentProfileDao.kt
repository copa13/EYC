package com.example.app.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface StudentProfileDao {

    @Query(
        "SELECT * FROM student_profile WHERE googleId = :googleId LIMIT 1"
    )
    suspend fun getProfile(
        googleId: String
    ): StudentProfile?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveProfile(
        profile: StudentProfile
    )

    @Query(
        "DELETE FROM student_profile WHERE googleId = :googleId"
    )
    suspend fun deleteProfile(
        googleId: String
    )
}
