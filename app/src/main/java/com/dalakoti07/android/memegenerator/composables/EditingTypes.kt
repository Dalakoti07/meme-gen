package com.dalakoti07.android.memegenerator.composables

import android.provider.CalendarContract
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dalakoti07.android.memegenerator.ui.theme.MemeGeneratorTheme
import com.dalakoti07.android.memegenerator.ui.theme.Purple40

// todo use this
@Preview
@Composable
fun PreviewEditingTypes() {
    MemeGeneratorTheme {
        Column(
            Modifier
                .background(
                    color = Color.White,
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        10.dp,
                    )
            ) {
                EditingTypes(
                    text = "T",
                    subText = "ext",
                    isSelected = true,
                )
                EditingTypes(
                    text = "\uD83D\uDE00",
                    subText = "oji",
                    isSelected = false,
                    modifier = Modifier.offset(
                        x = 32.dp,
                    ),
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        10.dp,
                    )
            ) {
                EditingTypes(
                    text = "T",
                    subText = "ext",
                    isSelected = false,
                )
                EditingTypes(
                    text = "\uD83D\uDE00",
                    subText = "oji",
                    isSelected = true,
                    modifier = Modifier.offset(
                        x = 32.dp,
                    ),
                )
            }

        }
    }
}

@Composable
fun EditingTypes(
    modifier: Modifier = Modifier,
    text: String,
    subText: String,
    isSelected: Boolean,
    onClick: () -> Unit = {},
) {
    Box(
        modifier = modifier.clickable {
            onClick()
        },
    ) {
        if (isSelected) {
            Text(
                text = "  $subText",
                modifier = Modifier
                    .align(
                        Alignment.CenterEnd
                    )
                    .offset(
                        x = 22.dp, // 32 - 6
                    )
                    .background(
                        color = MemeGeneratorTheme.colors.primary,
                        shape = RoundedCornerShape(topEnd = 10.dp, bottomEnd = 10.dp)
                    )
                    .padding(
                        bottom = 2.dp,
                        end = 8.dp,
                    )
                    .clickable {
                        onClick()
                    },
                color = MemeGeneratorTheme.colors.onPrimary,
            )
        }
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(
                    color = MemeGeneratorTheme.colors.primary,
                    shape = CircleShape,
                )
                .clickable {
                    onClick()
                },
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = text,
                color = MemeGeneratorTheme.colors.onPrimary,
                fontSize = 25.sp,
            )
        }
    }
}

