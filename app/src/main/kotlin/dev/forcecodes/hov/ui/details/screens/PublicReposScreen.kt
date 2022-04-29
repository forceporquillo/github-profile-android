package dev.forcecodes.hov.ui.details.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.forcecodes.hov.ui.compose.theme.MyApplicationTheme

// TODO
@Composable
fun UserPublicReposScreen() {
    LazyColumn {
        items(10) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(text = "preggo-android", fontSize = 16.sp, color = Color.White)
                Text(
                    modifier = Modifier.padding(vertical = 8.dp),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.White.copy(0.7f),
                    text = "\uD83E\uDD30 An Android application that aims to help mothers track their pregnancy."
                )
                Row {
                    StarredCountScreen()
                    Spacer(modifier = Modifier.padding(start = 8.dp, end = 8.dp))
                    MainLanguageScreen()
                }
            }
            Divider(color = Color.Gray.copy(.4f), thickness = .5.dp)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UserPublicReposScreenPreview() {
    UserPublicReposScreen()
}

