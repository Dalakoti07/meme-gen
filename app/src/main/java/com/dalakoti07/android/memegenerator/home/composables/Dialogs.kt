package com.dalakoti07.android.memegenerator.home.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.dalakoti07.android.memegenerator.ui.theme.MemeGeneratorTheme


@Composable
fun ShowDialogForTextDetails(onClose: () -> Unit, onDone: (String) -> Unit) {
    Dialog(onDismissRequest = onClose) {
        var enteredText by remember {
            mutableStateOf("")
        }
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text("Enter Text", style = MemeGeneratorTheme.styles.bodyLarge)
                TextField(
                    value = enteredText,
                    onValueChange = {
                        enteredText = it
                    },
                    modifier = Modifier.padding(
                        vertical = 8.dp,
                    )
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Button(onClick = onClose) {
                        Text("Close")
                    }
                    Button(onClick = {
                        if (enteredText.isNotEmpty())
                            onDone(enteredText)
                    }) {
                        Text("Done")
                    }
                }
            }
        }
    }
}