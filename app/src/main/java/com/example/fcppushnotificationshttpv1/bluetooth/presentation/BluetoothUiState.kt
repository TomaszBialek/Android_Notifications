package com.example.fcppushnotificationshttpv1.bluetooth.presentation

import com.example.fcppushnotificationshttpv1.bluetooth.domain.chat.BluetoothDevice

data class BluetoothUiState(
    val scannedDevice: List<BluetoothDevice> = emptyList(),
    val pairedDevice: List<BluetoothDevice> = emptyList(),
)