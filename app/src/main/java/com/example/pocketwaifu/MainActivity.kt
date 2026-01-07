package com.example.pocketwaifu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.pocketwaifu.presenter.main.MainScreen
import com.example.pocketwaifu.ui.theme.PocketWaifuTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PocketWaifuTheme {
                MainScreen()
            }
        }
    }
}