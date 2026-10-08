package com.example.app.careerguidance

class CareerAdapter {

    fun buildCareerPrompt(
        profile: CareerProfileInput,
        assessment: CareerAssessmentInput,
        skills: List<CareerSkillInput>
    ): String {

        val skillsText =
            if (skills.isEmpty()) {
                "No skill assessment data available."
            } else {
                skills.joinToString("\n") {
                    "- ${it.skillName}: ${it.percentage}% (${it.level})"
                }
            }

        val interestsText =
            profile.interests.joinToString(", ")
                .ifBlank { "Not provided" }

        val preferredAreasText =
            profile.preferredCareerAreas.joinToString(", ")
                .ifBlank { "Not provided" }

        val learningText =
            profile.learningPreferences.joinToString(", ")
                .ifBlank { "Not provided" }

        val projectsText =
            profile.experienceProjects.joinToString(", ")
                .ifBlank { "None" }

        val languagesText =
            profile.languagesKnown.joinToString(", ")
                .ifBlank { "Not provided" }

        return """
            You are the career prediction engine for an education app called EYC.

            Your task is to predict suitable career options for the student.

            IMPORTANT:
            - Use all available student information.
            - Consider interests, aptitude, skills, education and career preferences.
            - Do not choose a career only from one field.
            - Give multiple realistic career options.
            - Rank the careers from strongest match to weaker match.
            - Match percentage must be between 0 and 100.
            - Explain why each career matches the student.
            - Do not make medical, legal or financial decisions.
            - Return ONLY valid JSON.
            
            Student Profile:
            Name: ${profile.name}
            Age: ${profile.age}
            Education: ${profile.education}
            Course/Trade: ${profile.courseTrade}
            Education Level: ${profile.educationLevel}
            Interests: $interestsText
            Preferred Career Areas: $preferredAreasText
            Learning Preferences: $learningText
            Current Career Goal: ${profile.currentCareerGoal}
            Experience/Projects: $projectsText
            Languages Known: $languagesText
            Self Confidence: ${profile.selfConfidenceLevel}

            Interest & Aptitude Assessment:
            Interest Score: ${assessment.interestScore}
            Interest Percentage: ${assessment.interestPercentage}%
            Interest Level: ${assessment.interestLevel}

            Aptitude Score: ${assessment.aptitudeScore}
            Aptitude Percentage: ${assessment.aptitudePercentage}%
            Aptitude Level: ${assessment.aptitudeLevel}

            Overall Assessment Percentage:
            ${assessment.overallPercentage}%

            Skill Assessment:
            $skillsText

            Required JSON format:

            {
              "summary": "short overall career prediction summary",
              "recommendations": [
                {
                  "careerName": "career name",
                  "matchPercentage": 85,
                  "reason": "why this career matches",
                  "strengths": [
                    "strength 1",
                    "strength 2"
                  ],
                  "importantSkills": [
                    "skill 1",
                    "skill 2"
                  ]
                }
              ]
            }

            Return 3 to 5 career recommendations.
        """.trimIndent()
    }
}
