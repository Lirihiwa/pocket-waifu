package com.example.pocketwaifu.presenter.overlay.chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.pocketwaifu.R

@Composable
fun TopNavBar(
    onClose: () -> Unit,
) {

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(24.dp),
        shape = RoundedCornerShape(
            topStartPercent = 50,
            topEndPercent = 50,
            bottomEndPercent = 0,
            bottomStartPercent = 0
        ),
    ) {

        Row(
            modifier = Modifier
                .fillMaxSize(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
        ) {

            IconButton(
                modifier = Modifier
                    .fillMaxHeight(),
                onClick = onClose
            ) {

                Icon(
                    painter = painterResource(R.drawable.close),
                    contentDescription = "Закрыть",
                    tint = Color.Black,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        }
    }
}