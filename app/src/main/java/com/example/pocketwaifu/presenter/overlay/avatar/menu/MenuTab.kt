package com.example.pocketwaifu.presenter.overlay.avatar.menu

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.pocketwaifu.R

@Composable
fun MenuTab(
    onSettingsClick: () -> Unit = {},
    onChatClick: () -> Unit = {},
    onMicClick: () -> Unit = {},
    onCloseClick: () -> Unit = {},
    expanded: Boolean,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {

    val currentHeight by animateDpAsState(
        targetValue = if (expanded) 32.dp else 12.dp,
        animationSpec = tween(durationMillis = 300)
    )

    val currentAlpha by animateFloatAsState(
        targetValue = if (expanded) 1f else 0f,
        animationSpec = tween(durationMillis = 150),
    )

    Row(
        modifier = Modifier.height(32.dp),
        verticalAlignment = Alignment.Top,
    ) {

        Surface(
            modifier = modifier
                .fillMaxWidth()
                .requiredHeight(currentHeight),
            shape = RoundedCornerShape(
                topStartPercent = 0,
                topEndPercent = 0,
                bottomEndPercent = 50,
                bottomStartPercent = 50
            ),
        ) {

            Row(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .alpha(currentAlpha),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {

                MenuTabItem(
                    onCloseClick = onSettingsClick,
                    resourceId = R.drawable.settings,
                    contentDescription = "Настройки",
                    enabled = expanded,
                )

                MenuTabItem(
                    onCloseClick = onChatClick,
                    resourceId = R.drawable.chat,
                    contentDescription = "Чат",
                    enabled = expanded,
                )

                MenuTabItem(
                    onCloseClick = onMicClick,
                    resourceId = R.drawable.mic,
                    contentDescription = "Микрофон",
                    enabled = expanded,
                )

                MenuTabItem(
                    onCloseClick = onCloseClick,
                    resourceId = R.drawable.close,
                    contentDescription = "Закрыть",
                    enabled = expanded,
                )
            }
        }
    }
}