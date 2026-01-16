package com.example.pocketwaifu.presenter.overlay.avatar

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.pocketwaifu.data.models.Emotions

import com.example.pocketwaifu.presenter.overlay.OverlayViewModel
import com.example.pocketwaifu.presenter.overlay.avatar.menu.MenuTab
import com.example.pocketwaifu.services.overlay.OverlayService
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun AvatarOverlayScreen(
    avatarId: Int,
    onClose: () -> Unit,
    onDrag: (Float, Float) -> Unit,
) {
    val overlayViewModel: OverlayViewModel = koinViewModel(
        parameters = { parametersOf(avatarId) }
    )

    val avatar by overlayViewModel.avatar.collectAsState()

    val emotion by overlayViewModel.emotion.collectAsState()

    var isMenuTabExpanded by remember { mutableStateOf(false) }

    val context = LocalContext.current

    val getEmotionsPath = when(emotion) {
        Emotions.JOY -> avatar.imageOfJoyPath
        Emotions.ANGER -> avatar.imageOfAngerPath
        Emotions.LOVE -> avatar.imageOfLovePath
        Emotions.FEAR -> avatar.imageOfFearPath
        Emotions.NEUTRAL -> avatar.imageOfNeutralPath
    }

    Column(
        modifier = Modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                isMenuTabExpanded = !isMenuTabExpanded
            }
            .width(218.dp)
            .height(340.dp)
            .wrapContentHeight()
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    onDrag(dragAmount.x, dragAmount.y)
                }
            },
    ) {

        AvatarOverlay(
            imagePath = getEmotionsPath,
        )
        MenuTab(
            onCloseClick = onClose,
            onChatClick = {
                val intent = Intent(context, OverlayService::class.java).apply {
                    action = OverlayService.ACTION_CHAT_TOGGLE
                    putExtra(OverlayService.INTENT_EXTRA_AVATAR_ID, avatarId)
                }
                context.startService(intent)
            },
            modifier = Modifier
                .fillMaxWidth(),
            expanded = isMenuTabExpanded,
        )
    }
}