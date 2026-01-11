package com.example.pocketwaifu.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = ChatEntity.TABLE)
data class ChatEntity(

    @PrimaryKey(autoGenerate = true)
        val id: Int = 0,
    @ColumnInfo(name = "avatar_name")
        val avatarId: Int,
    @ColumnInfo(name = "message_content")
        val text: String?,
    @ColumnInfo(name = "is_user")
        val isUser: Boolean,
    @ColumnInfo(name = "creation_time")
        val timestamp: Long = System.currentTimeMillis(),
){
    companion object {
        const val TABLE = "messages"
    }
}

