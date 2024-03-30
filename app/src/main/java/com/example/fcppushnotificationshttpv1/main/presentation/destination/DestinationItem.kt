package com.example.fcppushnotificationshttpv1.main.presentation.destination

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fcppushnotificationshttpv1.main.domain.Destination

@Composable
fun DestinationItem(
    destination: Destination,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    Button(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        onClick = {
            navController.navigate(destination.route)
            Toast.makeText(
                context,
                destination.description.toUpperCase(Locale.current),
                Toast.LENGTH_SHORT
            ).show()
        },
        enabled = destination.isEnabled
    ) {
        Text(
            text = destination.description.toUpperCase(Locale.current),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
    }
}