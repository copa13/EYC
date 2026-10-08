package com.example.app.careermatching

import android.content.Context

class CareerMatchingRepository(
    context: Context
) {

    private val database =
        OnetDatabase(context.applicationContext)

    private val importer =
        OnetImporter(
            context = context.applicationContext,
            database = database
        )

    private val matchingEngine =
        CareerMatchingEngine()

    private val scoringEngine =
        CareerScoringEngine()

    private val explanationEngine =
        CareerExplanationEngine()

    suspend fun ensureDatabase() {

        if (database.occupationCount() < 1000) {
            importer.importAll()
        }
    }

    suspend fun rebuildDatabase() {

        database.clearAll()

        importer.importAll()
    }

    suspend fun findCareers(
        input: EycCareerInput,
        limit: Int = 5
    ): CareerMatchingResult {

        ensureDatabase()

        val occupations =
            database.getAllOccupations()

        val candidates =
            matchingEngine.match(
                input = input,
                occupations = occupations,
                limit = limit
            )

        return CareerMatchingResult(
            studentId = input.studentId,
            candidates = candidates
        )
    }

    suspend fun rankCareers(
        input: EycCareerInput,
        limit: Int = 5
    ): CareerRankingResult {

        val matchingResult =
            findCareers(
                input = input,
                limit = limit
            )

        return scoringEngine.rank(
            studentId = input.studentId,
            candidates = matchingResult.candidates
        )
    }

    suspend fun explainCareers(
        input: EycCareerInput,
        limit: Int = 5
    ): CareerExplanationResult {

        val matchingResult =
            findCareers(
                input = input,
                limit = limit
            )

        val rankingResult =
            scoringEngine.rank(
                studentId = input.studentId,
                candidates = matchingResult.candidates
            )

        return explanationEngine.explain(
            studentId = input.studentId,
            candidates = matchingResult.candidates,
            rankedScores = rankingResult.rankedCareers
        )
    }

    suspend fun getBestCareerExplanation(
        input: EycCareerInput
    ): CareerRecommendationExplanation? {

        val result =
            explainCareers(
                input = input,
                limit = 5
            )

        return result.bestMatch
    }

    fun close() {
        database.close()
    }
}
