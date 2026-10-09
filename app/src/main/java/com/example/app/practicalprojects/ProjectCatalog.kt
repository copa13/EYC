package com.example.app.practicalprojects

enum class CatalogProjectDifficulty {
    BEGINNER,
    INTERMEDIATE,
    ADVANCED
}

data class ProjectTemplate(
    val id: String,
    val title: String,
    val careerCodes: List<String>,
    val difficulty: CatalogProjectDifficulty,
    val description: String,
    val requiredSkills: List<String>,
    val objectives: List<String>,
    val requirements: List<String>,
    val tasks: List<String>
)

object ProjectCatalog {

    private val templates = listOf(
        ProjectTemplate(
            id = "COPA-001",
            title = "Student Record Management System",
            careerCodes = listOf(
                "SOFTWARE_DEVELOPER",
                "WEB_DEVELOPER",
                "DATABASE_ADMINISTRATOR",
                "IT_SUPPORT"
            ),
            difficulty = CatalogProjectDifficulty.BEGINNER,
            description = "Create a system to store and manage student records.",
            requiredSkills = listOf(
                "Computer Fundamentals",
                "Programming",
                "Database",
                "Problem Solving"
            ),
            objectives = listOf(
                "Add student records",
                "View and search records",
                "Update and delete records",
                "Validate user input"
            ),
            requirements = listOf(
                "A development environment",
                "A programming language",
                "A local database or file storage"
            ),
            tasks = listOf(
                "Plan the student record fields",
                "Create the record data model",
                "Implement adding records",
                "Implement searching and viewing",
                "Implement updating and deleting",
                "Validate input",
                "Document the project"
            )
        ),

        ProjectTemplate(
            id = "COPA-002",
            title = "Personal Portfolio Website",
            careerCodes = listOf(
                "WEB_DEVELOPER",
                "UI_UX_DESIGNER",
                "SOFTWARE_DEVELOPER"
            ),
            difficulty = CatalogProjectDifficulty.BEGINNER,
            description = "Build a portfolio showing skills and completed projects.",
            requiredSkills = listOf(
                "HTML",
                "CSS",
                "Web Development",
                "UI/UX",
                "Documentation"
            ),
            objectives = listOf(
                "Create a home page",
                "Display skills and projects",
                "Add contact information",
                "Make the layout responsive"
            ),
            requirements = listOf(
                "A code editor",
                "A web browser",
                "HTML and CSS knowledge"
            ),
            tasks = listOf(
                "Plan the page layout",
                "Create the HTML structure",
                "Style the pages with CSS",
                "Add project descriptions",
                "Improve mobile responsiveness",
                "Check navigation and links",
                "Document the project"
            )
        ),

        ProjectTemplate(
            id = "COPA-003",
            title = "Inventory Management System",
            careerCodes = listOf(
                "SOFTWARE_DEVELOPER",
                "DATABASE_ADMINISTRATOR",
                "IT_SUPPORT",
                "DATA_ANALYST"
            ),
            difficulty = CatalogProjectDifficulty.INTERMEDIATE,
            description = "Track products, quantities and stock changes.",
            requiredSkills = listOf(
                "Programming",
                "Database",
                "Problem Solving",
                "Software Testing",
                "Documentation"
            ),
            objectives = listOf(
                "Add and update products",
                "Track available stock",
                "Search product records",
                "Identify low-stock items"
            ),
            requirements = listOf(
                "A programming environment",
                "A data storage solution",
                "A defined product data model"
            ),
            tasks = listOf(
                "Design product fields",
                "Create product storage",
                "Implement product management",
                "Implement stock updates",
                "Add search and filtering",
                "Create low-stock warnings",
                "Test common scenarios",
                "Document the project"
            )
        ),

        ProjectTemplate(
            id = "COPA-004",
            title = "Cybersecurity Awareness Checker",
            careerCodes = listOf(
                "CYBERSECURITY_ANALYST",
                "IT_SUPPORT",
                "NETWORK_ADMINISTRATOR"
            ),
            difficulty = CatalogProjectDifficulty.INTERMEDIATE,
            description = "Create an educational checker for common digital-safety practices.",
            requiredSkills = listOf(
                "Cybersecurity",
                "Computer Fundamentals",
                "Networking",
                "Critical Thinking",
                "Documentation"
            ),
            objectives = listOf(
                "Teach safe password practices",
                "Identify common phishing warning signs",
                "Provide security recommendations",
                "Display educational results"
            ),
            requirements = listOf(
                "A programming environment",
                "A collection of educational safety rules",
                "A user-friendly interface"
            ),
            tasks = listOf(
                "Define safety-check questions",
                "Create a rule-based assessment",
                "Implement answer evaluation",
                "Display safety recommendations",
                "Add educational examples",
                "Test different answer combinations",
                "Document limitations and usage"
            )
        ),

        ProjectTemplate(
            id = "COPA-005",
            title = "Career Skills and Progress Dashboard",
            careerCodes = listOf(
                "SOFTWARE_DEVELOPER",
                "DATA_ANALYST",
                "WEB_DEVELOPER",
                "IT_SUPPORT"
            ),
            difficulty = CatalogProjectDifficulty.ADVANCED,
            description = "Track career skills, completed tasks and project progress.",
            requiredSkills = listOf(
                "Programming",
                "Database",
                "Data Analytics",
                "UI/UX",
                "Problem Solving",
                "Software Testing"
            ),
            objectives = listOf(
                "Track skill development",
                "Record project completion",
                "Summarize progress",
                "Show areas needing improvement"
            ),
            requirements = listOf(
                "A programming environment",
                "A persistent storage solution",
                "A defined progress calculation"
            ),
            tasks = listOf(
                "Define skill and project models",
                "Create progress storage",
                "Implement task tracking",
                "Calculate completion percentages",
                "Display progress summaries",
                "Identify improvement areas",
                "Test progress calculations",
                "Document the project"
            )
        )
    )

