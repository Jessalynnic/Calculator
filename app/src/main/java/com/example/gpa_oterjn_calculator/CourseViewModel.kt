package com.example.gpa_oterjn_calculator

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class CourseViewModel : ViewModel() {
    var courses = mutableStateOf(listOf<Course>())
        private set

    fun addCourse(courseName: String, grade: String, credits: String) {
        if (courseName.isNotEmpty() && grade.isNotEmpty() && credits.isNotEmpty()) {
            val newCourse = Course(courseName, grade, credits)
            courses.value += newCourse
        }
    }

    fun removeCourse(course: Course) {
        courses.value -= course
    }

    fun computeGpa(): Double {
        return computeGPA(courses.value)
    }
}