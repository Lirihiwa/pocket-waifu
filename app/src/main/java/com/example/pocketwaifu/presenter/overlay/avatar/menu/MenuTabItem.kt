package com.example.pocketwaifu.presenter.overlay.avatar.menu

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.pocketwaifu.R

@Composable
fun MenuTabItem(
    onCloseClick: () -> Unit,
    resourceId: Int,
    contentDescription: String,
    enabled: Boolean,
) {

    IconButton(
        onClick = onCloseClick,
        enabled = enabled,
    ) {

        Icon(
            painter = painterResource(resourceId),
            contentDescription = contentDescription,
            tint = Color.Black,
            modifier = Modifier
                .size(20.dp),
        )
    }
}