package com.edsonhbarreto.travelmap.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.edsonhbarreto.travelmap.data.Place
import com.edsonhbarreto.travelmap.ui.components.AddPlaceDialog
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

private val DefaultCenter = LatLng(-14.235004, -51.92528) // Brasil, zoom out default

/** Maps a place color from the palette to the closest Google-Maps marker hue. */
private fun hueForColorTag(tag: Int): Float {
    val hues = listOf(20f, 174f, 270f, 48f, 340f, 152f, 225f, 30f)
    return hues[tag % hues.size]
}

@Composable
fun MapScreen(
    places: List<Place>,
    onAddPlace: (name: String, description: String, lat: Double, lng: Double) -> Unit,
    onMarkerClick: (Place) -> Unit
) {
    var pendingLatLng by remember { mutableStateOf<LatLng?>(null) }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            places.firstOrNull()?.let { LatLng(it.latitude, it.longitude) } ?: DefaultCenter,
            if (places.isEmpty()) 3f else 11f
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(),
            onMapLongClick = { latLng -> pendingLatLng = latLng }
        ) {
            places.forEach { place ->
                Marker(
                    state = MarkerState(position = LatLng(place.latitude, place.longitude)),
                    title = place.name,
                    snippet = place.description.ifBlank { "Toque para ver fotos e detalhes" },
                    icon = BitmapDescriptorFactory.defaultMarker(hueForColorTag(place.colorTag)),
                    onClick = {
                        onMarkerClick(place)
                        true
                    }
                )
            }
        }
    }

    pendingLatLng?.let { latLng ->
        AddPlaceDialog(
            initialLat = latLng.latitude,
            initialLng = latLng.longitude,
            onDismiss = { pendingLatLng = null },
            onConfirm = { name, description, lat, lng ->
                onAddPlace(name, description, lat, lng)
                pendingLatLng = null
            }
        )
    }
}
