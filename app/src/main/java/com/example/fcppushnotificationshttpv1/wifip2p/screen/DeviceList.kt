package com.example.fcppushnotificationshttpv1.wifip2p.screen

import android.net.wifi.p2p.WifiP2pDevice
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun DeviceList(
    list: Array<WifiP2pDevice>,
    onItemClickListener: (WifiP2pDevice) -> Unit
) {
    LazyColumn {
        items(list) { wifiDevice ->
            DeviceItem(wifiDevice, onItemClickListener)
        }
    }
}

@Composable
fun DeviceItem(
    item: WifiP2pDevice,
    onItemClickListener: (WifiP2pDevice) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth()
    ) {
        HorizontalDivider()
        Column(
            modifier = Modifier.clickable {
                onItemClickListener(item)
            }
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(onClick = { }) {
                    Text(text = "Connect")
                }
                Button(onClick = { }) {
                    Text(text = "Disconnect")
                }
            }
            Text(text = item.deviceAddress ?: "")
            Text(text = item.deviceName ?: "")
            Text(text = item.primaryDeviceType ?: "")
            Text(text = item.secondaryDeviceType ?: "")
            Text(text = item.getDeviceStatus())
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
    DeviceList(array) {

    }
}

@Preview
@Composable
fun DeviceItemPreview() {
    DeviceItem(item = WifiP2pDevice().apply {
        this.deviceAddress = "address"
        this.deviceName = "name"
        this.primaryDeviceType = "primaryType"
        this.secondaryDeviceType = "secondaryType"
        this.status = WifiP2pDevice.AVAILABLE
    }) {

    }
}

fun WifiP2pDevice.getDeviceStatus(): String {
    return when (status) {
        WifiP2pDevice.AVAILABLE -> "Available"
        WifiP2pDevice.INVITED -> "Invited"
        WifiP2pDevice.CONNECTED -> "Connected"
        WifiP2pDevice.FAILED -> "Failed"
        WifiP2pDevice.UNAVAILABLE -> "Unavailable"
        else -> "Unknown"
    }
}