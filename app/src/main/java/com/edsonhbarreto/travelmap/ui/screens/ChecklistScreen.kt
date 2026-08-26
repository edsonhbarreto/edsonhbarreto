package com.edsonhbarreto.travelmap.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
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
import com.edsonhbarreto.travelmap.data.ChecklistItem
import com.edsonhbarreto.travelmap.data.ChecklistType
import com.edsonhbarreto.travelmap.data.Place
import com.edsonhbarreto.travelmap.ui.components.AddChecklistItemDialog
import com.edsonhbarreto.travelmap.ui.components.ChecklistItemRow

private enum class Filter { ALL, REMINDERS, RESERVATIONS }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChecklistScreen(
    items: List<ChecklistItem>,
    places: List<Place>,
    onAdd: (title: String, notes: String, type: ChecklistType, placeId: Long?) -> Unit,
    onCheckedChange: (ChecklistItem, Boolean) -> Unit,
    onDelete: (ChecklistItem) -> Unit
) {
    var filter by remember { mutableStateOf(Filter.ALL) }
    var showAddDialog by remember { mutableStateOf(false) }

    val filteredItems = when (filter) {
        Filter.ALL -> items
        Filter.REMINDERS -> items.filter { it.type == ChecklistType.REMINDER }
        Filter.RESERVATIONS -> items.filter { it.type == ChecklistType.RESERVATION }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Filled.Add, contentDescription = "Adicionar item")
            }
        }
    ) { padding ->
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(selected = filter == Filter.ALL, onClick = { filter = Filter.ALL }, label = { Text("Todos") })
                    FilterChip(selected = filter == Filter.REMINDERS, onClick = { filter = Filter.REMINDERS }, label = { Text("Lembretes") })
                    FilterChip(selected = filter == Filter.RESERVATIONS, onClick = { filter = Filter.RESERVATIONS }, label = { Text("Reservas") })
                }
            }

            if (filteredItems.isEmpty()) {
                item {
                    Text("Nada por aqui ainda. Toque em + para adicionar um lembrete ou reserva.")
                }
            }

            items(filteredItems, key = { it.id }) { checklistItem ->
                val placeName = places.firstOrNull { it.id == checklistItem.placeId }?.name
                ChecklistItemRow(
                    item = checklistItem,
                    placeName = placeName,
                    onCheckedChange = { checked -> onCheckedChange(checklistItem, checked) },
                    onDelete = { onDelete(checklistItem) }
                )
            }
        }
    }

    if (showAddDialog) {
        AddChecklistItemDialog(
            places = places,
            onDismiss = { showAddDialog = false },
            onConfirm = { title, notes, type, placeId ->
                onAdd(title, notes, type, placeId)
                showAddDialog = false
            }
        )
    }
}
