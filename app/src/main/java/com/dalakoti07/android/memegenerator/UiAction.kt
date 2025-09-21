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

    data class ApplyTint(
        val argbColor: Int,
        val strength: Float = 0.4f,
    ) : UiAction()

    data class ApplySketch(
        val edgeSigma: Float = 1.0f,
        val shadeSigma: Float = 10.0f,
    ) : UiAction()

    data class ApplyOilPaint(
        val radius: Int = 3,
        val levels: Int = 16,
    ) : UiAction()

    data class ApplyWatercolor(
        val radius: Int = 4,
    ) : UiAction()

    data class ApplyHalftone(
        val cellSize: Int = 8,
    ) : UiAction()
}
