package com.example.app.assessment

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AssessmentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAnswer(
        answer: AssessmentAnswer
    )

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveResult(
        result: AssessmentResult
    )

    @Query(
        """
        SELECT * FROM assessment_answers
        WHERE googleId = :googleId
        """
    )
    suspend fun getAnswers(
        googleId: String
    ): List<AssessmentAnswer>

    @Query(
        """
        SELECT * FROM assessment_results
        WHERE googleId = :googleId
        ORDER BY attempt DESC
        LIMIT 1
        """
    )
    suspend fun getLatestResult(
        googleId: String
    ): AssessmentResult?

    @Query(
        """
        SELECT MAX(attempt)
        FROM assessment_results
        WHERE googleId = :googleId
        """
    )
    suspend fun getLatestAttempt(
        googleId: String
    ): Int?

    @Query(
        """
        DELETE FROM assessment_answers
        WHERE googleId = :googleId
        """
    )
    suspend fun deleteAnswers(
        googleId: String
    )
}
