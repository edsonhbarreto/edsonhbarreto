package com.edsonhbarreto.travelmap.data

import kotlinx.coroutines.flow.Flow

class TripRepository(private val db: AppDatabase) {
    val places: Flow<List<Place>> = db.placeDao().observeAll()
    val checklistItems: Flow<List<ChecklistItem>> = db.checklistDao().observeAll()

    suspend fun getPlace(id: Long): Place? = db.placeDao().getById(id)

    suspend fun upsertPlace(place: Place): Long = db.placeDao().insert(place)
    suspend fun deletePlace(place: Place) = db.placeDao().delete(place)

    suspend fun upsertChecklistItem(item: ChecklistItem): Long = db.checklistDao().insert(item)
    suspend fun deleteChecklistItem(item: ChecklistItem) = db.checklistDao().delete(item)
    suspend fun setChecklistItemDone(item: ChecklistItem, done: Boolean) =
        db.checklistDao().update(item.copy(isDone = done))
}
