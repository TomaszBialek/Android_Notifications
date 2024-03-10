package com.example.fcppushnotificationshttpv1

import android.Manifest
import android.animation.ObjectAnimator
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.animation.doOnEnd
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.splashscreen.SplashScreenViewProvider
import com.example.fcppushnotificationshttpv1.notification_chat.screen.NotificationChatScreen
import com.example.fcppushnotificationshttpv1.ui.theme.FCPPushNotificationsHTTPV1Theme

class MainActivity : ComponentActivity() {
    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                !mainViewModel.isReady.value
            }
            setOnExitAnimationListener(::onExitAnimationListener)
        }

        requestNotificationPermission()
        setContent {
            FCPPushNotificationsHTTPV1Theme {
                NotificationChatScreen()
            }
        }
    }

    private fun onExitAnimationListener(screen: SplashScreenViewProvider) {
        val zoomX = ObjectAnimator.ofFloat(
            screen.iconView,
            View.SCALE_X,
            0.4f,
            0.0f
        ).apply {
            interpolator = OvershootInterpolator()
            duration = 500L
            doOnEnd { screen.remove() }
        }

        val zoomY = ObjectAnimator.ofFloat(
            screen.iconView,
            View.SCALE_Y,
            0.4f,
            0.0f
        ).apply {
            interpolator = OvershootInterpolator()
            duration = 500L
            doOnEnd { screen.remove() }
        }

        zoomX.start()
        zoomY.start()
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val hasPermission = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

            if (!hasPermission) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    0
                )
            }
        }
    }
}