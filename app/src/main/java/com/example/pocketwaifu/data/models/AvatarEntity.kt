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
    @ColumnInfo(name = "prompt_content")
        val prompt: String = "",
    @ColumnInfo(name = "image_of_icon_path")
        val imageOfIconPath: String = "",
    @ColumnInfo(name = "image_of_neutral_path")
        val imageOfNeutralPath: String = "",
    @ColumnInfo(name = "image_of_joy_path")
        val imageOfJoyPath: String = "",
    @ColumnInfo(name = "image_of_anger_path")
        val imageOfAngerPath: String = "",
    @ColumnInfo(name = "image_of_love_path")
        val imageOfLovePath: String = "",
    @ColumnInfo(name = "image_of_fear_path")
        val imageOfFearPath: String = "",
) {
    companion object {
        const val TABLE = "avatars"
    }
}