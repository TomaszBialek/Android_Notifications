package com.example.fcppushnotificationshttpv1.wifip2p.chat.data

import android.annotation.SuppressLint
import android.content.Context
import android.content.IntentFilter
import android.net.NetworkInfo
import android.net.wifi.WpsInfo
import android.net.wifi.p2p.WifiP2pConfig
import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pDeviceList
import android.net.wifi.p2p.WifiP2pGroup
import android.net.wifi.p2p.WifiP2pInfo
import android.net.wifi.p2p.WifiP2pManager
import android.widget.Toast
import com.example.fcppushnotificationshttpv1.core.receivers.WifiDirectListener
import com.example.fcppushnotificationshttpv1.wifip2p.chat.domain.ComposeWifiP2pDevice
import com.example.fcppushnotificationshttpv1.wifip2p.chat.domain.ConnectionResult
import com.example.fcppushnotificationshttpv1.wifip2p.chat.domain.WifiP2pMessage
import com.example.fcppushnotificationshttpv1.wifip2p.chat.domain.toComposeWifiP2pDevice
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import java.io.IOException
import java.lang.Exception
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.ServerSocket
import java.net.Socket

@SuppressLint("MissingPermission")
class AndroidWifiP2pController(
    private val context: Context
) {
    private var dataTransferService: WifiP2pDataTransferService? = null

    private val wifiP2pManager by lazy {
        context.getSystemService(WifiP2pManager::class.java)
    }

    private val channel by lazy {
        wifiP2pManager.initialize(context, context.mainLooper, null)
    }

    private val _devices = MutableStateFlow<List<ComposeWifiP2pDevice>>(emptyList())
    val devices: StateFlow<List<ComposeWifiP2pDevice>> = _devices.asStateFlow()

    private val _isDiscoveringStarted = MutableStateFlow(false)
    val isDiscoveringStarted: StateFlow<Boolean> = _isDiscoveringStarted.asStateFlow()

    private val peerListListener = WifiP2pManager.PeerListListener { peerList ->
        val refreshedPeers = peerList.deviceList.map { it.toComposeWifiP2pDevice() }

        if (refreshedPeers.isEmpty()) {
            Toast.makeText(context, "No Device Found", Toast.LENGTH_SHORT).show()
        }

        _devices.update {
            refreshedPeers
        }
    }

    lateinit var connectionInfoListener: WifiP2pManager.ConnectionInfoListener

//    private val connectionInfoListener = WifiP2pManager.ConnectionInfoListener { wifiP2pInfo ->
//        if (wifiP2pInfo.groupFormed && wifiP2pInfo.isGroupOwner) {
//            startServerSocket()
//
//            Toast.makeText(context, "Host", Toast.LENGTH_SHORT).show()
//        } else if (wifiP2pInfo.groupFormed) {
//            val groupOwnerAddress = wifiP2pInfo.groupOwnerAddress
//            startClientSocket(groupOwnerAddress)
//
//            Toast.makeText(context, "Client", Toast.LENGTH_SHORT).show()
//        }
//    }

    private val wifiDirectBroadcastReceiver = WifiDirectBroadcastReceiver(object : WifiDirectListener{
        override fun stateChangedAction(isWifiP2PEnabled: Boolean) = Unit

        override fun peersChangedAction(deviceList: WifiP2pDeviceList?) {
            wifiP2pManager.requestPeers(channel, peerListListener)
        }

        override fun connectionChangedAction(
            p2pInfo: WifiP2pInfo?,
            networkInfo: NetworkInfo?,
            p2pGroup: WifiP2pGroup?
        ) {
            networkInfo?.let {
                if (it.isConnected) {
                    wifiP2pManager.requestConnectionInfo(channel, connectionInfoListener)
                }
            }
        }

        override fun thisDeviceChangedAction(p2pDevice: WifiP2pDevice?) {
            wifiP2pManager.requestPeers(channel, peerListListener)
        }
    })

    private var currentServerSocket: ServerSocket? = null
    private var currentClientSocket: Socket? = null

    init {
        context.registerReceiver(
            wifiDirectBroadcastReceiver,
            IntentFilter().apply {
                addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION)
                addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION)
                addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION)
                addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION)
            }
        )
    }

    fun startDiscovery() {
        wifiP2pManager.discoverPeers(channel, object : WifiP2pManager.ActionListener {
            override fun onSuccess() {
                _isDiscoveringStarted.update { true }
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
                _isDiscoveringStarted.update { false }
            }

        })

    }

    fun startServerSocket(): Flow<ConnectionResult> {
        return flow {
            currentServerSocket = ServerSocket(8888).apply {
                reuseAddress = true
            }

            var shouldLoop = true
            while (shouldLoop) {
                currentClientSocket = try {
                    currentServerSocket?.accept()
                } catch (e: IOException) {
                    shouldLoop = false
                    null
                }
                emit(ConnectionResult.ConnectionEstablished)
                currentClientSocket?.let {
                    currentServerSocket?.close()
                    val service = WifiP2pDataTransferService(it)
                    dataTransferService = service

                    emitAll(service.listenForIncomingMessages().map {
                        ConnectionResult.TransferSucceeded(it)
                    })
                }
            }

        }.onCompletion {
            closeConnection()
        }.flowOn(Dispatchers.IO)
    }

    fun startClientSocket(groupOwnerAddress: InetAddress?): Flow<ConnectionResult> {
        return flow {
            val hostAddress = groupOwnerAddress?.hostAddress ?: throw Exception("Empty host address")

            currentClientSocket = Socket()
            currentClientSocket?.let { socket ->
                try {
                    socket.connect(InetSocketAddress(hostAddress, 8888), 5_000)
                    emit(ConnectionResult.ConnectionEstablished)

                    WifiP2pDataTransferService(socket).also {
                        dataTransferService = it
                        emitAll(it.listenForIncomingMessages().map {
                            ConnectionResult.TransferSucceeded(it)
                        })
                    }
                } catch (e: IOException) {
                    socket.close()
                    currentClientSocket = null
                    emit(ConnectionResult.Error("Connection was interrupted"))
                }
            }
        }.onCompletion {
            closeConnection()
        }.flowOn(Dispatchers.IO)
    }

    suspend fun trySendMessage(message: String): WifiP2pMessage? {
        if (dataTransferService == null) {
            return null
        }

        return withContext(Dispatchers.IO) {
            val wifiP2pMessage = WifiP2pMessage(
                message = message,
                senderName = currentClientSocket?.inetAddress?.hostName ?: "Unknown name",
                isFromLocalUser = true
            )

            dataTransferService?.sendMessage(wifiP2pMessage.toByteArray())

            wifiP2pMessage
        }
    }

    fun connectToDevice(device: ComposeWifiP2pDevice) {
        val config = WifiP2pConfig().apply {
            deviceAddress = device.deviceAddress
            wps.setup = WpsInfo.PBC
        }

        wifiP2pManager.connect(channel, config, object : WifiP2pManager.ActionListener {
            override fun onSuccess() {
                Toast.makeText(context, "Connected: ${device.deviceAddress}", Toast.LENGTH_LONG).show()
            }

            override fun onFailure(p0: Int) {
                Toast.makeText(context, "Connecting to the device failed", Toast.LENGTH_LONG).show()
            }

        })
    }

    fun closeConnection() {
        wifiP2pManager.requestGroupInfo(channel, WifiP2pManager.GroupInfoListener { group ->
            if (group == null)
                return@GroupInfoListener

            wifiP2pManager.removeGroup(channel, object : WifiP2pManager.ActionListener {
                override fun onSuccess() {
                    currentClientSocket?.close()
                    currentServerSocket?.close()
                    currentClientSocket = null
                    currentServerSocket = null
                    Toast.makeText(context, "Connection removed", Toast.LENGTH_LONG).show()
                }

                override fun onFailure(reason: Int) {
                    val toastText = when (reason) {
                        WifiP2pManager.P2P_UNSUPPORTED -> "P2p is unsupported on the device."
                        WifiP2pManager.ERROR -> "Operation failed due to an internal error."
                        WifiP2pManager.BUSY -> "Operation failed because the framework is busy and unable to service the request"
                        else -> ""
                    }

                    Toast.makeText(context, "Connection removing failed: $toastText", Toast.LENGTH_LONG).show()
                }
            })
        })
    }

    fun release() {
        context.unregisterReceiver(wifiDirectBroadcastReceiver)
        closeConnection()
    }
}

