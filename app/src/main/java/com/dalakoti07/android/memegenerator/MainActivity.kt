package com.dalakoti07.android.memegenerator

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dalakoti07.android.memegenerator.MainUiStates.Companion.incrementingIdForTexts
import com.dalakoti07.android.memegenerator.composables.EditingTypes
import com.dalakoti07.android.memegenerator.composables.TextColorPicker
import com.dalakoti07.android.memegenerator.home.HomeScreen
import com.dalakoti07.android.memegenerator.home.composables.ImagePicker
import com.dalakoti07.android.memegenerator.ui.theme.MemeGeneratorTheme
import com.dalakoti07.android.memegenerator.utils.rememberFlowWithLifecycle

private const val TAG = "MainActivity"

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val state by viewModel.state.collectAsStateWithLifecycle()
            val events = rememberFlowWithLifecycle(flow = viewModel.events)
            MemeGeneratorTheme {
                HomeScreen(
                    states = state,
                    onAction = { action ->
                        viewModel.dispatchActions(
                            action
                        )
                    },
                    events = events,
                )
            }
        }
    }
}

@Composable
fun HomeScreenDeprecated(
    states: MainUiStates,
    onAction: (UiAction) -> Unit = {},
) {
    Log.d(TAG, "HomeScreen: state -> $states")
    var enteredText by remember {
        mutableStateOf("")
    }
    /*Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(
                12.dp,
            ),
    ) {
        *//*ImagePicker(
            isImageSelected = states.isImageSelected,
            onAction = {
                onAction(it)
            },
            states = states,
        )*//*
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
    }*/
}

// todo convert into dialog content for text
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
            states = MainUiStates()
        )
    }
}