package com.example.fcppushnotificationshttpv1.wifip2p.chat.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.NetworkInfo
import android.net.wifi.p2p.WifiP2pConfig
import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pDeviceList
import android.net.wifi.p2p.WifiP2pGroup
import android.net.wifi.p2p.WifiP2pInfo
import android.net.wifi.p2p.WifiP2pManager
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fcppushnotificationshttpv1.core.receivers.WiFiDirectBroadcastReceiver
import com.example.fcppushnotificationshttpv1.core.receivers.WifiDirectListener
import com.example.fcppushnotificationshttpv1.wifip2p.chat.domain.ClientClass
import com.example.fcppushnotificationshttpv1.wifip2p.chat.domain.ServerClass
import com.example.fcppushnotificationshttpv1.wifip2p.chat.domain.viewmodel.WifiP2pViewModel
import com.example.fcppushnotificationshttpv1.wifip2p.chat.presentation.device.DeviceList
import java.util.concurrent.Executors

@SuppressWarnings("MissingPermission")
@Composable
fun WifiP2pConnectionScreen(
    viewModel: WifiP2pViewModel = viewModel()
) {

    val context = LocalContext.current

    val peers by viewModel.peers.collectAsState()
    val deviceNameArray by viewModel.deviceNameArray.collectAsState()
    val deviceArray by viewModel.deviceArray.collectAsState()

    val connectionStatus by viewModel.connectionStatus.collectAsState()
    val messageField by viewModel.message.collectAsState()
    val text by viewModel.text.collectAsState()


    val peerListListener = getPeerListListener(viewModel, context)
    val connectionInfoListener = getConnectionInfoListener(context, viewModel)

    val manager: WifiP2pManager =
        context.getSystemService(Context.WIFI_P2P_SERVICE) as WifiP2pManager
    val channel: WifiP2pManager.Channel = manager.initialize(context, context.mainLooper, null)

    val wifiDirectListener = getWifiDirectListener(manager, channel, connectionInfoListener)

    WiFiDirectBroadcastReceiver(manager, channel, peerListListener, wifiDirectListener)

    val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        // should be empty
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
                Button(onClick = {
                    launcher.launch(intent)
                }) {
                    Text(text = "On/Off")
                }
                Spacer(modifier = Modifier.padding(16.dp))
                Button(onClick = {
                    manager.discoverPeers(channel, object : WifiP2pManager.ActionListener {
                        override fun onSuccess() {
                            viewModel.setConnectionStatus(true)
                        }

                        override fun onFailure(reason: Int) {
                            // Remember to grant the location permission
                            val toastText = when (reason) {
                                WifiP2pManager.P2P_UNSUPPORTED -> "P2p is unsupported on the device."
                                WifiP2pManager.ERROR -> "Operation failed due to an internal error."
                                WifiP2pManager.BUSY -> "Operation failed because the framework is busy and unable to service the request"
                                else -> ""
                            }

                            Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show()
                            viewModel.setConnectionStatus(false)
                        }

                    })
                }) {
                    Text(text = "Discover")
                }

            }
            Spacer(modifier = Modifier.padding(16.dp))

            val onConnectItemClickListener: (WifiP2pDevice) -> Unit =
                getOnConnectItemClickListener(manager, channel, context)
            val onDisconnectItemClickListener: (WifiP2pDevice) -> Unit =
                getOnDisconnectItemClickListener(context, manager, channel, viewModel)

            DeviceList(deviceArray, onConnectItemClickListener, onDisconnectItemClickListener)

            HorizontalDivider(thickness = 10.dp)
            Spacer(modifier = Modifier.padding(16.dp))
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = messageField,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.padding(16.dp))

            Spacer(modifier = Modifier.weight(1f))

            Row {
                TextField(
                    modifier = Modifier.weight(1.0f),
                    value = text,
                    onValueChange = { viewModel.setText(it) },
                    label = { Text("Enter a message") }
                )
                IconButton(onClick = {
                    val executor = Executors.newSingleThreadExecutor()
                    val textInByteArray = text.toByteArray()
                    viewModel.setText("")
                    executor.execute {
                        viewModel.write(textInByteArray)
                    }
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Send"
                    )
                }
            }
        }
    }
}

