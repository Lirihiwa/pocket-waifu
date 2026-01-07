package com.example.pocketwaifu.di

import androidx.room.Room
import com.example.pocketwaifu.data.db.AppDatabase
import com.example.pocketwaifu.data.repository.AvatarRepository
import com.example.pocketwaifu.data.repository.AvatarRepositoryImpl
import com.example.pocketwaifu.data.repository.ChatRepository
import com.example.pocketwaifu.data.repository.ChatRepositoryImpl
import com.example.pocketwaifu.domain.GetAllAvatarsUseCase
import com.example.pocketwaifu.domain.GetAllAvatarsUseCaseImpl
import com.example.pocketwaifu.presenter.main.MainViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "tasks.db"
        ).build()
    }

    single {get<AppDatabase>().chatDao }
    single {get<AppDatabase>().avatarDao }

    single<AvatarRepository> { AvatarRepositoryImpl(get(), get())}
    single<ChatRepository> { ChatRepositoryImpl(get()) }

    single<GetAllAvatarsUseCase> { GetAllAvatarsUseCaseImpl(get()) }

    viewModel { MainViewModel(get(), get()) }
}