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
    // ViewModel instance to manage the state and business logic for the GPA calculator
    val courseViewModel: CourseViewModel = viewModel()

    var course by remember { mutableStateOf("") }
    val grades = listOf("A+", "A", "A-", "B+", "B", "B-", "C+", "C", "C-", "D+", "D", "F")
    var selectedGrade by remember { mutableStateOf("") }
    var credits by remember { mutableStateOf("") }

    // State variables for input validation
    var isValidCourse by remember { mutableStateOf(true) }
    var isValidCredits by remember { mutableStateOf(true) }
    var isAllFieldsFilled by remember { mutableStateOf(true) }

    // State variable to track whether GPA has been computed
    var isGpaComputed by remember { mutableStateOf(false) }

    // Detecting device orientation
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE

    // Wrapping content in scrollable Column only in landscape mode
    val scrollState = rememberScrollState()

    // Function to determine the color of the GPA box based on GPA value
    fun gpaBoxColor(gpa: Double): Color {
        return if (gpa == 0.0) Color(0xFFdbe5fa)
        else when {
            gpa < 2.0 -> Color.Red
            gpa < 3.3 -> Color.Yellow
            else -> Color.Green
        }
    }

    // Function to determine the text color based on GPA value
    fun gpaTextColor(gpa: Double): Color {
        return when {
            gpa == 0.0 -> Color.Black
            gpa < 2.0 -> Color.Red
            gpa < 3.3 -> Color(0xFFebeb00)
            else -> Color.Green
        }
    }

    // Validate the format of the course input (e.g., BCS430)
    fun validateCourseFormat(courseName: String): Boolean {
        val coursePattern = Regex("^[A-Za-z]{3}\\d{3}$")
        return coursePattern.matches(courseName)
    }

    // Validate the format of the credits input (e.g., between 1.0 and 5.0)
    fun validateCreditsFormat(credits: String): Boolean {
        return credits.toDoubleOrNull()?.let { it in 1.0..5.0 } ?: false
    }

    // Function to add a course and reset the input fields after validation
    fun addCourse() {
        // Validate inputs
        val isValidCourseFormat = validateCourseFormat(course)
        val isValidCreditsFormat = validateCreditsFormat(credits)
        val areFieldsFilled = course.isNotEmpty() && selectedGrade.isNotEmpty() && credits.isNotEmpty()

        // Update validation states
        isValidCourse = isValidCourseFormat
        isValidCredits = isValidCreditsFormat
        isAllFieldsFilled = areFieldsFilled

        // If all inputs are valid, add the course and reset the fields
        if (isValidCourseFormat && isValidCreditsFormat && areFieldsFilled) {
            courseViewModel.addCourse(course, selectedGrade, credits)
            // Clear the fields after adding
            course = ""
            selectedGrade = ""
            credits = ""
        }
    }

    // Column that holds the entire layout
    Column(
        modifier = if (isLandscape) Modifier
            .fillMaxSize()
            .verticalScroll(scrollState) // Scrollable in landscape mode
        else Modifier.fillMaxSize(),
        horizontalAlignment = if (isLandscape) Alignment.CenterHorizontally else Alignment.CenterHorizontally,
        verticalArrangement = if (isLandscape) Arrangement.Center else Arrangement.Top
    ) {
        // Header box displaying "GPA Calculator" text
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF5384e5)) // Blue background
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
            Box( // Outer circular box with a background color based on GPA
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
                    // Column for displaying the GPA label and the GPA value
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = "Your GPA is",
                            fontFamily = signikaFontFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = 20.sp,
                        )

                        // Space between text and GPA value
                        Spacer(modifier = Modifier.height(5.dp))

                        // Display the GPA value
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

        // Input fields and buttons for course, grade, and credits
        Column {
            //Row that holds Course, Grade and Credits fields
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                // Column for course input field
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

                // Column for grade dropdown
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                ) {
                    // Dropdown for grade selection
                    GradeDropdown(
                        selectedGrade = selectedGrade,
                        grades = grades,
                        onGradeSelected = {
                            selectedGrade = it
                        }
                    )
                }
                // Column for credits input field
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
                    onClick = { addCourse() }, // Add course when clicked
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

        // Row that holds the list titles for Course, Grade, and Credits
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

        // List of courses added
        CourseList(courseViewModel = courseViewModel)

        //Box that holds "Compute GPA" or "Clear List" button
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = {
                    if (isGpaComputed) {
                        // Clear the course list if GPA is already computed
                        courseViewModel.clearCourses()
                        isGpaComputed = false  // Reset the flag after clearing
                    } else {
                        // Compute GPA and change the button to "Clear List"
                        courseViewModel.computeGpa()
                        isGpaComputed = true  // GPA has been computed
                    }
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
                    // Toggle text between "Compute GPA" and "Clear List" based on the GPA computed state
                    text = if (isGpaComputed) "Clear List" else "Compute GPA",
                    fontFamily = signikaFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 18.sp
                )
            }
        }
    }
}