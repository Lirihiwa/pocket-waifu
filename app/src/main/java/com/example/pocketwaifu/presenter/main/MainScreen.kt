package com.example.pocketwaifu.presenter.main

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.pocketwaifu.data.models.AvatarEntity
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainScreen() {
    val viewModel: MainViewModel = koinViewModel()
    val avatarList by viewModel.avatars.collectAsStateWithLifecycle()

    MainScreenContent(
        avatars = avatarList,
    )
}

@Composable
fun MainScreenContent(
    avatars: List<AvatarEntity>,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(items = avatars, key = { it.id }) { avatar ->
            AvatarItem(avatar = avatar)
        }
    }
}

@Composable
fun AvatarItem(avatar: AvatarEntity) {
    Card(
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
    )
}