package com.dalakoti07.android.memegenerator.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dalakoti07.android.memegenerator.EditingStage
import com.dalakoti07.android.memegenerator.MainUiStates
import com.dalakoti07.android.memegenerator.OneTimeEvents
import com.dalakoti07.android.memegenerator.UiAction
import com.dalakoti07.android.memegenerator.ui.theme.MemeGeneratorTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    MemeGeneratorTheme {
        HomeScreen(
            states = MainUiStates()
        )
    }
}

@Composable
fun HomeScreen(
    states: MainUiStates,
    onAction: (UiAction) -> Unit = {},
    events: Flow<OneTimeEvents> = flow {},
) {
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                if (states.isImageSelected) "Change" else "Open",
                style = MemeGeneratorTheme.styles.bodyLarge,
                modifier = Modifier.clickable {
                    onAction(UiAction.ReselectImageFromGallery)
                },
            )
            Row {
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Outlined.Info,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(end = 4.dp)
                            .size(32.dp),
                    )
                }
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Outlined.Refresh,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(end = 4.dp)
                            .size(32.dp),
                    )
                }
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Outlined.MoreVert,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(end = 4.dp)
                            .size(32.dp),
                    )
                }
            }
        }
    }) { padd ->
        HomeScreenContent(
            modifier = Modifier.padding(padd),
            onAction = onAction,
            states = states,
            events = events,
        )
    }
}