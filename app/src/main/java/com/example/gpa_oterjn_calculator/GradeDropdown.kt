package com.example.gpa_oterjn_calculator

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun GradeDropdown(
    selectedGrade: String,
    grades: List<String>,
    onGradeSelected: (String) -> Unit
) {
    var expanded by remember {
        mutableStateOf(false)
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        }
    ) {
        // TextField to show the selected grade
        TextField(
            value = selectedGrade,
            onValueChange = {},
            placeholder = {
                Text(
                    text = "Grade"
                )
            },
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor()
                .height(50.dp)
                .width(120.dp) // width of text field
                .clip(RoundedCornerShape(30.dp))

        )

        // Dropdown Menu
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            grades.forEach { grade ->
                DropdownMenuItem(
                    text = {
                        Text(text = grade)
                    },
                    onClick = {
                        onGradeSelected(grade)
                        expanded = false
                    }
                )
            }
        }
    }
}