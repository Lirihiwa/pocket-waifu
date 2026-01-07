package com.example.pocketwaifu.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = AvatarEntity.TABLE)
data class AvatarEntity(

    @PrimaryKey(autoGenerate = true)
        val id: Int = 0,
    @ColumnInfo(name = "avatar_name")
        val avatarName: String = "NoName",
    @ColumnInfo(name = "avatar_description")
        val description: String,
    @ColumnInfo(name = "image_path")
        val imagePath: String,

){
    companion object {
        const val TABLE = "avatars"
    }
}