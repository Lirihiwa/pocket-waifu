package com.example.pocketwaifu.services.waifuoverlay

import android.app.Service
import android.content.Context
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
import com.example.pocketwaifu.presenter.overlay.avatar.OverlayScreen

class WaifuOverlayService : Service() {

    private lateinit var windowManager: WindowManager
    private lateinit var lifecycleOwner: WaifuOverlayLifecycleOwner
    private var overlayView: ComposeView? = null
    private lateinit var params: WindowManager.LayoutParams

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()

        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        lifecycleOwner = WaifuOverlayLifecycleOwner()
        lifecycleOwner.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val avatarId = intent?.getIntExtra("AVATAR_ID", -1) ?: -1

        if (avatarId < 0) {
            stopSelf()
        }

        if (overlayView == null) {
            createWaifuView(avatarId)
        }

        lifecycleOwner.onResume()
        return START_STICKY
    }

    private fun createWaifuView(avatarId: Int) {
        params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT,
        )

        params.gravity = Gravity.BOTTOM or Gravity.END
        params.x = 100
        params.y = 100

        overlayView = ComposeView(this).apply {

            setViewTreeLifecycleOwner(lifecycleOwner)
            setViewTreeSavedStateRegistryOwner(lifecycleOwner)
            setViewTreeViewModelStoreOwner(lifecycleOwner)

            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)

            setContent {
                OverlayScreen(
                    avatarId = avatarId,
                    onClose = { stopSelf() },
                    onDrag = { dx, dy ->
                        updatePosition(dx, dy)
                    }
                )
            }
        }

        windowManager.addView(overlayView, params)
    }

    private fun updatePosition(dx: Float, dy: Float, ) {
        params.x -= dx.toInt()
        params.y -= dy.toInt()
        windowManager.updateViewLayout(overlayView, params)
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycleOwner.onDestroy()
        if (overlayView != null) {
            windowManager.removeView(overlayView)
            overlayView = null
        }
    }
}
