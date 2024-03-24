package com.example.fcppushnotificationshttpv1.wifip2p.chat.presentation.device

import android.net.wifi.p2p.WifiP2pDevice
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fcppushnotificationshttpv1.R

@Composable
fun DeviceItem(
    item: WifiP2pDevice,
    onConnectItemClickListener: (WifiP2pDevice) -> Unit,
    onDisconnectItemClickListener: (WifiP2pDevice) -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxWidth()
            .padding(16.dp)
    ) {
        HorizontalDivider()
        Column(
            modifier = Modifier.clickable {
                onConnectItemClickListener(item)
            }
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(onClick = {
                    onConnectItemClickListener(item)
                }) {
                    Text(text = "Connect")
                }
                Button(onClick = {
                    onDisconnectItemClickListener(item)
                }) {
                    Text(text = "Disconnect")
                }
            }
            Text(text = item.deviceAddress ?: "")
            Text(text = item.deviceName ?: "")
            Text(text = item.primaryDeviceType ?: "")
            Text(text = item.secondaryDeviceType ?: "")
            DeviceDeviceStatus(item)
        }
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
    }, {

    }, {

    })
}

@Composable
private fun DeviceDeviceStatus(device: WifiP2pDevice) {
    Text(
        text = when (device.status) {
            WifiP2pDevice.AVAILABLE -> stringResource(id = R.string.device_status_available)
            WifiP2pDevice.INVITED -> stringResource(id = R.string.device_status_invited)
            WifiP2pDevice.CONNECTED -> stringResource(id = R.string.device_status_connected)
            WifiP2pDevice.FAILED -> stringResource(id = R.string.device_status_failed)
            WifiP2pDevice.UNAVAILABLE -> stringResource(id = R.string.device_status_unavailable)
            else -> stringResource(id = R.string.device_status_unknown)
        }
    )
}