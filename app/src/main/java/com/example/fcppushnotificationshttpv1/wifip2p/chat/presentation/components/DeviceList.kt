package com.example.fcppushnotificationshttpv1.wifip2p.chat.presentation.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.fcppushnotificationshttpv1.wifip2p.chat.domain.ComposeWifiP2pDevice

@Composable
fun DeviceList(
    list: List<ComposeWifiP2pDevice>,
    onConnectItemClickListener: (ComposeWifiP2pDevice) -> Unit,
    onDisconnectItemClickListener: () -> Unit,
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
    val list = listOf(
        ComposeWifiP2pDevice(
            "deviceAddress1",
            "deviceName1",
            "primaryDeviceType",
            "secondaryDeviceType",
            ComposeWifiP2pDevice.STATUS.AVAILABLE
        ),
        ComposeWifiP2pDevice(
            "deviceAddress2",
            null,
            "primaryDeviceType2",
            "secondaryDeviceType2",
            ComposeWifiP2pDevice.STATUS.UNKNOWN
        ),
        ComposeWifiP2pDevice(
            "deviceAddress3",
            null,
            "primaryDeviceType3",
            "secondaryDeviceType3",
            ComposeWifiP2pDevice.STATUS.FAILED
        )
    )
    DeviceList(list, {

    }, {

    })
}