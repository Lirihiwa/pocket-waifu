package com.example.pocketwaifu.presenter.overlay.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.pocketwaifu.R
import com.example.pocketwaifu.presenter.overlay.OverlayViewModel
import com.example.pocketwaifu.presenter.overlay.chat.list.ChatList
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun ChatOverlayScreen(
    avatarId: Int,
    onClose: () -> Unit,
    onDrag: (Float, Float) -> Unit,
) {

    val overlayViewModel: OverlayViewModel = koinViewModel(
        parameters = { parametersOf(avatarId) }
    )

    val scale by overlayViewModel.chatScale.collectAsState()
    val transparency by overlayViewModel.transparency.collectAsState()

    val messageList by overlayViewModel.messages.collectAsStateWithLifecycle()

    val baseWidth = 180.dp
    val baseHeight = 280.dp

    val dynamicWidth = baseWidth * scale
    val dynamicHeight = baseHeight * scale

    Column(
        modifier = Modifier
            .graphicsLayer(
                scaleX = scale,
                scaleY = scale,
                alpha = transparency,
            )
            .width(dynamicWidth)
            .height(dynamicHeight)
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    onDrag(dragAmount.x, dragAmount.y)
                }
            },
    ) {

        TopNavBar(
            onClose = onClose
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
        ) {

            ChatList(
                messages = messageList,
            )
        }

        MessageTab(
            onSendClick = { message ->
                overlayViewModel.sendMessage(
                    text = message,
                )
            },
        )
    }
}