package com.dalakoti07.android.memegenerator

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

private const val TAG = "MainViewModel"


class MainViewModel : ViewModel() {

    private val _state = MutableStateFlow(
        MainUiStates.initState()
    )
    val state: StateFlow<MainUiStates>
        get() = _state.asStateFlow()

    fun dispatchActions(action: UiAction) {
        Log.d(TAG, "dispatchActions: $action")
        when (action) {
            is UiAction.AddTextViewToImage->{
                _state.update {
                    state.value.copy(
                        textsInImage = state.value.textsInImage
                                + action.textNCoordinates
                    )
                }
            }
            is UiAction.ToggleAddText -> {
                if (state.value.editingStage == EditingStage.TEXT) {
                    _state.update {
                        state.value.copy(
                            editingStage = EditingStage.NONE,
                        )
                    }
                } else {
                    _state.update {
                        state.value.copy(
                            editingStage = EditingStage.TEXT,
                        )
                    }
                }
            }
            is UiAction.ToggleAddEmoji -> {
                if (state.value.editingStage == EditingStage.EMOJI) {
                    _state.update {
                        state.value.copy(
                            editingStage = EditingStage.NONE,
                        )
                    }
                } else {
                    _state.update {
                        state.value.copy(
                            editingStage = EditingStage.EMOJI,
                        )
                    }
                }
            }
            is UiAction.ImageSelected -> {
                _state.update {
                    state.value.copy(
                        isImageSelected = true,
                    )
                }
            }
        }
    }

}