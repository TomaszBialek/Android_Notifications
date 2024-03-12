package com.example.fcppushnotificationshttpv1.wifip2p.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun WifiP2pConnectionScreen() {
    Surface {
        Column {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Connection Status",
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.padding(16.dp))
            Row {
                Button(onClick = { /*TODO*/ }) {
                    Text(text = "On/Off")
                }
                Spacer(modifier = Modifier.padding(16.dp))
                Button(onClick = { /*TODO*/ }) {
                    Text(text = "Discover")
                }

            }
            Spacer(modifier = Modifier.padding(16.dp))
            DeviceList()
        }
    }
}

@Preview
@Composable
fun WifiP2pConnectionScreenPreview() {
    WifiP2pConnectionScreen()
}