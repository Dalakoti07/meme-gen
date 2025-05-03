package com.dalakoti07.android.memegenerator.home.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dalakoti07.android.memegenerator.EditingStage
import com.dalakoti07.android.memegenerator.MainUiStates
import com.dalakoti07.android.memegenerator.MenuItemOptions
import com.dalakoti07.android.memegenerator.home.HomeScreenContent
import com.dalakoti07.android.memegenerator.ui.theme.MemeGeneratorTheme

private val tintColorPalette = listOf(
    Color(0xFF641B2E),
    Color(0xFF626F47),
    Color(0xFFBB3E00),
    Color(0xFF4A102A),
    Color(0xFF213448),
    Color(0xFF06202B),
)

private fun getColorByIndex(index: Int): Color {
    return tintColorPalette[index % tintColorPalette.size]
}

@Preview(showBackground = true)
@Composable
fun PreviewHomeScreenContent() {
    MemeGeneratorTheme {
        HomeScreenContent(
            states = MainUiStates(
                isImageSelected = true,
                menuItemSelected = MenuItemOptions.LOOKS,
            ),
        )
    }
}

@Composable
fun TintOptions(idx: Int) {
    Box(
        modifier = Modifier
            .background(
                color = getColorByIndex(idx),
            )
            .padding(10.dp)
            .size(width = 80.dp, height = 100.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text("Idx $idx", color = Color.White)
    }
}
