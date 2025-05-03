package com.dalakoti07.android.memegenerator

import androidx.compose.ui.graphics.Color

enum class MenuItemOptions{
    NONE,
    LOOKS,
    EDITING,
    EXPORT,
}

enum class EditingStage {
    TEXT,
    EMOJI,
    CROP,
    Rotate,
    NONE,
}

data class TextViewInImage(
    val text: String,
    val xOffset: Int,
    val yOffset: Int,
    val id: Int,
    val color: Color,
)

data class MainUiStates(
    val menuItemSelected: MenuItemOptions = MenuItemOptions.NONE,
    val isImageSelected: Boolean = false,
    val textsInImage: List<TextViewInImage> = emptyList(),
) {
    companion object {
        var incrementingIdForTexts = 1
        fun initState(): MainUiStates {
            return MainUiStates()
        }
    }
}
