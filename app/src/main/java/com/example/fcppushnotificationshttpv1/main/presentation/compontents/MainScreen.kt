package com.example.fcppushnotificationshttpv1.main.presentation.compontents

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.fcppushnotificationshttpv1.main.presentation.MainViewModel

@Composable
fun MainScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel = viewModel()
) {
    DestinationLazyColumn(mainViewModel.screens, navController, modifier)
}

@Preview
@Composable
fun MainScreenPreview() {
    MainScreen(navController = NavController(LocalContext.current))
}