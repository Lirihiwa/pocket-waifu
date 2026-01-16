package com.example.pocketwaifu.presenter.overlay.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pocketwaifu.R

@Composable
fun MessageTab(
    onSendClick: (message: String) -> Unit,
) {

    var message by remember { mutableStateOf("") }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
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
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            BasicTextField(
                modifier = Modifier
                    .weight(1f)
                    .background(Color.Transparent),
                onValueChange = { text ->
                    message = text
                },
                value = message,
                decorationBox = { innerTextField ->
                    if (message.isEmpty()) {
                        Text(
                            text = "Сообщение..."
                        )
                    }
                    innerTextField()
                }
            )

            IconButton(
                modifier = Modifier
                    .fillMaxHeight(),
                onClick = {
                    onSendClick(message)
                    message = ""
                }
            ) {

                Icon(
                    painter = painterResource(R.drawable.send),
                    contentDescription = "Отправить",
                    tint = Color.Black,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        }
    }
}


@Composable
@Preview
fun MessageTabPreview() {

    MessageTab(
        onSendClick = {},
    )
}