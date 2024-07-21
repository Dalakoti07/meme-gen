package com.dalakoti07.android.memegenerator.composables

sealed class UiActions {

    data object ToggleAddText: UiActions()
    data object ToggleAddEmoji: UiActions()
}
