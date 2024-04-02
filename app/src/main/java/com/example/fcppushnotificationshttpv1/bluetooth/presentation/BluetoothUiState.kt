package com.example.fcppushnotificationshttpv1.bluetooth.presentation

import com.example.fcppushnotificationshttpv1.bluetooth.domain.chat.BluetoothDevice

data class BluetoothUiState(
    val scannedDevice: List<BluetoothDevice> = emptyList(),
    val pairedDevice: List<BluetoothDevice> = emptyList(),
    val isConnected: Boolean = false,
    val isConnecting: Boolean = false,
    val errorMessage: String? = null
)