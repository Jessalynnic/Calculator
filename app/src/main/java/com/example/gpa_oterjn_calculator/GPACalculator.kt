package com.example.gpa_oterjn_calculator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

val signikaFontFamily = FontFamily(
    Font(R.font.signika_light, FontWeight.Light),
    Font(R.font.signika_regular, FontWeight.Normal),
    Font(R.font.signika_medium, FontWeight.Medium),
    Font(R.font.signika_semibold, FontWeight.SemiBold),
    Font(R.font.signika_bold, FontWeight.Bold)

)

@Composable
fun GPACalculator() {
    val courseViewModel: CourseViewModel = viewModel()

    var course by remember { mutableStateOf("") }
    val grades = listOf("A+", "A", "A-", "B+", "B", "B-", "C+", "C", "C-", "D+", "D", "F")
    var selectedGrade by remember { mutableStateOf("") }
    var credits by remember { mutableStateOf("") }

    var isValidCourse by remember { mutableStateOf(true) }
    var isValidCredits by remember { mutableStateOf(true) }
    var isAllFieldsFilled by remember { mutableStateOf(true) }

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE

    // Wrapping content in scrollable Column only in landscape mode
    val scrollState = rememberScrollState()

    fun gpaBoxColor(gpa: Double): Color {
        return if (gpa == 0.0) Color(0xFFdbe5fa)
        else when {
            gpa < 2.0 -> Color.Red
            gpa < 3.3 -> Color.Yellow
            else -> Color.Green
        }
    }

    fun gpaTextColor(gpa: Double): Color {
        return when {
            gpa == 0.0 -> Color.Black
            gpa < 2.0 -> Color.Red
            gpa < 3.3 -> Color(0xFFebeb00)
            else -> Color.Green
        }
    }

    fun validateCourseFormat(courseName: String): Boolean {
        val coursePattern = Regex("^[A-Za-z]{3}\\d{3}$")
        return coursePattern.matches(courseName)
    }

    fun validateCreditsFormat(credits: String): Boolean {
        return credits.toDoubleOrNull()?.let { it in 1.0..5.0 } ?: false
    }

    // Add course to the view model
    fun addCourse() {

        val isValidCourseFormat = validateCourseFormat(course)
        val isValidCreditsFormat = validateCreditsFormat(credits)
        val areFieldsFilled = course.isNotEmpty() && selectedGrade.isNotEmpty() && credits.isNotEmpty()

        isValidCourse = isValidCourseFormat
        isValidCredits = isValidCreditsFormat
        isAllFieldsFilled = areFieldsFilled

        if (isValidCourseFormat && isValidCreditsFormat && areFieldsFilled) {
            courseViewModel.addCourse(course, selectedGrade, credits)
            // Clear the fields after adding
            course = ""
            selectedGrade = ""
            credits = ""
        }
    }


    Column(
        modifier = if (isLandscape) Modifier
            .fillMaxSize()
            .verticalScroll(scrollState) // Scrollable in landscape mode
        else Modifier.fillMaxSize(),
        horizontalAlignment = if (isLandscape) Alignment.CenterHorizontally else Alignment.CenterHorizontally,
        verticalArrangement = if (isLandscape) Arrangement.Center else Arrangement.Top
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF5384e5))
                .padding(vertical = 30.dp),
            contentAlignment = Alignment.BottomCenter
        ){
            Text(
                text = "GPA Calculator",
                modifier = Modifier.padding(top = 15.dp),
                fontFamily = signikaFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 20.sp,
                color = Color.White
            )
        }

        //Main Box that holds GPA circle display
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 18.dp),
            contentAlignment = Alignment.Center
        ){
            Box( //Outer box for circle
                modifier = Modifier
                    .size(160.dp) // Larger background shadow layer
                    .background( // Custom colored shadow effect
                        color = gpaBoxColor(courseViewModel.gpa.value),
                        shape = CircleShape
                    )
                    .padding(10.dp)
            ){
                Box( //Inner box for circle
                    modifier = Modifier
                        .size(150.dp) //Size of the inner circle
                        .clip(CircleShape)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = "Your GPA is",
                            fontFamily = signikaFontFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = 20.sp,
                        )

                        Spacer(modifier = Modifier.height(5.dp))

                        Text(
                            text = String.format("%.2f", courseViewModel.gpa.value),
                            fontFamily = signikaFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 40.sp,
                            color = gpaTextColor(courseViewModel.gpa.value)
                        )
                    }
                }
            }
        }

        Column {
            //Row that holds Course, Grade and Credits fields
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                ) {
                    //Text field for Course input
                    CustomTextField(
                        value = course,
                        onValueChange = {
                            course = it
                        },
                        placeholder = "Course (ex.BCS430)",
                        isCourseField = true,
                        showError = !isValidCourse || !isAllFieldsFilled,
                        errorMessage = if (!isAllFieldsFilled) "Course field cannot be empty." else "Invalid format. Use 3 letters + 3 digits (e.g., BCS430)"
                    )
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                ) {
                    GradeDropdown(
                        selectedGrade = selectedGrade,
                        grades = grades,
                        onGradeSelected = {
                            selectedGrade = it
                        }
                    )
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                ) {
                    //Text field for Credit input
                    CustomTextField(
                        value = credits,
                        onValueChange = {
                            credits = it
                        },
                        placeholder = "Credits (ex.3.0)",
                        isCreditsField = true,
                        showError = !isValidCredits || !isAllFieldsFilled,
                        errorMessage = if (!isAllFieldsFilled) "Credits field cannot be empty." else "Credits must be between 1.0 and 5.0.",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }
            }

            //Box that holds "Add Course" button
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ){
                Button(
                    onClick = { addCourse() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF5384e5),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(30.dp),
                    modifier = Modifier
                        .width(200.dp)
                        .height(50.dp)
                        .padding(horizontal = 16.dp)
                        .padding(top = 10.dp)
                        .shadow(
                            elevation = 5.dp,
                            shape = RoundedCornerShape(30.dp)
                        )
                ) {
                    Text(
                        text = "Add Course",
                        fontFamily = signikaFontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 18.sp
                    )
                }
            }
        }

        //Row for List Titles
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ){
            Text(
                text = "Course",
                fontFamily = signikaFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp
            )

            Text(
                text = "Grade",
                fontFamily = signikaFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp
            )

            Text(
                text = "Credits",
                fontFamily = signikaFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp
            )
        }

        CourseList(courseViewModel = courseViewModel)

        //Box that holds "Compute GPA" button
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = {
                    courseViewModel.computeGpa()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF5384e5),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(30.dp),
                modifier = Modifier
                    .width(200.dp)
                    .height(50.dp)
                    .padding(horizontal = 16.dp)
                    .padding(top = 10.dp)
                    .shadow(
                        elevation = 5.dp,
                        shape = RoundedCornerShape(30.dp)
                    )
            ) {
                Text(
                    text = "Compute GPA",
                    fontFamily = signikaFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 18.sp
                )
            }
        }
    }
}