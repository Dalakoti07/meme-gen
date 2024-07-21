package com.dalakoti07.android.memegenerator

enum class EditingStage {
    TEXT,
    EMOJI,
    NONE,
}

data class MainUiStates(
    val editingStage: EditingStage,
) {
    companion object {
        fun initState(): MainUiStates {
            return MainUiStates(
                editingStage = EditingStage.NONE,
            )
        }
    }
}
