package com.example.gpa_oterjn_calculator

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class CourseViewModel : ViewModel() {
    var courses = mutableStateOf(listOf<Course>())
        private set

    // variable to store the current GPA
    var gpa = mutableStateOf(0.0)
        private set

    // Function to add a course to a list
    fun addCourse(courseName: String, grade: String, credits: String) {
        if (courseName.isNotEmpty() && grade.isNotEmpty() && credits.isNotEmpty()) {
            val newCourse = Course(courseName, grade, credits)
            courses.value += newCourse
        }
    }

    // Function to remove a course and recalculate GPA
    fun removeCourse(course: Course) {
        courses.value -= course
        recalculateGPA() // Recalculate GPA after removing a course
    }

    fun computeGpa() {
        gpa.value = computeGPA(courses.value)
    }

    // Function to recalculate GPA based on the current list of courses
    private fun recalculateGPA() {
        gpa.value = computeGPA(courses.value)  // Recalculate GPA and update state
    }

    // Function to clear all courses
    fun clearCourses() {
        courses.value = listOf()  // Set the course list to an empty list
        gpa.value = 0.0  // Reset GPA as well
    }
}