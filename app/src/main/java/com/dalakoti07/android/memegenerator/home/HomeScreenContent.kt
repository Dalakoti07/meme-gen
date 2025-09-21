package com.dalakoti07.android.memegenerator.home

import android.content.Intent
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import com.dalakoti07.android.memegenerator.MainUiStates
import com.dalakoti07.android.memegenerator.MenuItemOptions
import com.dalakoti07.android.memegenerator.OneTimeEvents
import com.dalakoti07.android.memegenerator.UiAction
import com.dalakoti07.android.memegenerator.export.saveBitmapToFile
import com.dalakoti07.android.memegenerator.home.composables.EditingOptions
import com.dalakoti07.android.memegenerator.home.composables.TheCanvasArea
import com.dalakoti07.android.memegenerator.home.composables.TintOptions
import com.dalakoti07.android.memegenerator.home.composables.tintColorPalette
import com.dalakoti07.android.memegenerator.ui.theme.MemeGeneratorTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow

@Preview(showBackground = true)
@Composable
fun PreviewHomeScreenContent() {
    MemeGeneratorTheme {
        HomeScreenContent(
            states = MainUiStates(
                isImageSelected = true,
                menuItemSelected = MenuItemOptions.EDITING,
            ),
        )
    }
}

@Composable
fun HomeScreenContent(
    modifier: Modifier = Modifier,
    onAction: (UiAction) -> Unit = {},
    states: MainUiStates,
    events: Flow<OneTimeEvents> = flow {},
) {
    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        TheCanvasArea(
            isImageSelected = states.isImageSelected,
            onAction = onAction,
            states = states,
            events = events
        )
        if (states.isImageSelected) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color(0xFFfef9f0),
                    )
                    .align(Alignment.BottomCenter)
            ) {
                SubMenuAsPerMenu(states, onAction)
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                ) {
                    MainMenuOptions(
                        text = "Looks",
                        isSelected = states.menuItemSelected == MenuItemOptions.LOOKS,
                        onAction = { onAction(UiAction.MenuItemSelected(MenuItemOptions.LOOKS)) }
                    )
                    MainMenuOptions(
                        text = "Editing",
                        isSelected = states.menuItemSelected == MenuItemOptions.EDITING,
                        onAction = { onAction(UiAction.MenuItemSelected(MenuItemOptions.EDITING)) }
                    )
                    MainMenuOptions(
                        text = "Export",
                        isSelected = states.menuItemSelected == MenuItemOptions.EXPORT,
                        onAction = { onAction(UiAction.MenuItemSelected(MenuItemOptions.EXPORT)) }
                    )
                }
            }
        }
    }
}

@Composable
fun SubMenuAsPerMenu(states: MainUiStates, onAction: (UiAction) -> Unit) {
    when (states.menuItemSelected) {
        MenuItemOptions.LOOKS -> {
            LazyRow(modifier = Modifier.fillMaxWidth()) {
                items(tintColorPalette.size) {
                    TintOptions(it) { action ->
                        onAction(action)
                    }
                }
            }
        }

        MenuItemOptions.EDITING -> {
            LazyRow(modifier = Modifier.fillMaxWidth()) {
                item {
                    EditingOptions(
                        "Text", "Add text on top of Images",
                        onClick = {
                            onAction(UiAction.AddTextOverImage)
                        },
                    )
                }
                item {
                    EditingOptions(
                        "Emoji", "Add Emoji on top of Images",
                        onClick = {
                            onAction(UiAction.AddEmojiOverImage)
                        },
                    )
                }
                item {
                    EditingOptions(
                        "Sketch", "Pencil sketch effect",
                        onClick = {
                            onAction(UiAction.ApplySketch())
                        },
                    )
                }
                item {
                    EditingOptions(
                        "Oil Paint", "Oil painting effect",
                        onClick = {
                            onAction(UiAction.ApplyOilPaint())
                        },
                    )
                }
                item {
                    EditingOptions(
                        "Watercolor", "Kuwahara watercolor",
                        onClick = {
                            onAction(UiAction.ApplyWatercolor())
                        },
                    )
                }
                item {
                    EditingOptions(
                        "Halftone", "Comic dot shading",
                        onClick = {
                            onAction(UiAction.ApplyHalftone())
                        },
                    )
                }
            }
        }

        MenuItemOptions.EXPORT -> {
            LazyRow(modifier = Modifier.fillMaxWidth()) {
                item {
                    EditingOptions(
                        "As Image", "Share as an image now",
                        onClick = {
                            onAction(UiAction.ExportAsImage)
                        },
                    )
                }
            }
        }

        MenuItemOptions.NONE -> {}
    }
}

@Composable
fun RowScope.MainMenuOptions(text: String, isSelected: Boolean, onAction: () -> Unit) {
    Box(
        modifier = Modifier
            .clickable {
                onAction()
            }
            .background(
                color = if (isSelected) Color(0xFFF5ECD5) else Color.Transparent,
            )
            .padding(10.dp)
            .weight(1f),
        contentAlignment = Alignment.Center,
    ) {
        Text(text, style = MemeGeneratorTheme.styles.bodyLarge, modifier = Modifier)
    }
}
