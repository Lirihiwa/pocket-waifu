package com.example.pocketwaifu.di

import androidx.room.Room
import com.example.pocketwaifu.data.db.AppDatabase
import com.example.pocketwaifu.data.network.AIApiService
import com.example.pocketwaifu.data.network.PromptService
import com.example.pocketwaifu.data.network.PromptServiceImpl
import com.example.pocketwaifu.data.repository.AvatarRepository
import com.example.pocketwaifu.data.repository.AvatarRepositoryImpl
import com.example.pocketwaifu.data.repository.ChatRepository
import com.example.pocketwaifu.data.repository.ChatRepositoryImpl
import com.example.pocketwaifu.domain.GetAllAvatarsUseCase
import com.example.pocketwaifu.domain.GetAllAvatarsUseCaseImpl
import com.example.pocketwaifu.domain.GetAllMessagesForAvatarUseCase
import com.example.pocketwaifu.domain.GetAllMessagesForAvatarUseCaseImpl
import com.example.pocketwaifu.domain.GetAvatarByIdUseCase
import com.example.pocketwaifu.domain.GetAvatarByIdUseCaseImpl
import com.example.pocketwaifu.domain.SendMessageUseCase
import com.example.pocketwaifu.domain.SendMessageUseCaseImpl
import com.example.pocketwaifu.presenter.chat.SpeechToTextManager
import com.example.pocketwaifu.presenter.chat.SpeechToTextManagerImpl
import com.example.pocketwaifu.presenter.main.MainViewModel
import com.example.pocketwaifu.presenter.overlay.OverlayViewModel
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "tasks.db"
        ).build()
    }


    single {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    single {
        OkHttpClient.Builder()
            .addInterceptor(get<HttpLoggingInterceptor>())
            .build()
    }

    single {
        Retrofit.Builder()
            .baseUrl("https://api.groq.com/openai/v1/")
            .client(get<OkHttpClient>())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AIApiService::class.java)
    }

    single {
        Json {
            ignoreUnknownKeys = true
            isLenient = true
            encodeDefaults = true
        }
    }

    single<PromptService> { PromptServiceImpl() }
    single<SpeechToTextManager> { SpeechToTextManagerImpl(androidContext()) }

    single {get<AppDatabase>().chatDao }
    single {get<AppDatabase>().avatarDao }

    single<AvatarRepository> { AvatarRepositoryImpl(get(), get())}
    single<ChatRepository> { ChatRepositoryImpl(get(), get(), get(), get())}

    single<GetAllAvatarsUseCase> { GetAllAvatarsUseCaseImpl(get()) }
    single<GetAvatarByIdUseCase> { GetAvatarByIdUseCaseImpl(get()) }
    single<GetAllMessagesForAvatarUseCase> { GetAllMessagesForAvatarUseCaseImpl(get()) }
    single<SendMessageUseCase> { SendMessageUseCaseImpl(get()) }

    viewModel { MainViewModel(get(), get()) }
    viewModel { (avatarId: Int) -> OverlayViewModel(
        avatarId = avatarId,
        getAllMessagesForAvatarUseCase = get(),
        getAvatarByIdUseCase = get(),
        sendMessageUseCase = get(),
        speechToTextManager = get(),
    ) }
}