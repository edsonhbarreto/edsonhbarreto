package com.edsonhbarreto.travelmap.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ChecklistDao {
    /** Pending first, urgent booking gaps at the very top. */
    @Query(
        "SELECT * FROM checklist_items " +
            "ORDER BY isDone ASC, isUrgent DESC, dateTimeMillis IS NULL, dateTimeMillis ASC, id ASC"
    )
    fun observeAll(): Flow<List<ChecklistItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: ChecklistItem): Long

    @Update
    suspend fun update(item: ChecklistItem)

    @Delete
    suspend fun delete(item: ChecklistItem)
}
