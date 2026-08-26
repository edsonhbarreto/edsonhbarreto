package com.edsonhbarreto.travelmap

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.edsonhbarreto.travelmap.navigation.AppNavigation
import com.edsonhbarreto.travelmap.ui.theme.TravelMapTheme

class MainActivity : ComponentActivity() {

    private val viewModel: TripViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TravelMapTheme {
                AppNavigation(viewModel = viewModel)
            }
        }
    }
}
