package com.dalakoti07.android.memegenerator

sealed class UiAction {

    // todo remove this
    data object ToggleAddText : UiAction()

    // todo remove this
    data object ToggleAddEmoji : UiAction()

    data object ImageSelected : UiAction()
    data class AddTextViewToImage(
        val textViewInImage: TextViewInImage,
    ) : UiAction()

    data class RemoveImage(
        val textViewInImage: TextViewInImage,
    ) : UiAction()

    data object ReselectImageFromGallery : UiAction()
    data class MenuItemSelected(val menuItemSelected: MenuItemOptions) : UiAction()
    data object AddTextOverImage : UiAction()
    data object AddEmojiOverImage : UiAction()
    data object ExportAsImage : UiAction()
}
