package com.dalakoti07.android.memegenerator.home.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.dalakoti07.android.memegenerator.UiAction
import com.dalakoti07.android.memegenerator.MainUiStates
import com.dalakoti07.android.memegenerator.MenuItemOptions
import com.dalakoti07.android.memegenerator.home.HomeScreenContent
import com.dalakoti07.android.memegenerator.ui.theme.MemeGeneratorTheme

val tintColorPalette = listOf(
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
fun TintOptions(idx: Int, onTintSelected: (UiAction.ApplyTint) -> Unit = {}) {
    Box(
        modifier = Modifier
            .background(
                color = getColorByIndex(idx),
            )
            .clickable {
                val c = getColorByIndex(idx)
                onTintSelected(
                    UiAction.ApplyTint(
                        argbColor = android.graphics.Color.argb(
                            (0.4f * 255).toInt(),
                            (c.red * 255).toInt(),
                            (c.green * 255).toInt(),
                            (c.blue * 255).toInt()
                        ),
                        strength = 0.4f,
                    )
                )
            }
            .padding(10.dp)
            .size(width = 80.dp, height = 100.dp),
        contentAlignment = Alignment.TopEnd,
    ) {
        Text("$idx", color = Color.White)
    }
}
