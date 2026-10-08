package com.example.app.careerguidance

import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class GeminiCareerRepository {

    companion object {

        private const val MODEL =
            "gemini-3.8-flash"

        private const val ENDPOINT =
            "https://generativelanguage.googleapis.com/v1beta/models/$MODEL:generateContent"
    }

    fun generateCareerPrediction(
        prompt: String,
        apiKey: String
    ): CareerPredictionResult {

        if (apiKey.isBlank()) {
            throw IllegalStateException(
                "Gemini API key is missing."
            )
        }

        val url =
            URL("$ENDPOINT?key=$apiKey")

        val connection =
            url.openConnection() as HttpURLConnection

        connection.requestMethod = "POST"
        connection.connectTimeout = 30_000
        connection.readTimeout = 60_000
        connection.doOutput = true

        connection.setRequestProperty(
            "Content-Type",
            "application/json"
        )

        val requestBody =
            JSONObject()
                .put(
                    "contents",
                    JSONArray()
                        .put(
                            JSONObject()
                                .put(
                                    "parts",
                                    JSONArray()
                                        .put(
                                            JSONObject()
                                                .put(
                                                    "text",
                                                    prompt
                                                )
                                        )
                                )
                        )
                )
                .put(
                    "generationConfig",
                    JSONObject()
                        .put(
                            "temperature",
                            0.2
                        )
                        .put(
                            "maxOutputTokens",
                            2000
                        )
                )

        connection.outputStream.use { output ->
            output.write(
                requestBody
                    .toString()
                    .toByteArray(Charsets.UTF_8)
            )
        }

        val responseCode =
            connection.responseCode

        val responseText =
            if (responseCode in 200..299) {
                connection.inputStream
                    .bufferedReader()
                    .use { it.readText() }
            } else {
                connection.errorStream
                    ?.bufferedReader()
                    ?.use { it.readText() }
                    ?: "Gemini request failed."
            }

        connection.disconnect()

        if (responseCode !in 200..299) {
            throw IllegalStateException(
                "Gemini API error: $responseText"
            )
        }

        return parseResponse(responseText)
    }

    private fun parseResponse(
        responseText: String
    ): CareerPredictionResult {

        val root =
            JSONObject(responseText)

        val candidates =
            root.optJSONArray("candidates")
                ?: throw IllegalStateException(
                    "No Gemini candidate returned."
                )

        if (candidates.length() == 0) {
            throw IllegalStateException(
                "Gemini returned no result."
            )
        }

        val candidate =
            candidates.getJSONObject(0)

        val content =
            candidate.getJSONObject("content")

        val parts =
            content.getJSONArray("parts")

        val rawText =
            parts
                .getJSONObject(0)
                .optString("text")
                .trim()

        val cleanJson =
            rawText
                .removePrefix("```json")
                .removePrefix("```")
                .removeSuffix("```")
                .trim()

        val resultJson =
            JSONObject(cleanJson)

        val summary =
            resultJson.optString("summary")

        val recommendationsJson =
            resultJson.optJSONArray(
                "recommendations"
            )
                ?: JSONArray()

        val recommendations =
            mutableListOf<CareerRecommendation>()

        for (index in 0 until recommendationsJson.length()) {

            val item =
                recommendationsJson
                    .getJSONObject(index)

            val strengths =
                jsonArrayToList(
                    item.optJSONArray("strengths")
                )

            val importantSkills =
                jsonArrayToList(
                    item.optJSONArray("importantSkills")
                )

            recommendations.add(
                CareerRecommendation(
                    careerName =
                        item.optString(
                            "careerName"
                        ),
                    matchPercentage =
                        item.optInt(
                            "matchPercentage"
                        ).coerceIn(0, 100),
                    reason =
                        item.optString(
                            "reason"
                        ),
                    strengths =
                        strengths,
                    importantSkills =
                        importantSkills
                )
            )
        }

        return CareerPredictionResult(
            summary = summary,
            recommendations =
                recommendations.sortedByDescending {
                    it.matchPercentage
                }
        )
    }

    private fun jsonArrayToList(
        array: JSONArray?
    ): List<String> {

        if (array == null) {
            return emptyList()
        }

        val result =
            mutableListOf<String>()

        for (index in 0 until array.length()) {
            result.add(
                array.optString(index)
            )
        }

        return result
    }
}
