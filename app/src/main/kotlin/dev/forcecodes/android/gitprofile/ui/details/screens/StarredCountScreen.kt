package dev.forcecodes.android.gitprofile.ui.details.screens

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun StarredCountScreen() {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Filled.Star,
            contentDescription = null,
            tint = Color(0xFFEAC54F), modifier =
            Modifier.size(22.dp)
        )
        Text(
            text = "0",
            modifier = Modifier.padding(start = 8.dp),
            Color.White
        )
    }
}

@Preview
@Composable
fun StarredCountScreenPreview() {
    StarredCountScreen()
}