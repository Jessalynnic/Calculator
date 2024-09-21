package com.example.gpa_oterjn_calculator

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.remember
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit, // Lambda function to handle changes to the text input
    placeholder: String = "",
    width: Dp = 120.dp,
    height: Dp = 50.dp,
    fontFamily: FontFamily = signikaFontFamily,
    isCourseField: Boolean = false,
    isCreditsField: Boolean = false,
    showError: Boolean = false, // Determines whether to show the error message
    errorMessage: String = "", // The error message to display if `showError` is true
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    autoFocus: Boolean = false // If true, the TextField will be automatically focused when displayed
) {
    // Remember and manage a FocusRequester to control focus on the TextField
    val focusRequester = remember { FocusRequester() }

    // Focus request is triggered when autoFocus is true
    if (autoFocus) {
        LaunchedEffect(Unit) {
            focusRequester.requestFocus() // Requests focus to the TextField when the UI is launched
        }
    }

    // Basic input field
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = placeholder
            )
        },
        textStyle = TextStyle(
            color = Color.Black,
            fontSize = 16.sp,
            fontFamily = fontFamily
        ),
        visualTransformation = visualTransformation,
        isError = showError,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        modifier = Modifier
            .width(width)
            .height(height)
            .clip(RoundedCornerShape(30.dp))
            .background(Color(0xFFEFEEEE))
            .shadow(
                elevation = 5.dp,
                shape = RoundedCornerShape(5.dp)
            )
            .focusRequester(focusRequester) // Binds the TextField to a FocusRequester, allowing for focus control
            .clickable {
                focusRequester.requestFocus() // Request focus when clicked
            }

    )
    // Display an error message if `showError` is true
    if (showError) {
        Text(
            text = errorMessage, // The error message text to display
            color = Color.Red,
            fontSize = 12.sp,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}
