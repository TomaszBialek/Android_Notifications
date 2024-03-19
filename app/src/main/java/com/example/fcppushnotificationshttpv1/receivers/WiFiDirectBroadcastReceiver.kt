package com.example.fcppushnotificationshttpv1.receivers

import android.annotation.SuppressLint
import android.net.NetworkInfo
import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pDeviceList
import android.net.wifi.p2p.WifiP2pGroup
import android.net.wifi.p2p.WifiP2pInfo
import android.net.wifi.p2p.WifiP2pManager
import android.os.Build
import android.util.Log
import androidx.compose.runtime.Composable

@SuppressLint("MissingPermission")
@Composable
fun WiFiDirectBroadcastReceiver(
    manager: WifiP2pManager,
    channel: WifiP2pManager.Channel,
    peerListListener: WifiP2pManager.PeerListListener,
    wifiDirectListener: WifiDirectListener
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
                // Determine if Wi-Fi Direct mode is enabled or not, alert the Activity.

                val state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1)
                val isWifiP2PEnabled = state == WifiP2pManager.WIFI_P2P_STATE_ENABLED
                wifiDirectListener.stateChangedAction(isWifiP2PEnabled)
            }

            WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION -> {
                // The peer list has changed! We should probably do something about that.

                val deviceList = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    intent.getParcelableExtra(WifiP2pManager.EXTRA_P2P_DEVICE_LIST, WifiP2pDeviceList::class.java)
                } else {
                    intent.getParcelableExtra(WifiP2pManager.EXTRA_P2P_DEVICE_LIST)
                }

                manager.requestPeers(channel, peerListListener)

                wifiDirectListener.peersChangedAction(deviceList)
            }

            WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION -> {
                // Connection state changed! We should probably do something about that.

                val p2pInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_INFO, WifiP2pInfo::class.java)
                } else {
                    intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_INFO)
                }

                val networkInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO, NetworkInfo::class.java)
                } else {
                    intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO)
                }

                val p2pGroup = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_GROUP, WifiP2pGroup::class.java)
                } else {
                    intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_GROUP)
                }

                wifiDirectListener.connectionChangedAction(p2pInfo, networkInfo, p2pGroup)
            }

            WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION -> {
                // Request available peers from the wifi p2p manager. This is an
                // asynchronous call and the calling activity is notified with a
                // callback on PeerListListener.onPeersAvailable()

                val p2pDevice = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_DEVICE, WifiP2pDevice::class.java)
                } else {
                    intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_DEVICE)
                }

                manager.requestPeers(channel, peerListListener)
                Log.d("WiFiDirectBroadcastReceiver", "P2P peers changed")
                wifiDirectListener.thisDeviceChangedAction(p2pDevice)
            }
        }

    }
}
