package com.example.pocketwaifu.presenter.overlay.avatar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp

import com.example.pocketwaifu.presenter.overlay.OverlayViewModel
import com.example.pocketwaifu.presenter.overlay.avatar.menu.MenuTab
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

    var isMenuTabExpanded by remember { mutableStateOf(false) }

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

        WaifuOverlay(
            imagePath = avatar?.imageOfFearPath ?: "",
        )

        MenuTab(
            onCloseClick = onClose,
            modifier = Modifier
                .fillMaxWidth(),
            expanded = isMenuTabExpanded,
        )
    }
}