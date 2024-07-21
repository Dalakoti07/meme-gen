package com.dalakoti07.android.memegenerator

import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dalakoti07.android.memegenerator.MainUiStates.Companion.incrementingIdForTexts
import com.dalakoti07.android.memegenerator.composables.EditingTypes
import com.dalakoti07.android.memegenerator.composables.TextColorPicker
import com.dalakoti07.android.memegenerator.ui.theme.MemeGeneratorTheme

private const val TAG = "MainActivity"

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val state by viewModel.state.collectAsStateWithLifecycle()
            MemeGeneratorTheme {
                HomeScreen(
                    states = state,
                    onAction = { action ->
                        viewModel.dispatchActions(
                            action
                        )
                    }
                )
            }
        }
    }
}

/**
 * Are these image picker APIs backward compatible??
 */
@Composable
fun ColumnScope.ImagePicker(
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

    Box(
        modifier = Modifier,
    ) {
        if (isImageSelected) {
            Image(
                bitmap = imageBitmap!!,
                contentDescription = "Selected Image",
                modifier = Modifier
                    .fillMaxWidth()
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.placeholder),
                contentDescription = null,
            )
        }
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
    Button(
        modifier = Modifier.padding(
            top = 10.dp,
        ),
        onClick = {
            if (permissionGranted) {
                imagePickerLauncher.launch("image/*")
            } else {
                requestPermissionLauncher.launch(android.Manifest.permission.READ_MEDIA_IMAGES)
            }
        },
    ) {
        Text(
            if (imageBitmap == null)
                "Pick an Image"
            else "Change Image"
        )
    }
}

@Composable
fun HomeScreen(
    states: MainUiStates,
    onAction: (UiAction) -> Unit = {},
) {
    Log.d(TAG, "HomeScreen: state -> $states")
    var enteredText by remember {
        mutableStateOf("")
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(
                12.dp,
            ),
    ) {
        ImagePicker(
            isImageSelected = states.isImageSelected,
            onAction = {
                onAction(it)
            },
            states = states,
        )
        if (states.isImageSelected) {
            Row(
                modifier = Modifier
                    .padding(
                        top = 10.dp,
                    )
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
            ) {
                EditingTypes(
                    text = "T",
                    subText = "ext",
                    isSelected = states.editingStage == EditingStage.TEXT,
                    onClick = {
                        Log.d(TAG, "HomeScreen: toggle text edit")
                        onAction(UiAction.ToggleAddText)
                    },
                )
                EditingTypes(
                    text = "\uD83D\uDE00",
                    subText = "oji",
                    isSelected = states.editingStage == EditingStage.EMOJI,
                    modifier = Modifier.offset(
                        x = 32.dp,
                    ),
                    onClick = {
                        Log.d(TAG, "HomeScreen: toggle emoji edit")
                        onAction(UiAction.ToggleAddEmoji)
                    },
                )
            }
        }
        if (states.editingStage == EditingStage.TEXT) {
            EnterTextArea(
                enteredText = enteredText,
                onSubmit = {
                    onAction(
                        UiAction.AddTextViewToImage(
                            textNCoordinates = TextNCoordinates(
                                text = enteredText,
                                xOffset = 0,
                                yOffset = 0,
                                id = incrementingIdForTexts++,
                                color = Color.Black,
                            )
                        )
                    )
                },
                onEnter = {
                    enteredText = it
                },
            )
        } else if (states.editingStage == EditingStage.EMOJI) {
            Text(
                modifier = Modifier.padding(
                    top = 20.dp,
                ),
                text = "Emoji Coming Soon",
            )
        }
    }
}

@Composable
fun ColumnScope.EnterTextArea(
    onEnter: (String) -> Unit = {},
    enteredText: String = "",
    onSubmit: () -> Unit = {},
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TextField(
            value = enteredText,
            onValueChange = {
                onEnter(it)
            },
            modifier = Modifier
                .padding(
                    top = 10.dp,
                    end = 10.dp,
                )
                .weight(1f),
        )
        Button(onClick = onSubmit) {
            Text(text = "Done")
        }
    }
    TextColorPicker()
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    MemeGeneratorTheme {
        HomeScreen(
            states = MainUiStates(
                editingStage = EditingStage.TEXT,
            )
        )
    }
}