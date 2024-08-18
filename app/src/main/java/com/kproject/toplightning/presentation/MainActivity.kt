package com.kproject.toplightning.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kproject.toplightning.presentation.screens.home.HomeScreen
import com.kproject.toplightning.presentation.screens.home.HomeViewModel
import com.kproject.toplightning.presentation.theme.TopLightningTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val uiState by homeViewModel.uiState.collectAsStateWithLifecycle()
            TopLightningTheme(darkTheme = uiState.isDarkMode) {
                HomeScreen(homeViewModel = homeViewModel)
            }
        }
    }
}