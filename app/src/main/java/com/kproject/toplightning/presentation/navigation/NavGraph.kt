package com.kproject.toplightning.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kproject.toplightning.presentation.screens.home.HomeScreen
import com.kproject.toplightning.presentation.screens.home.HomeViewModel

@Composable
fun NavGraph(
    homeViewModel: HomeViewModel,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = HomeScreen
    ) {
        composable<HomeScreen> {
            HomeScreen(homeViewModel = homeViewModel)
        }
    }
}