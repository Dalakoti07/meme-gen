package com.dalakoti07.android.memegenerator.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.toArgb
import android.widget.Toast
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import androidx.core.graphics.toColorInt
import com.dalakoti07.android.memegenerator.ui.theme.MemeGeneratorTheme

@Preview
@Composable
fun PreviewTextColorPicker() {
    MemeGeneratorTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color.White
                )
        ) {
            TextColorPicker()
        }
    }
}

@Composable
fun TextColorPicker() {
    var currentColor by remember { mutableStateOf(Color.Black) }
    var showColorPicker by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val presetColors = listOf(Color.Black, Color.Red, Color.Blue, Color.Green, Color.Yellow)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Tap to change text color",
            color = currentColor,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp)
                .clickable { showColorPicker = true }
        )

        // Display preset colors
        Row(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            presetColors.forEach { color ->
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .border(2.dp, Color.Gray, CircleShape)
                        .padding(4.dp)
                        .background(color, CircleShape)
                        .clickable { currentColor = color }
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
        }

        if (showColorPicker) {
            CustomColorPicker { selectedColor ->
                currentColor = selectedColor
                showColorPicker = false
            }
        }
    }
}

@Composable
fun CustomColorPicker(onColorSelected: (Color) -> Unit) {
    var customColor by remember { mutableIntStateOf("#FFFFFF".toColorInt()) }
    Dialog(onDismissRequest = {}) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Choose a color")
            Spacer(modifier = Modifier.height(20.dp))
//            ColorPicker(defaultColor = Color.White) { color ->
//                customColor = color.toArgb()
//            }
            Spacer(modifier = Modifier.height(20.dp))
            Button(onClick = { onColorSelected(Color(customColor)) }) {
                Text("Set Color")
            }
        }
    }
}
