package com.example.fcppushnotificationshttpv1.receivers

import android.annotation.SuppressLint
import android.net.wifi.p2p.WifiP2pManager
import android.util.Log
import androidx.compose.runtime.Composable

@SuppressLint("MissingPermission")
@Composable
fun WiFiDirectBroadcastReceiver(
    manager: WifiP2pManager,
    channel: WifiP2pManager.Channel,
    peerListListener: WifiP2pManager.PeerListListener
) {

    SystemBroadcastReceiver(
        // Indicates a change in the Wi-Fi Direct status.
        WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION,
        // Indicates a change in the list of available peers.
        WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION,
        // Indicates the state of Wi-Fi Direct connectivity has changed.
        WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION,
        // Indicates this device's details have changed.
        WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION
    ) { intent ->
        when (intent?.action) {
            WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION -> {
                // Determine if Wi-Fi Direct mode is enabled or not, alert
                // the Activity.
                val state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1)
                //TODO:
//                activity.isWifiP2pEnabled = state == WifiP2pManager.WIFI_P2P_STATE_ENABLED
            }

            WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION -> {

                // The peer list has changed! We should probably do something about
                // that.

            }

            WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION -> {

                // Connection state changed! We should probably do something about
                // that.

            }

            WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION -> {

                // Request available peers from the wifi p2p manager. This is an
                // asynchronous call and the calling activity is notified with a
                // callback on PeerListListener.onPeersAvailable()
                manager.requestPeers(channel, peerListListener)
                Log.d("WiFiDirectBroadcastReceiver", "P2P peers changed")


                //TODO:
//                (activity.supportFragmentManager.findFragmentById(R.id.frag_list) as DeviceListFragment)
//                    .apply {
//                        updateThisDevice(
//                            intent.getParcelableExtra(
//                                WifiP2pManager.EXTRA_WIFI_P2P_DEVICE) as WifiP2pDevice
//                        )
//                    }
            }
        }

    }
}
