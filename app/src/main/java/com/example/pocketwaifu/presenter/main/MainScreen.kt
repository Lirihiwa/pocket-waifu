package com.example.pocketwaifu.presenter.main

import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.pocketwaifu.data.models.AvatarEntity
import com.example.pocketwaifu.services.overlay.OverlayService
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainScreen() {
    val context = LocalContext.current
    val viewModel: MainViewModel = koinViewModel()
    val avatarList by viewModel.avatars.collectAsStateWithLifecycle()

    MainScreenContent(
        avatars = avatarList,
        onAvatarClick = { avatar ->

            if (!Settings.canDrawOverlays(context)) {

                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    "package:${context.packageName}".toUri()
                )
                context.startActivity(intent)
            } else {

                val intent = Intent(context, OverlayService::class.java).apply {
                    putExtra(OverlayService.INTENT_EXTRA_AVATAR_ID, avatar.id)
                }
                context.startService(intent)
            }
        }
    )
}

@Composable
fun MainScreenContent(
    avatars: List<AvatarEntity>,
    onAvatarClick: (AvatarEntity) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(items = avatars, key = { it.id }) { avatar ->
            AvatarItem(
                avatar = avatar,
                onClick = { onAvatarClick(avatar) }
            )
        }
    }
}

@Composable
fun AvatarItem(
    avatar: AvatarEntity,
    onClick: () -> Unit = {}
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            ) {
                Text(
                    text = avatar.avatarName,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = avatar.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }

            AsyncImage(
                model = "file:///android_asset/${avatar.imageOfIconPath}",
                contentDescription = "Avatar portrait",
                modifier = Modifier
                    .size(90.dp)
                    .padding(4.dp),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Preview
@Composable
fun MainScreenPreview() {
    val mockAvatars = listOf(
        AvatarEntity(
            id = 1,
            avatarName = "Короне ",
            description = "Описание какое то."
        ),
        AvatarEntity(
            id = 2,
            avatarName = "Мику",
            description = "ВВАВАЫМЫМЛЬЫЛМТЖДЫАТМЖДОЫТАДМТ."
        )
    )

    MainScreenContent(
        avatars = mockAvatars,
        onAvatarClick = {}
    )
}