package com.example.pocketwaifu.presenter.overlay.settings

import android.annotation.SuppressLint
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.pocketwaifu.R
import com.example.pocketwaifu.presenter.overlay.OverlayViewModel
import org.koin.androidx.compose.koinViewModel

@SuppressLint("DefaultLocale")
@Composable
fun SettingsOverlayScreen(
    onClose: () -> Unit,
    onDrag: (Float, Float) -> Unit,
) {

    val overlayViewModel: OverlayViewModel = koinViewModel()

    val avatarScale by overlayViewModel.avatarScale.collectAsState()
    val chatScale by overlayViewModel.chatScale.collectAsState()
    val transparency by overlayViewModel.transparency.collectAsState()

    Card(
        modifier = Modifier
            .width(300.dp)
            .padding(12.dp)
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    onDrag(dragAmount.x, dragAmount.y)
                }
            },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),

    ) {

        Row (
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth()
                .height(24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
        ) {

            IconButton(
                onClick = onClose
            ) {

                Icon(
                    painter = painterResource(R.drawable.close),
                    modifier = Modifier
                        .fillMaxHeight(),
                    contentDescription = "Закрыть настройки"
                )
            }
        }

        SettingsSlider(
            label = "Размер аватара:",
            value = avatarScale,
            onValueChange = { overlayViewModel.setAvatarScale(it) },
            valueText = String.format("%.1fx", avatarScale),
            valueRange = 0.5f..2.0f,
        )

        SettingsSlider(
            label = "Размер чата:",
            value = chatScale,
            onValueChange = { overlayViewModel.setChatScale(it) },
            valueText = String.format("%.1fx", chatScale),
            valueRange = 0.5f..2.0f,
        )

        SettingsSlider(
            label = "Прозрачность:",
            value = transparency,
            onValueChange = { overlayViewModel.setTransparency(it) },
            valueText = "${(transparency * 100).toInt()}%",
            valueRange = 0.1f..1.0f,
        )
    }
}