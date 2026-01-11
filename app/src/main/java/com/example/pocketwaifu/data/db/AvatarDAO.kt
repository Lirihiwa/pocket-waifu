package com.example.pocketwaifu.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.pocketwaifu.data.models.AvatarEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AvatarDAO {

    @Query("SELECT COUNT(*) FROM ${AvatarEntity.TABLE}")
    suspend fun getAvatarsCount() : Int

    @Query("SELECT * FROM ${AvatarEntity.TABLE}")
    fun getAllAvatars() : Flow<List<AvatarEntity>>

    @Query("SELECT * FROM ${AvatarEntity.TABLE} WHERE id = :avatarId")
    suspend fun getAvatarById(avatarId: Int) : AvatarEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBaseAvatars(avatars: List<AvatarEntity>)
}