package com.dalakoti07.android.memegenerator.home.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dalakoti07.android.memegenerator.ui.theme.MemeGeneratorTheme

@Composable
fun EditingOptions(text: String, subtitle: String, onClick:()->Unit) {
    Card(
        modifier = Modifier
            .padding(10.dp)
            .clickable {
                onClick()
            }
            .size(width = 100.dp, height = 80.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp, pressedElevation = 1.dp)
    ) {
        Text(text, modifier = Modifier.padding(4.dp), style = MemeGeneratorTheme.styles.bodyLarge)
        Text(subtitle, modifier = Modifier.padding(4.dp), style = MemeGeneratorTheme.styles.bodyMedium)
    }
}
