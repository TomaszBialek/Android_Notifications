package com.example.fcppushnotificationshttpv1

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
fun MainScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel = viewModel()
) {
    DestinationLazyColumn(mainViewModel.destinations, navController, modifier)
}

@Composable
private fun DestinationLazyColumn(
    destinations: List<Destination>,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        items(destinations) { destination ->
            DestinationItem(destination, navController, modifier)
        }
    }
}

@Composable
private fun DestinationItem(
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
            navController.navigate("NotificationChatScreen/${destination.destination}")
            Toast.makeText(
                context,
                destination.destination.toUpperCase(Locale.current),
                Toast.LENGTH_SHORT
            ).show()
        },
        enabled = destination.isEnabled
    ) {
        Text(
            text = destination.destination.toUpperCase(Locale.current),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
    }
}

@Preview
@Composable
fun MainScreenPreview() {
    MainScreen(navController = NavController(LocalContext.current))
}