package com.dalakoti07.android.memegenerator

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val TAG = "MainViewModel"


sealed class OneTimeEvents {
    data object SelectImageFromGallery : OneTimeEvents()
    data class Error(val message: String) : OneTimeEvents()
    data object ShowAddImageDialog : OneTimeEvents()
    data object ExportAsImage : OneTimeEvents()
    data object ShowAddEmojiDialog : OneTimeEvents()
    data class ApplyTint(val argbColor: Int, val strength: Float) : OneTimeEvents()
    data class ApplySketch(val edgeSigma: Float, val shadeSigma: Float) : OneTimeEvents()
    data class ApplyOilPaint(val radius: Int, val levels: Int) : OneTimeEvents()
}

class MainViewModel : ViewModel() {

    private val _events = Channel<OneTimeEvents>(capacity = Channel.UNLIMITED)
    val events = _events.receiveAsFlow()

    private val _state = MutableStateFlow(
        MainUiStates.initState()
    )
    val state: StateFlow<MainUiStates>
        get() = _state.asStateFlow()

    fun dispatchActions(action: UiAction) {
        Log.d(TAG, "dispatchActions: $action")
        when (action) {
            is UiAction.RemoveImage -> {
                _state.update {
                    state.value.copy(
                        textsInImage = state.value.textsInImage.filter {
                            it.id != action.textViewInImage.id
                        }
                    )
                }
            }

            is UiAction.AddTextViewToImage -> {
                _state.update {
                    state.value.copy(
                        textsInImage = state.value.textsInImage
                                + action.textViewInImage
                    )
                }
            }

            is UiAction.ToggleAddText -> {
                /*if (state.value.editingStage == EditingStage.TEXT) {
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
                }*/
            }

            is UiAction.ToggleAddEmoji -> {
                /*if (state.value.editingStage == EditingStage.EMOJI) {
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
                }*/
            }

            is UiAction.ImageSelected -> {
                _state.update {
                    state.value.copy(
                        isImageSelected = true,
                    )
                }
            }

            is UiAction.ReselectImageFromGallery -> {
                viewModelScope.launch {
                    _events.send(OneTimeEvents.SelectImageFromGallery)
                }
            }

            is UiAction.MenuItemSelected -> {
                _state.update {
                    it.copy(
                        menuItemSelected = if (it.menuItemSelected == action.menuItemSelected)
                            MenuItemOptions.NONE
                        else action.menuItemSelected,
                    )
                }
            }
            is UiAction.AddTextOverImage->{
                viewModelScope.launch {
                    _events.send(OneTimeEvents.ShowAddImageDialog)
                }
            }
            is UiAction.AddEmojiOverImage->{

            }
            is UiAction.ExportAsImage ->{
                viewModelScope.launch {
                    _events.send(OneTimeEvents.ExportAsImage)
                }
            }
            is UiAction.ApplyTint -> {
                viewModelScope.launch {
                    _events.send(OneTimeEvents.ApplyTint(action.argbColor, action.strength))
                }
            }
            is UiAction.ApplySketch -> {
                viewModelScope.launch {
                    _events.send(OneTimeEvents.ApplySketch(action.edgeSigma, action.shadeSigma))
                }
            }
            is UiAction.ApplyOilPaint -> {
                viewModelScope.launch {
                    _events.send(OneTimeEvents.ApplyOilPaint(action.radius, action.levels))
                }
            }
        }
    }

}