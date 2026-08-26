package com.edsonhbarreto.travelmap.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.edsonhbarreto.travelmap.data.ChecklistType
import com.edsonhbarreto.travelmap.data.Place

@Composable
fun AddChecklistItemDialog(
    places: List<Place>,
    onDismiss: () -> Unit,
    onConfirm: (title: String, notes: String, type: ChecklistType, placeId: Long?) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var type by remember { mutableStateOf(ChecklistType.REMINDER) }
    var selectedPlace by remember { mutableStateOf<Place?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Novo item") },
        text = {
            Column(
                modifier = Modifier
                    .heightIn(max = 420.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Título") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Notas (opcional)") },
                    modifier = Modifier.padding(top = 8.dp)
                )

                Row(modifier = Modifier.padding(top = 12.dp)) {
                    listOf(ChecklistType.REMINDER to "Lembrete", ChecklistType.RESERVATION to "Reserva").forEach { (value, label) ->
                        Row(
                            modifier = Modifier.selectable(
                                selected = type == value,
                                onClick = { type = value }
                            ),
                            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                        ) {
                            RadioButton(selected = type == value, onClick = { type = value })
                            Text(label)
                        }
                    }
                }

                if (places.isNotEmpty()) {
                    Text("Vincular a um lugar (opcional):", modifier = Modifier.padding(top = 8.dp))
                    Row(modifier = Modifier.padding(top = 4.dp)) {
                        Row(
                            modifier = Modifier.selectable(
                                selected = selectedPlace == null,
                                onClick = { selectedPlace = null }
                            ),
                            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                        ) {
                            RadioButton(selected = selectedPlace == null, onClick = { selectedPlace = null })
                            Text("Nenhum")
                        }
                    }
                    places.forEach { place ->
                        Row(
                            modifier = Modifier.selectable(
                                selected = selectedPlace?.id == place.id,
                                onClick = { selectedPlace = place }
                            ),
                            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                        ) {
                            RadioButton(selected = selectedPlace?.id == place.id, onClick = { selectedPlace = place })
                            Text(place.name)
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = { if (title.isNotBlank()) onConfirm(title, notes, type, selectedPlace?.id) },
                enabled = title.isNotBlank()
            ) { Text("Adicionar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}
