package com.dalakoti07.android.memegenerator.home.composables

import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.dalakoti07.android.memegenerator.MainUiStates
import com.dalakoti07.android.memegenerator.R
import com.dalakoti07.android.memegenerator.UiAction

/**
 * Are these image picker APIs backward compatible??
 */
@Composable
fun BoxWithConstraintsScope.ImagePicker(
    isImageSelected: Boolean,
    onAction: (UiAction) -> Unit = {},
    states: MainUiStates,
) {
    // image manipulation
    var offset by remember { mutableStateOf(Offset.Zero) } // State to hold the current position
    var scale by remember { mutableStateOf(1f) }
    var rotationGlobal by remember { mutableStateOf(0f) }

    var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
    val context = LocalContext.current
    var permissionGranted by remember { mutableStateOf(false) }
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            permissionGranted = isGranted
        }
    )

    LaunchedEffect(key1 = true) {
        if (ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            permissionGranted = true
        } else {
            requestPermissionLauncher.launch(android.Manifest.permission.READ_MEDIA_IMAGES)
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
                imageBitmap = bitmap?.asImageBitmap()
            }
        }
    }

    ShowPlaceHolderOrImage(isImageSelected, imageBitmap, onClick = {
        if (permissionGranted) {
            imagePickerLauncher.launch("image/*")
        } else {
            requestPermissionLauncher.launch(android.Manifest.permission.READ_MEDIA_IMAGES)
        }
    })
    states.textsInImage.forEach { text ->
        val initFontSize = 50
        Box(
            modifier = Modifier
                .offset { IntOffset(offset.x.toInt(), offset.y.toInt()) }
                .graphicsLayer(
                    rotationZ = rotationGlobal
                )
                .pointerInput(Unit) {
                    detectTransformGestures { _, pan, zoom, rotate ->
                        offset += pan
                        scale *= zoom
                        rotationGlobal += rotate
                    }
                },
        ) {
            Text(
                text = text.text,
                color = text.color,
                modifier = Modifier
                    .padding(8.dp),
                fontSize = (initFontSize * scale).sp,
            )
            Image(
                imageVector = Icons.Outlined.Close,
                contentDescription = null,
                alignment = Alignment.TopEnd,
                modifier = Modifier
                    .size(20.dp)
                    .background(
                        color = Color.White,
                        shape = CircleShape,
                    )
                    .clickable {
                        onAction(
                            UiAction.RemoveImage(text)
                        )
                    },
            )
        }
    }
}

@Composable
private fun BoxWithConstraintsScope.ShowPlaceHolderOrImage(
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

    if (isImageSelected) {
        Image(
            bitmap = imageBitmap!!,
            contentDescription = "Selected Image",
            modifier = if (isPortrait) {
                // Fill both width and height for portrait
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            } else {
                // Fill width only, height wraps content for landscape
                Modifier
                    .fillMaxWidth()
                    .aspectRatio(imageAspectRatio)
            }
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
