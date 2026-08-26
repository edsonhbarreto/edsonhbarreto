package com.edsonhbarreto.travelmap.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

/**
 * When [initialLat]/[initialLng] are both 0.0 (dialog opened from the "+" button, not a map
 * long-press), the coordinate fields are shown editable so the user can type them in — e.g.
 * copied from Google Maps — instead of always landing the pin in the Gulf of Guinea.
 */
@Composable
fun AddPlaceDialog(
    initialLat: Double,
    initialLng: Double,
    onDismiss: () -> Unit,
    onConfirm: (name: String, description: String, lat: Double, lng: Double) -> Unit
) {
    val locationKnown = initialLat != 0.0 || initialLng != 0.0
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var latText by remember { mutableStateOf(if (locationKnown) initialLat.toString() else "") }
    var lngText by remember { mutableStateOf(if (locationKnown) initialLng.toString() else "") }

    val lat = latText.toDoubleOrNull()
    val lng = lngText.toDoubleOrNull()
    val canConfirm = name.isNotBlank() && lat != null && lng != null

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Novo lugar") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nome do lugar") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Descrição (opcional)") },
                    modifier = Modifier.padding(top = 8.dp)
                )
                if (locationKnown) {
                    Text(
                        text = "Localização escolhida no mapa ✓",
                        modifier = Modifier.padding(top = 12.dp)
                    )
                } else {
                    Row(modifier = Modifier.padding(top = 12.dp)) {
                        OutlinedTextField(
                            value = latText,
                            onValueChange = { latText = it },
                            label = { Text("Latitude") },
                            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.width(140.dp)
                        )
                        OutlinedTextField(
                            value = lngText,
                            onValueChange = { lngText = it },
                            label = { Text("Longitude") },
                            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.width(140.dp).padding(start = 8.dp)
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = { if (canConfirm) onConfirm(name, description, lat!!, lng!!) },
                enabled = canConfirm
            ) { Text("Adicionar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}
