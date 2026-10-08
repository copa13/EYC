package com.example.app.assessment

object AssessmentQuestions {

    val questions = listOf(

        // -------------------------
        // INTEREST ASSESSMENT
        // -------------------------

        AssessmentQuestion(
            id = 1,
            type = AssessmentType.INTEREST,
            question = "Which activity would you enjoy most?",
            options = listOf(
                AssessmentOption(
                    "Building or repairing something",
                    5
                ),
                AssessmentOption(
                    "Helping and guiding people",
                    4
                ),
                AssessmentOption(
                    "Working with numbers and data",
                    3
                ),
                AssessmentOption(
                    "Creating designs or content",
                    2
                )
            )
        ),

        AssessmentQuestion(
            id = 2,
            type = AssessmentType.INTEREST,
            question = "Which type of work interests you most?",
            options = listOf(
                AssessmentOption(
                    "Technology and computers",
                    5
                ),
                AssessmentOption(
                    "Business and management",
                    4
                ),
                AssessmentOption(
                    "Science and research",
                    3
                ),
                AssessmentOption(
                    "Arts and creativity",
                    2
                )
            )
        ),

        AssessmentQuestion(
            id = 3,
            type = AssessmentType.INTEREST,
            question = "What would you prefer during a project?",
            options = listOf(
                AssessmentOption(
                    "Solve technical problems",
                    5
                ),
                AssessmentOption(
                    "Lead the team",
                    4
                ),
                AssessmentOption(
                    "Study information",
                    3
                ),
                AssessmentOption(
                    "Create something visual",
                    2
                )
            )
        ),

        AssessmentQuestion(
            id = 4,
            type = AssessmentType.INTEREST,
            question = "Which task sounds most interesting?",
            options = listOf(
                AssessmentOption(
                    "Writing computer programs",
                    5
                ),
                AssessmentOption(
                    "Teaching someone",
                    4
                ),
                AssessmentOption(
                    "Analyzing information",
                    3
                ),
                AssessmentOption(
                    "Making graphics",
                    2
                )
            )
        ),

        AssessmentQuestion(
            id = 5,
            type = AssessmentType.INTEREST,
            question = "Which environment would you prefer?",
            options = listOf(
                AssessmentOption(
                    "Technology-focused workplace",
                    5
                ),
                AssessmentOption(
                    "Team and people-focused workplace",
                    4
                ),
                AssessmentOption(
                    "Research-focused workplace",
                    3
                ),
                AssessmentOption(
                    "Creative workplace",
                    2
                )
            )
        ),

        AssessmentQuestion(
            id = 6,
            type = AssessmentType.INTEREST,
            question = "Which problem would you enjoy solving?",
            options = listOf(
                AssessmentOption(
                    "A technical problem",
                    5
                ),
                AssessmentOption(
                    "A people-related problem",
                    4
                ),
                AssessmentOption(
                    "A data-related problem",
                    3
                ),
                AssessmentOption(
                    "A design problem",
                    2
                )
            )
        ),

        AssessmentQuestion(
            id = 7,
            type = AssessmentType.INTEREST,
            question = "Which activity would you choose in free time?",
            options = listOf(
                AssessmentOption(
                    "Learning new technology",
                    5
                ),
                AssessmentOption(
                    "Discussing ideas with others",
                    4
                ),
                AssessmentOption(
                    "Solving puzzles",
                    3
                ),
                AssessmentOption(
                    "Drawing or designing",
                    2
                )
            )
        ),

        AssessmentQuestion(
            id = 8,
            type = AssessmentType.INTEREST,
            question = "Which project sounds best?",
            options = listOf(
                AssessmentOption(
                    "Developing an app",
                    5
                ),
                AssessmentOption(
                    "Organizing an event",
                    4
                ),
                AssessmentOption(
                    "Analyzing survey results",
                    3
                ),
                AssessmentOption(
                    "Designing a poster",
                    2
                )
            )
        ),

        AssessmentQuestion(
            id = 9,
            type = AssessmentType.INTEREST,
            question = "What would you like to learn more about?",
            options = listOf(
                AssessmentOption(
                    "Programming and AI",
                    5
                ),
                AssessmentOption(
                    "Communication and leadership",
                    4
                ),
                AssessmentOption(
                    "Mathematics and statistics",
                    3
                ),
                AssessmentOption(
                    "Design and media",
                    2
                )
            )
        ),

        AssessmentQuestion(
            id = 10,
            type = AssessmentType.INTEREST,
            question = "Which result would make you happiest?",
            options = listOf(
                AssessmentOption(
                    "A working software product",
                    5
                ),
                AssessmentOption(
                    "A successful team project",
                    4
                ),
                AssessmentOption(
                    "A correct analysis",
                    3
                ),
                AssessmentOption(
                    "A creative design",
                    2
                )
            )
        ),

        // -------------------------
        // APTITUDE ASSESSMENT
        // -------------------------

        AssessmentQuestion(
            id = 11,
            type = AssessmentType.APTITUDE,
            question = "What is 15 + 27?",
            options = listOf(
                AssessmentOption("32", 0),
                AssessmentOption("42", 5),
                AssessmentOption("52", 0),
                AssessmentOption("40", 0)
            )
        ),

        AssessmentQuestion(
            id = 12,
            type = AssessmentType.APTITUDE,
            question = "What comes next: 2, 4, 8, 16, ?",
            options = listOf(
                AssessmentOption("20", 0),
                AssessmentOption("24", 0),
                AssessmentOption("32", 5),
                AssessmentOption("36", 0)
            )
        ),

        AssessmentQuestion(
            id = 13,
            type = AssessmentType.APTITUDE,
            question = "If 5 pens cost ₹50, what is the cost of 1 pen?",
            options = listOf(
                AssessmentOption("₹5", 0),
                AssessmentOption("₹10", 5),
                AssessmentOption("₹15", 0),
                AssessmentOption("₹20", 0)
            )
        ),

        AssessmentQuestion(
            id = 14,
            type = AssessmentType.APTITUDE,
            question = "Which number is different?",
            options = listOf(
                AssessmentOption("2", 0),
                AssessmentOption("4", 0),
                AssessmentOption("8", 0),
                AssessmentOption("11", 5)
            )
        ),

        AssessmentQuestion(
            id = 15,
            type = AssessmentType.APTITUDE,
            question = "If A = 1, B = 2, what is D?",
            options = listOf(
                AssessmentOption("3", 0),
                AssessmentOption("4", 5),
                AssessmentOption("5", 0),
                AssessmentOption("6", 0)
            )
        ),

        AssessmentQuestion(
            id = 16,
            type = AssessmentType.APTITUDE,
            question = "What is 25% of 100?",
            options = listOf(
                AssessmentOption("10", 0),
                AssessmentOption("20", 0),
                AssessmentOption("25", 5),
                AssessmentOption("50", 0)
            )
        ),

        AssessmentQuestion(
            id = 17,
            type = AssessmentType.APTITUDE,
            question = "Complete the pattern: 3, 6, 9, 12, ?",
            options = listOf(
                AssessmentOption("14", 0),
                AssessmentOption("15", 5),
                AssessmentOption("16", 0),
                AssessmentOption("18", 0)
            )
        ),

        AssessmentQuestion(
            id = 18,
            type = AssessmentType.APTITUDE,
            question = "A train travels 60 km in 1 hour. How far in 2 hours?",
            options = listOf(
                AssessmentOption("90 km", 0),
                AssessmentOption("100 km", 0),
                AssessmentOption("120 km", 5),
                AssessmentOption("180 km", 0)
            )
        ),

        AssessmentQuestion(
            id = 19,
            type = AssessmentType.APTITUDE,
            question = "Which word is closest in meaning to 'rapid'?",
            options = listOf(
                AssessmentOption("Slow", 0),
                AssessmentOption("Fast", 5),
                AssessmentOption("Weak", 0),
                AssessmentOption("Late", 0)
            )
        ),

        AssessmentQuestion(
            id = 20,
            type = AssessmentType.APTITUDE,
            question = "If all cats are animals, which statement is true?",
            options = listOf(
                AssessmentOption(
                    "All animals are cats",
                    0
                ),
                AssessmentOption(
                    "Some cats are not animals",
                    0
                ),
                AssessmentOption(
                    "Cats are animals",
                    5
                ),
                AssessmentOption(
                    "No cats are animals",
                    0
                )
            )
        )
    )
}