    fun getAll(): List<ProjectTemplate> = templates.toList()

    fun getById(id: String): ProjectTemplate? {
        return templates.firstOrNull { it.id.equals(id, ignoreCase = true) }
    }

    fun recommend(
        careerCode: String,
        currentSkills: List<String>,
        difficulty: CatalogProjectDifficulty? = null,
        limit: Int = 5
    ): List<ProjectTemplate> {
        if (limit <= 0) return emptyList()

        val normalizedCareer = normalize(careerCode)
        val normalizedSkills = currentSkills
            .map(::normalize)
            .filter(String::isNotBlank)
            .toSet()

        return templates
            .asSequence()
            .filter { difficulty == null || it.difficulty == difficulty }
            .map { project ->
                val careerMatch = project.careerCodes.any {
                    normalize(it) == normalizedCareer
                }

                val matchingSkills = project.requiredSkills.count {
                    normalize(it) in normalizedSkills
                }

                val missingSkills = project.requiredSkills.size - matchingSkills

                val score = when {
                    project.requiredSkills.isEmpty() -> 0
                    else -> (
                        matchingSkills.toDouble() /
                            project.requiredSkills.size.toDouble() * 100.0
                        ).toInt()
                }

                Triple(project, careerMatch, score to missingSkills)
            }
            .sortedWith(
                compareByDescending<Triple<ProjectTemplate, Boolean, Pair<Int, Int>>> {
                    it.second
                }.thenByDescending {
                    it.third.first
                }.thenBy {
                    it.third.second
                }
            )
            .take(limit)
            .map { it.first }
            .toList()
    }

    private fun normalize(value: String): String {
        return value
            .trim()
            .uppercase()
            .replace(Regex("[^A-Z0-9]+"), "_")
            .trim('_')
    }
}
