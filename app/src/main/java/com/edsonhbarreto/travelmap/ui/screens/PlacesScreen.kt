package com.edsonhbarreto.travelmap.ui.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.edsonhbarreto.travelmap.data.Place
import com.edsonhbarreto.travelmap.ui.components.AddPlaceDialog
import com.edsonhbarreto.travelmap.ui.components.PlaceCard

@Composable
fun PlacesScreen(
    places: List<Place>,
    onAddPlace: (name: String, description: String, lat: Double, lng: Double) -> Unit,
    onPlaceClick: (Place) -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Filled.Add, contentDescription = "Adicionar lugar")
            }
        }
    ) { padding ->
        if (places.isEmpty()) {
            Text(
                text = "Nenhum lugar ainda. Toque em + ou segure o dedo no mapa para adicionar o primeiro lugar da viagem.",
                modifier = Modifier.padding(padding).padding(24.dp)
            )
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(12.dp),
                verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                items(places, key = { it.id }) { place ->
                    PlaceCard(place = place, onClick = { onPlaceClick(place) })
                }
            }
        }
    }

    if (showAddDialog) {
        AddPlaceDialog(
            initialLat = 0.0,
            initialLng = 0.0,
            onDismiss = { showAddDialog = false },
            onConfirm = { name, description, lat, lng ->
                onAddPlace(name, description, lat, lng)
                showAddDialog = false
            }
        )
    }
}
