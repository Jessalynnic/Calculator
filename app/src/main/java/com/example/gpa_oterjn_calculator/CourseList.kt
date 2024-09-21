package com.example.gpa_oterjn_calculator

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun CourseList(courseViewModel: CourseViewModel) {
    // LazyColumn to display a scrollable list of courses
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .height(350.dp)
            .padding(top = 5.dp)
    ) {
        // Loop through the list of courses from the ViewModel
        items(courseViewModel.courses.value) { courseItem ->
            // Display each course item as a swipe-to-dismiss item
            SwipeToDismissItem(courseItem, courseViewModel)
        }
    }
}

@Composable
fun SwipeToDismissItem(courseItem: Course, courseViewModel: CourseViewModel) {
    // Track whether the item has been dismissed
    var isDismissed by remember { mutableStateOf(false) }

    // Only show the item if it hasn't been dismissed
    if (!isDismissed) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFdbe5fa))
                .clip(RoundedCornerShape(10.dp))
                .shadow(1.dp) // Shadow for slight elevation effect
                .padding(10.dp)
                .pointerInput(Unit) {
                    // Detect long press gesture to dismiss the item
                    detectTapGestures(onLongPress = {
                        isDismissed = true
                        // Remove the course from ViewModel
                        courseViewModel.removeCourse(courseItem)
                    })
                }
        ) {
            // Row to hold the course details (name, grade, credits) and delete icon
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.width(50.dp))

                // Display course name
                Text(
                    text = courseItem.name,
                    fontFamily = signikaFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 20.sp,
                )

                Spacer(modifier = Modifier.width(65.dp))

                // Display course grade
                Text(
                    text = courseItem.grade,
                    fontFamily = signikaFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 20.sp,
                )

                Spacer(modifier = Modifier.width(89.dp))

                // Display course credits
                Text(
                    text = courseItem.credits,
                    fontFamily = signikaFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 20.sp,
                )

                Spacer(modifier = Modifier.width(35.dp))

                // Delete button with a red trash icon
                IconButton(onClick = {
                    isDismissed = true
                    // Remove the course from ViewModel
                    courseViewModel.removeCourse(courseItem)
                }) {
                    Icon(
                        imageVector = Icons.Default.Delete, // Trash icon
                        contentDescription = "Delete",
                        tint = Color.Red
                    )
                }
            }
        }
    }
}