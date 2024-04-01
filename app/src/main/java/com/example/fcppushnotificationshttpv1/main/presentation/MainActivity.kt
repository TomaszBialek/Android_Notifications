package com.example.fcppushnotificationshttpv1.main.presentation

import android.Manifest
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
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
import androidx.core.splashscreen.SplashScreenViewProvider
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.fcppushnotificationshttpv1.core.Screen
import com.example.fcppushnotificationshttpv1.main.domain.MainViewModel
import com.example.fcppushnotificationshttpv1.notification.chat.presentation.NotificationChatScreen
import com.example.fcppushnotificationshttpv1.private_notes.presentation.add_edit_note.components.AddEditNoteScreen
import com.example.fcppushnotificationshttpv1.private_notes.presentation.notes.components.NotesScreen
import com.example.fcppushnotificationshttpv1.ui.theme.FCPPushNotificationsHTTPV1Theme
import com.example.fcppushnotificationshttpv1.wifip2p.chat.presentation.WifiP2pConnectionScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel by viewModels<MainViewModel>()

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                !mainViewModel.isReady.value
            }
            setOnExitAnimationListener(::onExitAnimationListener)
        }

        requestWifiAndNearbyDevicesPermission()

        requestNotificationPermission()

        setContent {
            FCPPushNotificationsHTTPV1Theme {
                Surface(
                    color = MaterialTheme.colorScheme.background,
                    modifier = Modifier.fillMaxSize()
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = Screen.MainScreen.route) {
                        composable(route = Screen.MainScreen.route) {
                            MainScreen(navController = navController)
                        }
                        composable(route = Screen.NotificationChatScreen.route) {
                            NotificationChatScreen()
                        }
                        composable(route = Screen.WifiP2pConnectionScreen.route) {
                            WifiP2pConnectionScreen()
                        }
                        composable(route = Screen.PrivateNotesScreen.route) {
                            NotesScreen(navController)
                        }
                        composable(
                            route = Screen.AddEditPrivateNoteScreen.route +
                                    "?noteId={noteId}&noteColor={noteColor}",
                            arguments = listOf(
                                navArgument(name = "noteId") {
                                    type = NavType.IntType
                                    defaultValue = -1
                                },
                                navArgument(name = "noteColor") {
                                    type = NavType.IntType
                                    defaultValue = -1
                                }
                            )
                        ) {
                            val color = it.arguments?.getInt("noteColor") ?: -1
                            AddEditNoteScreen(navController = navController, noteColor = color)
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

    private fun requestWifiAndNearbyDevicesPermission() {
        val hasLocationPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED

        if (!hasLocationPermission) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                0
            )
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val hasNearbyWifiDevicesPermission = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.NEARBY_WIFI_DEVICES
            ) != PackageManager.PERMISSION_GRANTED

            if (!hasNearbyWifiDevicesPermission) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.NEARBY_WIFI_DEVICES),
                    0
                )
            }
        }
    }
}