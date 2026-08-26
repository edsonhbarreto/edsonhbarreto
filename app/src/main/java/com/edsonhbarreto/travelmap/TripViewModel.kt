package com.edsonhbarreto.travelmap

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.edsonhbarreto.travelmap.data.AppDatabase
import com.edsonhbarreto.travelmap.data.ChecklistItem
import com.edsonhbarreto.travelmap.data.ChecklistType
import com.edsonhbarreto.travelmap.data.Place
import com.edsonhbarreto.travelmap.data.TripRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TripViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = TripRepository(AppDatabase.getInstance(application))

    val places: StateFlow<List<Place>> = repository.places.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList()
    )

    val checklistItems: StateFlow<List<ChecklistItem>> = repository.checklistItems.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList()
    )

    fun addPlace(name: String, description: String, lat: Double, lng: Double, imageUris: List<String> = emptyList()) {
        viewModelScope.launch {
            val colorTag = places.value.size
            repository.upsertPlace(
                Place(name = name, description = description, latitude = lat, longitude = lng, imageUris = imageUris, colorTag = colorTag)
            )
        }
    }

    fun updatePlaceImages(place: Place, imageUris: List<String>) {
        viewModelScope.launch { repository.upsertPlace(place.copy(imageUris = imageUris)) }
    }

    fun deletePlace(place: Place) {
        viewModelScope.launch { repository.deletePlace(place) }
    }

    fun addChecklistItem(title: String, notes: String, type: ChecklistType, placeId: Long?, dateTimeMillis: Long?) {
        viewModelScope.launch {
            repository.upsertChecklistItem(
                ChecklistItem(title = title, notes = notes, type = type, placeId = placeId, dateTimeMillis = dateTimeMillis)
            )
        }
    }

    fun toggleChecklistItem(item: ChecklistItem, done: Boolean) {
        viewModelScope.launch { repository.setChecklistItemDone(item, done) }
    }

    fun deleteChecklistItem(item: ChecklistItem) {
        viewModelScope.launch { repository.deleteChecklistItem(item) }
    }
}
