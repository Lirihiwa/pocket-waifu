package com.example.pocketwaifu.services.overlay

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.IBinder
import android.view.Gravity
import android.view.WindowManager
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.example.pocketwaifu.presenter.overlay.avatar.AvatarOverlayScreen
import com.example.pocketwaifu.presenter.overlay.chat.ChatOverlayScreen
import com.example.pocketwaifu.presenter.overlay.settings.SettingsOverlayScreen
import kotlin.math.roundToInt

class OverlayService : Service() {

    private lateinit var windowManager: WindowManager
    private lateinit var lifecycleOwner: OverlayLifecycleOwner

    private var avatarOverlayView: ComposeView? = null
    private lateinit var avatarParams: WindowManager.LayoutParams

    private var chatOverlayView: ComposeView? = null
    private var chatParams: WindowManager.LayoutParams? = null

    private var settingsOverlayView: ComposeView? = null
    private var settingsParams: WindowManager.LayoutParams? = null

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()

        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        lifecycleOwner = OverlayLifecycleOwner()
        lifecycleOwner.onCreate()
    }

    private fun toggleChat(avatarId: Int) {

        if (chatOverlayView == null) {
            openChatView(avatarId)
        } else {
            closeChatView()
        }
    }

    private fun toggleSettings() {

        if (settingsOverlayView == null) {
            openSettingsView()
        } else {
            closeSettingsView()
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        when (intent?.action) {
            ACTION_CHAT_TOGGLE -> {

                val avatarId = intent.getIntExtra(INTENT_EXTRA_AVATAR_ID, -1) ?: -1
                toggleChat(avatarId)
                return START_STICKY
            }
            ACTION_SETTINGS_TOGGLE -> {

                toggleSettings()
                return START_STICKY
            }
        }

        if (avatarOverlayView == null) {

            val avatarId = intent?.getIntExtra(INTENT_EXTRA_AVATAR_ID, -1) ?: -1
            createAvatarView(avatarId)
        }

        lifecycleOwner.onResume()
        return START_STICKY
    }

    private fun createAvatarView(avatarId: Int) {

        avatarParams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT,
        )

        avatarParams.gravity = Gravity.BOTTOM or Gravity.END
        avatarParams.x = 100
        avatarParams.y = 100

        avatarOverlayView = ComposeView(this).apply {

            setViewTreeLifecycleOwner(lifecycleOwner)
            setViewTreeSavedStateRegistryOwner(lifecycleOwner)
            setViewTreeViewModelStoreOwner(lifecycleOwner)

            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)

            setContent {
                AvatarOverlayScreen(
                    avatarId = avatarId,
                    onClose = { stopSelf() },
                    onDrag = { dx, dy ->
                        updateAvatarViewPosition(dx, dy)
                    }
                )
            }
        }

        windowManager.addView(avatarOverlayView, avatarParams)
    }

    private fun updateAvatarViewPosition(dx: Float, dy: Float, ) {

        avatarParams.x -= dx.roundToInt()
        avatarParams.y -= dy.roundToInt()
        windowManager.updateViewLayout(avatarOverlayView, avatarParams)
    }

    private fun updateChatViewPosition(dx: Float, dy: Float, ) {

        chatParams?.x -= dx.roundToInt()
        chatParams?.y -= dy.roundToInt()
        windowManager.updateViewLayout(chatOverlayView, chatParams)
    }

    private fun updateSettingsViewPosition(dx: Float, dy: Float, ) {

        settingsParams?.x -= dx.roundToInt()
        settingsParams?.y -= dy.roundToInt()
        windowManager.updateViewLayout(settingsOverlayView, settingsParams)
    }

    private fun openChatView(avatarId: Int) {

        chatParams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                    WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
            PixelFormat.TRANSLUCENT,
        )

        chatParams?.gravity = Gravity.BOTTOM or Gravity.END
        chatParams?.x = avatarParams.x + 100
        chatParams?.y = avatarParams.y + 100

        chatOverlayView = ComposeView(this).apply {

            setViewTreeLifecycleOwner(lifecycleOwner)
            setViewTreeSavedStateRegistryOwner(lifecycleOwner)
            setViewTreeViewModelStoreOwner(lifecycleOwner)

            setContent {
                ChatOverlayScreen(
                    avatarId = avatarId,
                    onClose = { closeChatView() },
                    onDrag = { dx, dy ->
                        updateChatViewPosition(dx, dy)
                    }
                )
            }
        }
        windowManager.addView(chatOverlayView, chatParams)
    }

    private fun openSettingsView() {

        settingsParams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                    WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
            PixelFormat.TRANSLUCENT,
        )

        settingsParams?.gravity = Gravity.BOTTOM or Gravity.END
        settingsParams?.x = avatarParams.x + 100
        settingsParams?.y = avatarParams.y + 100

        settingsOverlayView = ComposeView(this).apply {

            setViewTreeLifecycleOwner(lifecycleOwner)
            setViewTreeSavedStateRegistryOwner(lifecycleOwner)
            setViewTreeViewModelStoreOwner(lifecycleOwner)

            setContent {
                SettingsOverlayScreen (
                    onClose = { closeSettingsView() },
                    onDrag = { dx, dy ->
                        updateSettingsViewPosition(dx, dy)
                    }
                )
            }
        }
        windowManager.addView(settingsOverlayView, settingsParams)
    }
    private fun closeChatView() {

        if (chatOverlayView != null) {

            windowManager.removeView(chatOverlayView)
            chatOverlayView = null
            chatParams = null
        }
    }

    private fun closeSettingsView() {

        if (settingsOverlayView != null) {

            windowManager.removeView(settingsOverlayView)
            settingsOverlayView = null
            settingsParams = null
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        if (avatarOverlayView != null) {
            closeChatView()
            closeSettingsView()
            windowManager.removeView(avatarOverlayView)
            avatarOverlayView = null
        }

        lifecycleOwner.onDestroy()
    }

    companion object {
        const val ACTION_CHAT_TOGGLE = "ACTION_TOGGLE_CHAT"

        const val ACTION_SETTINGS_TOGGLE = "ACTION_TOGGLE_SETTINGS"
        const val INTENT_EXTRA_AVATAR_ID = "AVATAR_ID"
    }
}