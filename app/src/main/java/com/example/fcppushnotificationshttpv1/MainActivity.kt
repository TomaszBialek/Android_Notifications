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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.animation.doOnEnd
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.fcppushnotificationshttpv1.chat.screen.ChatScreen
import com.example.fcppushnotificationshttpv1.chat.viewmodel.ChatViewModel
import com.example.fcppushnotificationshttpv1.enter_token.EnterTokenDialog
import com.example.fcppushnotificationshttpv1.ui.theme.FCPPushNotificationsHTTPV1Theme

class MainActivity : ComponentActivity() {

    private val chatViewModel by viewModels<ChatViewModel>()
    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                !mainViewModel.isReady.value
            }
            setOnExitAnimationListener { screen ->
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
                    interpolator  = OvershootInterpolator()
                    duration = 500L
                    doOnEnd { screen.remove() }
                }

                zoomX.start()
                zoomY.start()
            }
        }

        requestNotificationPermission()
        setContent {
            FCPPushNotificationsHTTPV1Theme {
                Surface(
                    color = MaterialTheme.colorScheme.background,
                    modifier = Modifier.fillMaxSize()
                ) {
                    val state = chatViewModel.state
                    if (state.isEnteringToken) {
                        EnterTokenDialog(
                            token = state.remoteToken,
                            onTokenChange = chatViewModel::onRemoteTokenChange,
                            onSubmit = chatViewModel::onSubmitRemoteToken
                        )
                    } else {
                        ChatScreen(
                            messageText = state.messageText,
                            onMessageSend = {
                                chatViewModel.sendMessage(isBroadcast = false)
                            },
                            onMessageBroadcast = {
                                chatViewModel.sendMessage(isBroadcast = true)
                            },
                            onMessageChange = chatViewModel::onMessageChange
                        )
                    }
                }
            }
        }
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