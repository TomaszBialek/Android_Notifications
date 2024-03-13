package com.example.fcppushnotificationshttpv1.wifip2p.screen

import android.content.Context
import android.net.NetworkInfo
import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pDeviceList
import android.net.wifi.p2p.WifiP2pGroup
import android.net.wifi.p2p.WifiP2pInfo
import android.net.wifi.p2p.WifiP2pManager
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fcppushnotificationshttpv1.receivers.WiFiDirectBroadcastReceiver
import com.example.fcppushnotificationshttpv1.receivers.WifiDirectListener

@SuppressWarnings("MissingPermission")
@Composable
fun WifiP2pConnectionScreen() {

    val peers = mutableListOf<WifiP2pDevice>()

    val peerListListener = WifiP2pManager.PeerListListener { peerList ->
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
            Log.d("WifiP2pConnectionScreen", "No devices found")
            return@PeerListListener
        }
    }

    val context = LocalContext.current
    val manager: WifiP2pManager =
        context.getSystemService(Context.WIFI_P2P_SERVICE) as WifiP2pManager
    val channel: WifiP2pManager.Channel = manager.initialize(context, context.mainLooper, null)

    WiFiDirectBroadcastReceiver(manager, channel, peerListListener, object : WifiDirectListener {
        override fun stateChangedAction(isWifiP2PEnabled: Boolean) {
            val a = 5
        }

        override fun peersChangedAction(deviceList: WifiP2pDeviceList?) {
            val a = 5
        }

        override fun connectionChangedAction(
            p2pInfo: WifiP2pInfo?,
            networkInfo: NetworkInfo?,
            p2pGroup: WifiP2pGroup?
        ) {
            val a = 5
        }

        override fun thisDeviceChangedAction(p2pDevice: WifiP2pDevice?) {
            val a = 5
        }
    })

    var connectionStatus by rememberSaveable {
        mutableStateOf(false)
    }

    Surface {
        Column {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Connection Status: " + if (connectionStatus) "Discovery Started" else "Discovery not Started",
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.padding(16.dp))
            Row {
                Button(onClick = { /*TODO*/ }) {
                    Text(text = "On/Off")
                }
                Spacer(modifier = Modifier.padding(16.dp))
                Button(onClick = {
                    manager.discoverPeers(channel, object : WifiP2pManager.ActionListener {
                        override fun onSuccess() {
                            connectionStatus = true
                        }

                        override fun onFailure(reason: Int) {
                            // Remember to grant the location permission
                            val toastText = when(reason) {
                                WifiP2pManager.P2P_UNSUPPORTED -> "P2p is unsupported on the device."
                                WifiP2pManager.ERROR -> "Operation failed due to an internal error."
                                WifiP2pManager.BUSY -> "Operation failed because the framework is busy and unable to service the request"
                                else -> ""
                            }

                            Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show()
                            connectionStatus = false
                        }

                    })
                }) {
                    Text(text = "Discover")
                }

            }
            Spacer(modifier = Modifier.padding(16.dp))
            DeviceList()
        }
    }
}

@Preview
@Composable
fun WifiP2pConnectionScreenPreview() {
    WifiP2pConnectionScreen()
}