package com.example.app.careerreport

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class SkillAssessmentInput(
    val aptitudeSummary: String,
    val skills: List<ReportSkill>,
    val strengths: List<String>,
    val weaknesses: List<String>
)

data class SkillGapInput(
    val remainingGaps: List<String>,
    val recommendations: List<String>
)

data class ProjectProgressInput(
    val totalProjects: Int,
    val completedProjects: Int,
    val inProgressProjects: Int
)

object CareerReadinessReportAdapter {

    fun build(
        studentName: String,
        careerName: String,
        assessment: SkillAssessmentInput,
        skillGap: SkillGapInput,
        projects: ProjectProgressInput
    ): CareerReadinessReport {

        val averageSkillScore =
            assessment.skills
                .takeIf { it.isNotEmpty() }
                ?.map { it.score }
                ?.average()
                ?.toInt()
                ?: 0

        val projectScore = when {
            projects.totalProjects <= 0 -> 0
            else -> (
                projects.completedProjects
                    .coerceIn(0, projects.totalProjects) * 100
                ) / projects.totalProjects
        }

        val readinessScore = (
            averageSkillScore * 0.7 +
            projectScore * 0.3
        ).toInt().coerceIn(0, 100)

        val status = when {
            readinessScore >= 80 -> "Career Ready"
            readinessScore >= 60 -> "Nearly Ready"
            readinessScore >= 40 -> "Developing"
            else -> "Needs Preparation"
        }

        val generatedAt = SimpleDateFormat(
            "dd MMM yyyy, HH:mm",
            Locale.getDefault()
        ).format(Date())

        val projectSummary = buildString {
            append("Total projects: ${projects.totalProjects}. ")
            append("Completed: ${projects.completedProjects}. ")
            append("In progress: ${projects.inProgressProjects}.")
        }

        val explanation = when {
            readinessScore >= 80 ->
                "Your current skill and project scores indicate strong preparation. Review remaining gaps before applying."
            readinessScore >= 60 ->
                "You have made progress. Strengthen the remaining skills and complete practical projects."
            readinessScore >= 40 ->
                "Continue building core skills and practical experience to improve career readiness."
            else ->
                "Focus on foundational skills, aptitude improvement and completing guided practical projects."
        }

        return CareerReadinessReport(
            studentName = studentName,
            careerName = careerName,
            generatedAt = generatedAt,
            readinessScore = readinessScore,
            readinessStatus = status,
            aptitudeSummary = assessment.aptitudeSummary,
            skillsSummary = assessment.skills,
            projectSummary = projectSummary,
            strengths = assessment.strengths,
            weaknesses = assessment.weaknesses,
            remainingSkillGaps = skillGap.remainingGaps,
            recommendations = skillGap.recommendations,
            readinessExplanation = explanation
        )
    }
}