@SuppressLint("MissingPermission")
@Composable
private fun getOnDisconnectItemClickListener(
    context: Context,
    manager: WifiP2pManager,
    channel: WifiP2pManager.Channel,
    viewModel: WifiP2pViewModel
): (WifiP2pDevice) -> Unit = {
    manager.requestGroupInfo(channel, WifiP2pManager.GroupInfoListener { group ->
        if (group == null)
            return@GroupInfoListener

        manager.removeGroup(channel, object : WifiP2pManager.ActionListener {
            override fun onSuccess() {
                viewModel.closeSocket()
                Toast.makeText(context, "Connection removed", Toast.LENGTH_LONG)
                    .show()
            }

            override fun onFailure(p0: Int) {
                Toast.makeText(
                    context,
                    "Connection removing failed",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    })
}

@SuppressLint("MissingPermission")
@Composable
private fun getOnConnectItemClickListener(
    manager: WifiP2pManager,
    channel: WifiP2pManager.Channel,
    context: Context
): (WifiP2pDevice) -> Unit = { device ->
    val config = WifiP2pConfig().apply {
        deviceAddress = device.deviceAddress
    }

    manager.connect(channel, config, object : WifiP2pManager.ActionListener {
        override fun onSuccess() {
            Toast.makeText(
                context,
                "Connected: ${device.deviceAddress}",
                Toast.LENGTH_LONG
            ).show()
        }

        override fun onFailure(p0: Int) {
            Toast.makeText(
                context,
                "Connecting to the device failed",
                Toast.LENGTH_LONG
            ).show()
        }

    })
}

@Composable
private fun getPeerListListener(
    viewModel: WifiP2pViewModel,
    context: Context
) = WifiP2pManager.PeerListListener { peerList ->
    val refreshedPeers = peerList.deviceList.toList()
    if (refreshedPeers != viewModel.peers) {
        viewModel.setPeers(refreshedPeers)

        val deviceNames = peerList.deviceList.map { it.deviceName }.toTypedArray()
        viewModel.setDeviceNameArray(deviceNames)

        val devices = peerList.deviceList.toTypedArray()
        viewModel.setDeviceArray(devices)

        if (refreshedPeers.isEmpty()) {
            Toast.makeText(context, "No Device Found", Toast.LENGTH_SHORT).show()
            return@PeerListListener
        }
    }

    if (refreshedPeers.isEmpty()) {
        Log.d("WifiP2pConnectionScreen", "No devices found")
        return@PeerListListener
    }
}

@Composable
private fun getConnectionInfoListener(
    context: Context,
    viewModel: WifiP2pViewModel
) = WifiP2pManager.ConnectionInfoListener { wifiP2pInfo ->
    if (wifiP2pInfo.groupFormed && wifiP2pInfo.isGroupOwner) {
        viewModel.startServerSocket()
        Toast.makeText(context, "Host", Toast.LENGTH_SHORT).show()
    } else if (wifiP2pInfo.groupFormed) {
        val groupOwnerAddress = wifiP2pInfo.groupOwnerAddress
        viewModel.startClientSocket(groupOwnerAddress)
        Toast.makeText(context, "Client", Toast.LENGTH_SHORT).show()
    }
}

@Composable
private fun getWifiDirectListener(
    manager: WifiP2pManager,
    channel: WifiP2pManager.Channel,
    connectionInfoListener: WifiP2pManager.ConnectionInfoListener
) = object : WifiDirectListener {
    override fun stateChangedAction(isWifiP2PEnabled: Boolean) = Unit

    override fun peersChangedAction(deviceList: WifiP2pDeviceList?) = Unit

    override fun connectionChangedAction(
        p2pInfo: WifiP2pInfo?,
        networkInfo: NetworkInfo?,
        p2pGroup: WifiP2pGroup?
    ) {
        networkInfo?.let {
            if (it.isConnected) {
                manager.requestConnectionInfo(channel, connectionInfoListener)
            }
        }
    }

    override fun thisDeviceChangedAction(p2pDevice: WifiP2pDevice?) = Unit
}

@Preview
@Composable
fun WifiP2pConnectionScreenPreview() {
    WifiP2pConnectionScreen()
}
