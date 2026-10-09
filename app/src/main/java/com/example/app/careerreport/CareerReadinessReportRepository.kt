package com.example.app.careerreport

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Contract for the three existing EYC modules.
 *
 * Connect each function to the real data in:
 * #3 Skill Assessment
 * #6 Skill Gap
 * #8 Practical Projects
 */
interface CareerReportDataSource {
    suspend fun getStudentName(): String
    suspend fun getTargetCareer(): String
    suspend fun getSkillAssessment(): SkillAssessmentInput
    suspend fun getSkillGaps(): SkillGapInput
    suspend fun getProjectProgress(): ProjectProgressInput
}

class CareerReadinessReportRepository(
    private val dataSource: CareerReportDataSource
) {
    suspend fun generateReport(): CareerReadinessReport =
        withContext(Dispatchers.IO) {
            val studentName = dataSource.getStudentName()
            val careerName = dataSource.getTargetCareer()
            val assessment = dataSource.getSkillAssessment()
            val gaps = dataSource.getSkillGaps()
            val projects = dataSource.getProjectProgress()

            require(studentName.isNotBlank()) {
                "Student name is missing."
            }
            require(careerName.isNotBlank()) {
                "Target career is missing."
            }
            require(projects.totalProjects >= 0) {
                "Total projects cannot be negative."
            }
            require(projects.completedProjects >= 0) {
                "Completed projects cannot be negative."
            }
            require(projects.inProgressProjects >= 0) {
                "In-progress projects cannot be negative."
            }
            require(
                projects.completedProjects +
                    projects.inProgressProjects <= projects.totalProjects
            ) {
                "Project counts exceed the total project count."
            }

            CareerReadinessReportAdapter.build(
                studentName = studentName,
                careerName = careerName,
                assessment = assessment,
                skillGap = gaps,
                projects = projects
            )
        }
}

