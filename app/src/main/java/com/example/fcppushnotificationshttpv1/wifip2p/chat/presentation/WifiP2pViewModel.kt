package com.example.fcppushnotificationshttpv1.wifip2p.chat.presentation

import android.net.wifi.p2p.WifiP2pManager
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fcppushnotificationshttpv1.wifip2p.chat.data.AndroidWifiP2pController
import com.example.fcppushnotificationshttpv1.wifip2p.chat.domain.ComposeWifiP2pDevice
import com.example.fcppushnotificationshttpv1.wifip2p.chat.domain.ConnectionResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WifiP2pViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val wifiP2pController: AndroidWifiP2pController
) : ViewModel() {

    private val _state = MutableStateFlow(WifiP2pUiState())
    val state = combine(
        wifiP2pController.isDiscoveringStarted,
        wifiP2pController.devices,
        _state
    ) { isDiscoveringStarted, devices, state ->
        state.copy(
            deviceArray = devices,
            isDiscoveringStarted = isDiscoveringStarted,
            messages = state.messages
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), _state.value)

    private var deviceConnectionJob: Job? = null

    init {
        wifiP2pController.connectionInfoListener =
            WifiP2pManager.ConnectionInfoListener { wifiP2pInfo ->
                if (wifiP2pInfo.groupFormed && wifiP2pInfo.isGroupOwner) {
                    deviceConnectionJob =
                        wifiP2pController.startServerSocket().listen()
                } else if (wifiP2pInfo.groupFormed) {
                    val groupOwnerAddress = wifiP2pInfo.groupOwnerAddress
                    deviceConnectionJob = wifiP2pController.startClientSocket(groupOwnerAddress).listen()
                }
            }
    }

    fun startDiscovery() {
        wifiP2pController.startDiscovery()
    }

    fun sendMessage(message: String) {
        viewModelScope.launch {
            val wifiP2pMessage = wifiP2pController.trySendMessage(message)
            if (wifiP2pMessage != null) {
                _state.update {
                    it.copy(
                        messages = it.messages + wifiP2pMessage
                    )
                }
            }
        }
    }

    fun connectToDevice(device: ComposeWifiP2pDevice) {
        wifiP2pController.connectToDevice(device)
    }

    fun disconnectFromDevice() {
        deviceConnectionJob?.cancel()
        wifiP2pController.closeConnection()
    }

    private fun Flow<ConnectionResult>.listen(): Job {
        return onEach { result ->
            when(result) {
                ConnectionResult.ConnectionEstablished -> {
                    _state.update {
                        it.copy(
                            isDiscoveringStarted = false,
                            errorMessage = null
                        )
                    }
                }
                is ConnectionResult.TransferSucceeded -> {
                    _state.update { it.copy(
                        messages = it.messages + result.message
                    ) }
                }
                is ConnectionResult.Error -> {
                    _state.update {
                        it.copy(
                            isDiscoveringStarted = false,
                            errorMessage = result.message
                        )
                    }
                }
            }
        }.catch { throwable ->
            wifiP2pController.closeConnection()
            _state.update {
                it.copy(
                    isDiscoveringStarted = false,
                    errorMessage = throwable.message
                )
            }
        }.launchIn(viewModelScope)
    }

    fun waitForIncomingConnection() {
//        deviceConnectionJob = wifiP2pController.startServerSocket().launchIn(viewModelScope)
    }
}