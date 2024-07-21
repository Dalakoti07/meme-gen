package com.dalakoti07.android.memegenerator

import androidx.compose.ui.graphics.Color

enum class EditingStage {
    TEXT,
    EMOJI,
    NONE,
}

data class TextNCoordinates(
    val text: String,
    val xOffset: Int,
    val yOffset: Int,
    val id: Int,
    val color: Color,
)

data class MainUiStates(
    val editingStage: EditingStage,
    val isImageSelected: Boolean = false,
    val textsInImage: List<TextNCoordinates> = emptyList(),
) {
    companion object {
        var incrementingIdForTexts = 1
        fun initState(): MainUiStates {
            return MainUiStates(
                editingStage = EditingStage.NONE,
            )
        }
    }
}
