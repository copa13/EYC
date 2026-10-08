package com.example.app.assessment

class AssessmentRepository(
    private val dao: AssessmentDao
) {

    suspend fun saveAnswer(
        answer: AssessmentAnswer
    ) {
        dao.saveAnswer(answer)
    }

    suspend fun saveResult(
        result: AssessmentResult
    ) {
        dao.saveResult(result)
    }

    suspend fun getLatestResult(
        googleId: String
    ): AssessmentResult? {
        return dao.getLatestResult(googleId)
    }

    suspend fun getLatestAttempt(
        googleId: String
    ): Int {
        return dao.getLatestAttempt(googleId) ?: 0
    }

    suspend fun clearCurrentAnswers(
        googleId: String
    ) {
        dao.deleteAnswers(googleId)
    }
}
