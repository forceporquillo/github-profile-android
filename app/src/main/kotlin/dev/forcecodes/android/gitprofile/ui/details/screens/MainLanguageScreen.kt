package dev.forcecodes.android.gitprofile.ui.details.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun MainLanguageScreen() {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Canvas(
            modifier = Modifier.size(16.dp),
            onDraw = {
                val size = 12.dp.toPx()
                drawCircle(
                    color = Color(0xFFA97BFF),
                    radius = size / 2f
                )
            }
        )
        Text(
            text = "Kotlin",
            modifier = Modifier.padding(start = 8.dp),
            color = Color.White
        )
    }
}

@Preview
@Composable
fun MainLanguageScreenPreview() {
    MainLanguageScreen()
}