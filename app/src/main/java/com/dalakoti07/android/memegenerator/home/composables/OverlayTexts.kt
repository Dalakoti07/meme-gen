package com.dalakoti07.android.memegenerator.home.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dalakoti07.android.memegenerator.MainUiStates
import com.dalakoti07.android.memegenerator.TextViewInImage
import com.dalakoti07.android.memegenerator.UiAction
import kotlinx.coroutines.delay

@Composable
fun ShowTexts(states: MainUiStates, onAction: (UiAction) -> Unit) {
    states.textsInImage.forEach { text ->
        EachTextOverImage(text,onAction)
    }
}

@Composable
private fun EachTextOverImage(text: TextViewInImage, onAction: (UiAction) -> Unit) {
    // image manipulation
    var showCrossButton by remember {
        mutableStateOf(false)
    }
    var offset by remember { mutableStateOf(Offset.Zero) } // State to hold the current position
    var scale by remember { mutableStateOf(1f) }
    var rotationGlobal by remember { mutableStateOf(0f) }
    val initFontSize = 30
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
            }
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        showCrossButton = true
                    }
                )
            },
    ) {
        Text(
            text = text.text,
            color = text.color,
            modifier = Modifier
                .padding(8.dp),
            fontSize = (initFontSize * scale).sp,
        )
        if(showCrossButton){
            LaunchedEffect(true) {
                delay(2000)
                showCrossButton = false
            }
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