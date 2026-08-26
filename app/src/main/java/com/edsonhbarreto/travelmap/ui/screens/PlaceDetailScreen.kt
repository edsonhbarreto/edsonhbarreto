package com.edsonhbarreto.travelmap.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.edsonhbarreto.travelmap.data.ChecklistItem
import com.edsonhbarreto.travelmap.data.ChecklistType
import com.edsonhbarreto.travelmap.data.Place
import com.edsonhbarreto.travelmap.ui.components.AddChecklistItemDialog
import com.edsonhbarreto.travelmap.ui.components.ChecklistItemRow
import com.edsonhbarreto.travelmap.ui.theme.PlaceColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceDetailScreen(
    place: Place,
    checklistItems: List<ChecklistItem>,
    onBack: () -> Unit,
    onImagesChanged: (List<String>) -> Unit,
    onAddChecklistItem: (title: String, notes: String, type: ChecklistType, placeId: Long?) -> Unit,
    onCheckedChange: (ChecklistItem, Boolean) -> Unit,
    onDeleteChecklistItem: (ChecklistItem) -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(limit = 10)
    ) { uris ->
        if (uris.isNotEmpty()) {
            onImagesChanged(place.imageUris + uris.map { it.toString() })
        }
    }

    val accent = PlaceColors[place.colorTag % PlaceColors.size]

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(place.name) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            item {
                if (place.imageUris.isNotEmpty()) {
                    val pagerState = rememberPagerState(pageCount = { place.imageUris.size })
                    HorizontalPager(state = pagerState, modifier = Modifier.fillMaxWidth().aspectRatio(1.4f)) { page ->
                        AsyncImage(
                            model = place.imageUris[page],
                            contentDescription = place.name,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1.4f),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Filled.Photo, contentDescription = null, tint = accent)
                    }
                }

                Column(modifier = Modifier.padding(16.dp)) {
                    if (place.description.isNotBlank()) {
                        Text(place.description, style = MaterialTheme.typography.bodyLarge)
                    }
                    Button(
                        onClick = {
                            photoPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                        },
                        modifier = Modifier.padding(top = 12.dp)
                    ) {
                        Icon(Icons.Filled.AddPhotoAlternate, contentDescription = null)
                        Text(" Adicionar fotos", modifier = Modifier.padding(start = 8.dp))
                    }

                    Text(
                        text = "Lembretes e reservas deste lugar",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(top = 20.dp, bottom = 8.dp)
                    )
                }
            }

            if (checklistItems.isEmpty()) {
                item {
                    Text(
                        "Nenhum item vinculado a este lugar ainda.",
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }

            items(checklistItems, key = { it.id }) { checklistItem ->
                Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
                    ChecklistItemRow(
                        item = checklistItem,
                        placeName = null,
                        onCheckedChange = { checked -> onCheckedChange(checklistItem, checked) },
                        onDelete = { onDeleteChecklistItem(checklistItem) }
                    )
                }
            }

            item {
                Button(
                    onClick = { showAddDialog = true },
                    modifier = Modifier.padding(16.dp)
                ) { Text("Adicionar lembrete/reserva") }
            }
        }
    }

    if (showAddDialog) {
        AddChecklistItemDialog(
            places = listOf(place),
            onDismiss = { showAddDialog = false },
            onConfirm = { title, notes, type, placeId ->
                onAddChecklistItem(title, notes, type, placeId ?: place.id)
                showAddDialog = false
            }
        )
    }
}
