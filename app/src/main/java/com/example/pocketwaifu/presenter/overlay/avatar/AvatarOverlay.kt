package com.example.pocketwaifu.presenter.overlay.avatar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun AvatarOverlay(
    imagePath: String,
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
    ) {

        AsyncImage(
            model = "file:///android_asset/${imagePath}",
            contentDescription = "Avatar portrait",
            modifier = Modifier.matchParentSize(),
            contentScale = ContentScale.Crop
        )
    }
}