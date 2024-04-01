package com.example.fcppushnotificationshttpv1.bluetooth.presentation.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.fcppushnotificationshttpv1.bluetooth.presentation.BluetoothViewModel

@Composable
fun BluetoothChatScreen(
    viewModel: BluetoothViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Surface(
        color = MaterialTheme.colorScheme.background
    ) {
        DeviceScreen(
            state = state,
            onStartScan = viewModel::startScan,
            onStopScan = viewModel::stopScan
        )
    }
}