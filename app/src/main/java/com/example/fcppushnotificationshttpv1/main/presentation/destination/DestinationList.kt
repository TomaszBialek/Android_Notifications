package com.example.fcppushnotificationshttpv1.main.presentation.destination

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.fcppushnotificationshttpv1.main.domain.Destination

@Composable
fun DestinationLazyColumn(
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