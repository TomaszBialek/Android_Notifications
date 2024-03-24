package com.example.fcppushnotificationshttpv1.wifip2p.chat.presentation.device

import android.net.wifi.p2p.WifiP2pDevice
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun DeviceList(
    list: Array<WifiP2pDevice>,
    onConnectItemClickListener: (WifiP2pDevice) -> Unit,
    onDisconnectItemClickListener: (WifiP2pDevice) -> Unit,
) {
    LazyColumn {
        items(list) { wifiDevice ->
            DeviceItem(wifiDevice, onConnectItemClickListener, onDisconnectItemClickListener)
        }
    }
}

@Preview
@Composable
fun DeviceListPreview() {
    val array = arrayOf(
        WifiP2pDevice().apply { this.deviceAddress = "1" },
        WifiP2pDevice().apply { this.deviceAddress = "2" },
        WifiP2pDevice().apply { this.deviceAddress = "3" }
    )
    DeviceList(array, {

    }, {

    })
}