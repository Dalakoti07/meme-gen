package com.dalakoti07.android.memegenerator.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dalakoti07.android.memegenerator.EditingStage
import com.dalakoti07.android.memegenerator.MainUiStates
import com.dalakoti07.android.memegenerator.UiAction
import com.dalakoti07.android.memegenerator.home.composables.ImagePicker
import com.dalakoti07.android.memegenerator.ui.theme.MemeGeneratorTheme

@Preview(showBackground = true)
@Composable
fun PreviewHomeScreenContent() {
    MemeGeneratorTheme {
        HomeScreenContent(
            states = MainUiStates(
                editingStage = EditingStage.TEXT,
            ),
        )
    }
}

@Composable
fun HomeScreenContent(
    modifier: Modifier = Modifier,
    onAction: (UiAction) -> Unit = {},
    states: MainUiStates,
) {
    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        ImagePicker(
            isImageSelected = states.isImageSelected,
            onAction = onAction,
            states = states,
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {
            MainMenuOptions("Looks")
            MainMenuOptions("Editing")
            MainMenuOptions("Export")
        }
    }
}

@Composable
fun RowScope.MainMenuOptions(text: String) {
    Box(modifier = Modifier
        .padding(10.dp)
        .weight(1f), contentAlignment = Alignment.Center) {
        Text(text, style = MemeGeneratorTheme.styles.bodyLarge, modifier = Modifier)
    }
}
