package com.edsonhbarreto.travelmap.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.edsonhbarreto.travelmap.data.ChecklistItem
import com.edsonhbarreto.travelmap.data.ChecklistType
import com.edsonhbarreto.travelmap.ui.theme.SunYellow
import com.edsonhbarreto.travelmap.ui.theme.Turquoise
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ChecklistItemRow(
    item: ChecklistItem,
    placeName: String?,
    onCheckedChange: (Boolean) -> Unit,
    onDelete: () -> Unit
) {
    val accent = if (item.type == ChecklistType.RESERVATION) Turquoise else SunYellow

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = accent.copy(alpha = 0.15f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(checked = item.isDone, onCheckedChange = onCheckedChange)

            Icon(
                imageVector = if (item.type == ChecklistType.RESERVATION) Icons.Filled.Event else Icons.Filled.Notifications,
                contentDescription = null,
                tint = accent,
                modifier = Modifier.padding(end = 8.dp)
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.bodyLarge,
                    textDecoration = if (item.isDone) TextDecoration.LineThrough else null
                )
                if (item.notes.isNotBlank()) {
                    Text(
                        text = item.notes,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                if (item.isUrgent && !item.isDone) {
                    Text(
                        text = "a resolver já",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }
                val subtitleParts = buildList {
                    placeName?.let { add(it) }
                    item.dateTimeMillis?.let {
                        add(SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date(it)))
                    }
                }
                if (subtitleParts.isNotEmpty()) {
                    Text(
                        text = subtitleParts.joinToString(" · "),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            IconButton(onClick = onDelete) {
                Icon(Icons.Filled.Delete, contentDescription = "Remover")
            }
        }
    }
}
