package com.example.pocketwaifu.presenter.overlay

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun OverlayScreen(
    avatarId: Int,
    onClose: () -> Unit,
    onDrag: (Float, Float) -> Unit
) {
    val overlayViewModel: OverlayViewModel = koinViewModel(
        parameters = { parametersOf(avatarId) }
    )

    val avatar by overlayViewModel.avatar.collectAsState()

    DraggableWaifuOverlay(
            imagePath = avatar?.imageOfFearPath ?: "",
            onClose = { onClose()},
            onDrag = { dx, dy -> onDrag(dx, dy)}
    )
}

@Composable
fun DraggableWaifuOverlay(
    imagePath: String,
    onClose: () -> Unit,
    onDrag: (Float, Float) -> Unit
) {

    Box(
        modifier = Modifier
            .width(219.dp)
            .height(300.dp)
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    onDrag(dragAmount.x, dragAmount.y)
                }
            },
    ) {
        AsyncImage(
            model = "file:///android_asset/${imagePath}",
            contentDescription = "Avatar portrait",
            modifier = Modifier
                .matchParentSize()
                .padding(4.dp),
            contentScale = ContentScale.Crop
        )
        Text(
            text = "Закрыть",
            modifier = Modifier
                .background(Color.LightGray)
                .clickable {
                    onClose()
                }
        )
    }

}