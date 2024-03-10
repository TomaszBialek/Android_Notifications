package com.example.fcppushnotificationshttpv1

import android.Manifest
import android.animation.ObjectAnimator
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
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
import androidx.core.splashscreen.SplashScreenViewProvider
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
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
                Surface(
                    color = MaterialTheme.colorScheme.background,
                    modifier = Modifier.fillMaxSize()
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "MainScreen") {
                        composable(route = "MainScreen") {
                            MainScreen(navController = navController)
                        }
                        composable(
                            route = "NotificationChatScreen/{destination}",
                            arguments = listOf(
                                navArgument(name = "destination") {
                                    type = NavType.StringType
                                }
                            )
                        ) { stackEntry ->
                            val destination = stackEntry.arguments?.getString("destination")
                            Log.i("MainActivity", "Navigation: $destination")
                            NotificationChatScreen()
                        }
                    }
                }
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