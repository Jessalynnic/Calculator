package com.example.gpa_oterjn_calculator



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

fun computeGPA(courses: List<Course>): Double {
    var totalGradePoints = 0.0
    var totalCredits = 0.0

    for (course in courses) {
        val gradePoint = getGradePoint(course.grade)
        val courseCredits = course.credits.toDoubleOrNull() ?: 0.0

        totalGradePoints += gradePoint * courseCredits
        totalCredits += courseCredits
    }

    return if (totalCredits > 0) totalGradePoints / totalCredits else 0.0
}