package com.example.fcppushnotificationshttpv1.wifip2p.chat.presentation.components

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
import com.example.fcppushnotificationshttpv1.wifip2p.chat.domain.ComposeWifiP2pDevice

@Composable
fun DeviceItem(
    item: ComposeWifiP2pDevice,
    onConnectItemClickListener: (ComposeWifiP2pDevice) -> Unit,
    onDisconnectItemClickListener: () -> Unit,
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
                    onDisconnectItemClickListener()
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
    DeviceItem(item = ComposeWifiP2pDevice(
        "address",
        "name",
        "primaryType",
        "secondaryType",
        ComposeWifiP2pDevice.STATUS.AVAILABLE
    ), {

    }, {

    })
}

@Composable
private fun DeviceDeviceStatus(device: ComposeWifiP2pDevice) {
    Text(
        text = when (device.deviceStatus) {
            ComposeWifiP2pDevice.STATUS.AVAILABLE -> stringResource(id = R.string.device_status_available)
            ComposeWifiP2pDevice.STATUS.INVITED -> stringResource(id = R.string.device_status_invited)
            ComposeWifiP2pDevice.STATUS.CONNECTED -> stringResource(id = R.string.device_status_connected)
            ComposeWifiP2pDevice.STATUS.FAILED -> stringResource(id = R.string.device_status_failed)
            ComposeWifiP2pDevice.STATUS.UNAVAILABLE -> stringResource(id = R.string.device_status_unavailable)
            ComposeWifiP2pDevice.STATUS.UNKNOWN -> stringResource(id = R.string.device_status_unknown)
        }
    )
}