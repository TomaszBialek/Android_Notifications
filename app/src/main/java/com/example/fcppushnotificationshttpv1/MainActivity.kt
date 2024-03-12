package com.example.fcppushnotificationshttpv1

import android.Manifest
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pManager
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
import androidx.compose.ui.platform.LocalContext
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
import com.example.fcppushnotificationshttpv1.receivers.WiFiDirectBroadcastReceiver
import com.example.fcppushnotificationshttpv1.ui.theme.FCPPushNotificationsHTTPV1Theme

class MainActivity : ComponentActivity() {

    private val TAG = "MainActivity"
    private val mainViewModel by viewModels<MainViewModel>()

    private val peers = mutableListOf<WifiP2pDevice>()

    private val peerListListener = WifiP2pManager.PeerListListener { peerList ->
        val refreshedPeers = peerList.deviceList
        if (refreshedPeers != peers) {
            peers.clear()
            peers.addAll(refreshedPeers)

            // If an AdapterView is backed by this data, notify it
            // of the change. For instance, if you have a ListView of
            // available peers, trigger an update.
            //TODO:
//            (listAdapter as WiFiPeerListAdapter).notifyDataSetChanged()

            // Perform any other updates needed based on the new list of
            // peers connected to the Wi-Fi P2P network.
        }

        if (peers.isEmpty()) {
            Log.d(TAG, "No devices found")
            return@PeerListListener
        }
    }

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
                val context = LocalContext.current
                val manager: WifiP2pManager = context.getSystemService(Context.WIFI_P2P_SERVICE) as WifiP2pManager
                val channel: WifiP2pManager.Channel = manager.initialize(context, context.mainLooper, null)

                WiFiDirectBroadcastReceiver(manager, channel, peerListListener)

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