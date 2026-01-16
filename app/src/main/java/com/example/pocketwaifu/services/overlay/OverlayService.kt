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
import kotlin.math.roundToInt

class OverlayService : Service() {

    private lateinit var windowManager: WindowManager
    private lateinit var lifecycleOwner: OverlayLifecycleOwner

    private var avatarOverlayView: ComposeView? = null
    private lateinit var avatarParams: WindowManager.LayoutParams

    private var chatOverlayView: ComposeView? = null
    private var chatParams: WindowManager.LayoutParams? = null

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

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val avatarId = intent?.getIntExtra(INTENT_EXTRA_AVATAR_ID, -1) ?: -1

        if (intent?.action == ACTION_CHAT_TOGGLE) {

            toggleChat(avatarId)
            return START_STICKY
        }

        if (avatarId < 0) {
            stopSelf()
        }

        if (avatarOverlayView == null) {
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

    private fun closeChatView() {

        if (chatOverlayView != null) {

            windowManager.removeView(chatOverlayView)
            chatOverlayView = null
            chatParams = null
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        if (avatarOverlayView != null) {
            closeChatView()
            windowManager.removeView(avatarOverlayView)
            avatarOverlayView = null
        }

        lifecycleOwner.onDestroy()
    }

    companion object {
        const val ACTION_CHAT_TOGGLE = "ACTION_TOGGLE_CHAT"
        const val INTENT_EXTRA_AVATAR_ID = "AVATAR_ID"
    }
}