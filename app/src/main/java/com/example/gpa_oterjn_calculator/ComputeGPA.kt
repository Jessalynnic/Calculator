package com.example.gpa_oterjn_calculator


// Function to get the grade point for a given grade
fun getGradePoint(grade: String): Double {
    return when (grade) {
        "A+", "A" -> 4.0
        "A-" -> 3.7
        "B+" -> 3.3
        "B" -> 3.0
        "B-" -> 2.7
        "C+" -> 2.3
        "C" -> 2.0
        "C-" -> 1.7
        "D+" -> 1.3
        "D" -> 1.0
        "F" -> 0.0
        else -> 0.0
    }
}

/*Function to compute the GPA based on a list of courses
  It calculates the GPA by summing the grade points and dividing by the total number of credits*/
fun computeGPA(courses: List<Course>): Double {
    // Initialize variables to store total grade points and total credits
    var totalGradePoints = 0.0
    var totalCredits = 0.0

    // Loop through each course in the list
    for (course in courses) {
        // Get the grade point value for the current course's grade
        val gradePoint = getGradePoint(course.grade)
        // Get the number of credits for the course, convert to double, or use 0.0 if conversion fails
        val courseCredits = course.credits.toDoubleOrNull() ?: 0.0

        // Multiply grade point by credits and add to the total grade points
        totalGradePoints += gradePoint * courseCredits

        // Add the course's credits to the total credits
        totalCredits += courseCredits
    }

    // If total credits is greater than 0, calculate GPA, otherwise return 0.0
    return if (totalCredits > 0) totalGradePoints / totalCredits else 0.0
}