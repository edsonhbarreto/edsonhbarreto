package com.edsonhbarreto.travelmap.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

enum class ChecklistType { REMINDER, RESERVATION }

/**
 * A reminder or reservation the traveler wants to track and check off.
 * [placeId] is optional so an item can be tied to a specific place on the map,
 * or left general (e.g. "check in online", "pack sunscreen").
 */
@Entity(
    tableName = "checklist_items",
    foreignKeys = [
        ForeignKey(
            entity = Place::class,
            parentColumns = ["id"],
            childColumns = ["placeId"],
            onDelete = ForeignKey.SET_NULL
        )
    ]
)
data class ChecklistItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val notes: String = "",
    val type: ChecklistType,
    val placeId: Long? = null,
    val dateTimeMillis: Long? = null,
    val isDone: Boolean = false,
    /** Booking gaps flagged in the itinerary as still to buy or confirm. */
    val isUrgent: Boolean = false
)
