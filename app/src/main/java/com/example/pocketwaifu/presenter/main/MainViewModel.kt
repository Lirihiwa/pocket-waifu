package com.example.pocketwaifu.presenter.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pocketwaifu.data.models.AvatarEntity
import com.example.pocketwaifu.data.repository.AvatarRepository
import com.example.pocketwaifu.domain.GetAllAvatarsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    val getAllAvatarsUseCase: GetAllAvatarsUseCase,
    val repository: AvatarRepository,
) : ViewModel() {

    private val _avatars = MutableStateFlow<List<AvatarEntity>>(emptyList())
    val avatars: StateFlow<List<AvatarEntity>>
        get() = _avatars

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.initializeDatabase()

            getAllAvatarsUseCase().collect { list ->
                _avatars.value = list
            }
        }
    }
}