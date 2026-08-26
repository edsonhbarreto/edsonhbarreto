package com.edsonhbarreto.travelmap.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * A place visited (or to visit) during the trip.
 * [colorTag] indexes into [com.edsonhbarreto.travelmap.ui.theme.PlaceColors] so each
 * place gets a consistent color on the map marker and its card.
 */
@Entity(tableName = "places")
data class Place(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val country: String = "",
    /** Free text as written in the itinerary, e.g. "16–20/10". */
    val dates: String = "",
    val description: String = "",
    val latitude: Double,
    val longitude: Double,
    val imageUris: List<String> = emptyList(),
    val colorTag: Int = 0,
    /** Marks the trip's centrepiece — the Amsterdam Marathon stop. */
    val highlight: Boolean = false
)
