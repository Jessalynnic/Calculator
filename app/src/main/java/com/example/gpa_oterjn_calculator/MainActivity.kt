package com.example.gpa_oterjn_calculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.gpa_oterjn_calculator.ui.theme.GPA_Oterjn_CalculatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GPA_Oterjn_CalculatorTheme {
                Surface (modifier = Modifier.fillMaxSize()){
                    GPACalculator()
                }
            }
        }
    }
}
