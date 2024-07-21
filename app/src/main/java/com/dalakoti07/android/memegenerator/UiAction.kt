package com.dalakoti07.android.memegenerator

sealed class UiAction {

    data object ToggleAddText: UiAction()
    data object ToggleAddEmoji: UiAction()
    data object ImageSelected: UiAction()
    data class AddTextViewToImage(
        val textNCoordinates: TextNCoordinates,
    ): UiAction()
}
