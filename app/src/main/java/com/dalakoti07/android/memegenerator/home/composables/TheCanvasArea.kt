package com.dalakoti07.android.memegenerator.home.composables

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import androidx.compose.ui.graphics.toArgb
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.drawToBitmap
import com.dalakoti07.android.memegenerator.MainUiStates
import com.dalakoti07.android.memegenerator.MainUiStates.Companion.incrementingIdForTexts
import com.dalakoti07.android.memegenerator.OneTimeEvents
import com.dalakoti07.android.memegenerator.TextViewInImage
import com.dalakoti07.android.memegenerator.UiAction
import com.dalakoti07.android.memegenerator.export.saveBitmapToFile
import com.dalakoti07.android.memegenerator.nativefilters.NativeFilters
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlin.math.roundToInt

/**
 * Are these image picker APIs backward compatible??
 */
@Composable
fun BoxWithConstraintsScope.TheCanvasArea(
    isImageSelected: Boolean,
    onAction: (UiAction) -> Unit = {},
    states: MainUiStates,
    events: Flow<OneTimeEvents> = flow {},
) {
    // the export part
    val context = LocalContext.current
    val view = LocalView.current
    var composableBounds by remember { mutableStateOf<androidx.compose.ui.geometry.Rect?>(null) }
    LaunchedEffect(events) {
        events.collectLatest {
            when (it) {
                is OneTimeEvents.ExportAsImage -> {
                    composableBounds?.let { bounds ->
                        // 1. Capture full screen
                        val fullBitmap = view.drawToBitmap()

                        // 2. Crop bitmap to the composable's position
                        val scale = context.resources.displayMetrics.density
                        val croppedBitmap = Bitmap.createBitmap(
                            fullBitmap,
                            bounds.left.roundToInt(),
                            bounds.top.roundToInt(),
                            bounds.width.roundToInt(),
                            bounds.height.roundToInt()
                        )
                        val file = saveBitmapToFile(context, croppedBitmap, "img${System.currentTimeMillis()}")
                        val uri = FileProvider.getUriForFile(
                            context,
                            "${context.packageName}.provider",
                            file
                        )

                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "image/png"
                            putExtra(Intent.EXTRA_STREAM, uri)
                            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        }
                        context.startActivity(Intent.createChooser(shareIntent, "Share via"))
                    }
                }
                else -> {}
            }
        }
    }
    // the export part

    var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
    var originalBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var permissionGranted by remember { mutableStateOf(false) }
    val readImagePermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        android.Manifest.permission.READ_MEDIA_IMAGES
    } else {
        android.Manifest.permission.READ_EXTERNAL_STORAGE
    }
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            permissionGranted = isGranted
        }
    )

    LaunchedEffect(key1 = true) {
        if (ContextCompat.checkSelfPermission(
                context,
                readImagePermission
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            permissionGranted = true
        } else {
            requestPermissionLauncher.launch(readImagePermission)
        }
    }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            context.contentResolver.openInputStream(it)?.use { inputStream ->
                val bitmap = BitmapFactory.decodeStream(inputStream)
                onAction(
                    UiAction.ImageSelected
                )
                originalBitmap = bitmap
                imageBitmap = bitmap?.asImageBitmap()
            }
        }
    }

    ShowPlaceHolderOrImage(
        modifier = Modifier.onGloballyPositioned { layoutCoordinates ->
            composableBounds = layoutCoordinates.boundsInWindow()
        },
        isImageSelected = isImageSelected,
        imageBitmap = imageBitmap,
        onClick = {
            if (permissionGranted) {
                imagePickerLauncher.launch("image/*")
            } else {
                requestPermissionLauncher.launch(readImagePermission)
            }
        })
    var showAddImageDialog by remember {
        mutableStateOf(false)
    }
    if (showAddImageDialog) {
        ShowDialogForTextDetails(
            onClose = {
                showAddImageDialog = false
            },
            onDone = {
                showAddImageDialog = false
                onAction(
                    UiAction.AddTextViewToImage(
                        textViewInImage = TextViewInImage(
                            text = it,
                            xOffset = 10,
                            yOffset = 10,
                            id = incrementingIdForTexts++,
                            color = Color.Black,
                        )
                    )
                )
            },
        )
    }
    ShowTexts(states, onAction)
    LaunchedEffect(key1 = events) {
        events.collectLatest {
            when (it) {
                is OneTimeEvents.SelectImageFromGallery -> {
                    if (permissionGranted) {
                        imagePickerLauncher.launch("image/*")
                    } else {
                        requestPermissionLauncher.launch(readImagePermission)
                    }
                }

                is OneTimeEvents.ShowAddImageDialog -> {
                    showAddImageDialog = true
                }

                is OneTimeEvents.ApplyTint -> {
                    originalBitmap?.let { src ->
                        // Work on a mutable copy
                        val mutable = src.copy(Bitmap.Config.ARGB_8888, true)
                        // Apply native tint in-place
                        NativeFilters.applyTint(mutable, it.argbColor, it.strength)
                        imageBitmap = mutable.asImageBitmap()
                    }
                }

                is OneTimeEvents.ApplySketch -> {
                    originalBitmap?.let { src ->
                        val mutable = src.copy(Bitmap.Config.ARGB_8888, true)
                        NativeFilters.applySketch(mutable, it.edgeSigma, it.shadeSigma)
                        imageBitmap = mutable.asImageBitmap()
                    }
                }

                else -> {}
            }
        }
    }
}

@Composable
private fun BoxWithConstraintsScope.ShowPlaceHolderOrImage(
    modifier: Modifier = Modifier,
    isImageSelected: Boolean,
    imageBitmap: ImageBitmap?,
    onClick: () -> Unit,
) {
    val imageAspectRatio by remember(imageBitmap) {
        val ratio =
            if (imageBitmap == null) 0.0f else imageBitmap.width.toFloat() / imageBitmap.height.toFloat()
        mutableFloatStateOf(ratio)
    }

    // Check if the image is portrait
    val isPortrait = imageAspectRatio < 1f

    if (isImageSelected && imageBitmap != null) {
        Image(
            bitmap = imageBitmap,
            contentDescription = "Selected Image",
            modifier =  modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .aspectRatio(imageAspectRatio),
        )
    } else {
        Icon(
            imageVector = Icons.Outlined.AddCircle,
            contentDescription = "Select image",
            modifier = Modifier
                .size(100.dp)
                .align(Alignment.Center)
                .clickable {
                    onClick()
                },
        )
    }
}
