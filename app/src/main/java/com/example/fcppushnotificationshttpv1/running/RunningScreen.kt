package com.example.fcppushnotificationshttpv1.running

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

@Composable
fun RunningScreen(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = {
            Intent(context, RunningService::class.java).also {
                it.action = RunningService.Actions.START.toString()
                context.startService(it)
            }
        }) {
            Text(text = "Start service")
        }
        Button(onClick = {
            Intent(context, RunningService::class.java).also {
                it.action = RunningService.Actions.STOP.toString()
                context.startService(it)
            }
        }) {
            Text(text = "Stop service")
        }
    }
}