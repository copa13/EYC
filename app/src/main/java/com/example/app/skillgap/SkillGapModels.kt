package com.example.app.skillgap

data class SkillGapInput(
    val studentId: String,
    val careerCode: String,
    val careerTitle: String,
    val requiredSkills: List<String>,
    val currentSkills: List<String>
)

enum class SkillGapStatus {
    MATCHED,
    PARTIAL,
    MISSING
}

enum class SkillPriority {
    CRITICAL,
    HIGH,
    MEDIUM,
    LOW
}

data class SkillGapItem(
    val skill: String,
    val status: SkillGapStatus,
    val priority: SkillPriority,
    val matchPercentage: Double,
    val explanation: String
)

data class SkillGapResult(
    val studentId: String,
    val careerCode: String,
    val careerTitle: String,

    val requiredSkills: List<String>,
    val currentSkills: List<String>,

    val matchedSkills: List<String>,
    val missingSkills: List<String>,
    val partialSkills: List<String>,

    val skillGapScore: Double,
    val skillGapPercentage: Double,

    val criticalSkills: List<String>,
    val improvementAreas: List<String>,

    val skillItems: List<SkillGapItem>,

    val explanation: String,

    val learningPathSkills: List<String>
)

data class CareerSkillGap(
    val careerCode: String,
    val careerTitle: String,
    val skillGapPercentage: Double,
    val missingSkills: List<String>,
    val criticalSkills: List<String>
)

data class SkillGapComparison(
    val studentId: String,
    val bestCareer: CareerSkillGap?,
    val alternativeCareers: List<CareerSkillGap>
)
