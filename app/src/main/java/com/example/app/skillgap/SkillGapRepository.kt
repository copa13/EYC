package com.example.app.skillgap

import com.example.app.careermatching.CareerCandidate
import com.example.app.careermatching.EycCareerInput

class SkillGapRepository(
    private val engine: SkillGapEngine = SkillGapEngine()
) {

    fun analyzeCareer(
        input: EycCareerInput,
        career: CareerCandidate
    ): SkillGapResult {

        val skillGapInput = SkillGapInput(
            studentId = input.studentId,
            careerCode = career.code,
            careerTitle = career.title,
            requiredSkills = career.skills,
            currentSkills = input.skills
        )

        return engine.analyze(skillGapInput)
    }

    fun analyzeCareers(
        input: EycCareerInput,
        careers: List<CareerCandidate>
    ): SkillGapComparison {

        val inputs = careers.map { career ->

            SkillGapInput(
                studentId = input.studentId,
                careerCode = career.code,
                careerTitle = career.title,
                requiredSkills = career.skills,
                currentSkills = input.skills
            )
        }

        return engine.analyzeCareers(
            studentId = input.studentId,
            careers = inputs
        )
    }

    fun analyzeFromRequiredSkills(
        studentId: String,
        careerCode: String,
        careerTitle: String,
        requiredSkills: List<String>,
        currentSkills: List<String>
    ): SkillGapResult {

        return engine.analyze(
            SkillGapInput(
                studentId = studentId,
                careerCode = careerCode,
                careerTitle = careerTitle,
                requiredSkills = requiredSkills,
                currentSkills = currentSkills
            )
        )
    }
}
