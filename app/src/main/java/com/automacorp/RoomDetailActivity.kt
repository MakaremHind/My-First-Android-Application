package com.automacorp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.automacorp.model.WindowDto
import com.automacorp.model.WindowStatus
import com.automacorp.ui.theme.AutomacorpTheme

class RoomDetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val roomId = intent.getStringExtra(MainActivity.ROOM_PARAM)?.toLongOrNull()
        val viewModel: RoomViewModel by viewModels()

        roomId?.let { viewModel.findRoom(it) }

        val navigateBack: () -> Unit = {
            startActivity(Intent(this, RoomListActivity::class.java))
        }

        setContent {
            AutomacorpTheme {
                val context = LocalContext.current

                Scaffold(
                    topBar = {
                        AutomacorpTopAppBar("Room Detail", navigateBack) {
                            IconButton(onClick = {
                                roomId?.let {
                                    viewModel.deleteRoom(it)
                                    Toast.makeText(context, "Room deleted successfully!", Toast.LENGTH_SHORT).show()
                                    navigateBack()
                                }
                            }) {
                                Icon(Icons.Filled.Delete, contentDescription = "Delete Room")
                            }
                        }
                    },
                    floatingActionButton = {
                        viewModel.room?.let { room ->
                            FloatingActionButton(
                                onClick = {
                                    // Update the room details
                                    viewModel.updateRoom(room.id, room)
                                    Toast.makeText(context, "Room updated successfully!", Toast.LENGTH_SHORT).show()
                                    navigateBack()
                                },
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            ) {
                                Text("Save")
                            }
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    if (viewModel.room != null) {
                        RoomDetailWithWindows(viewModel, Modifier.padding(innerPadding))
                    } else {
                        NoRoomFound(Modifier.padding(innerPadding))
                    }
                }
            }
        }
    }
}

@Composable
fun RoomDetailWithWindows(viewModel: RoomViewModel, modifier: Modifier = Modifier) {
    val room = viewModel.room
    if (room != null) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Editable Room Details
            EditableRoomDetailContent(viewModel)

            // Window List Header
            Text("Windows:", style = MaterialTheme.typography.titleMedium)

            // List of Windows
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(room.windows) { window ->
                    WindowItemWithDropdown(
                        window = window,
                        onStatusChange = { newStatus ->
                            // Update the window's status locally and in the ViewModel
                            val updatedWindows = room.windows.map {
                                if (it.id == window.id) it.copy(windowStatus = newStatus, roomName = room.name) else it
                            }
                            viewModel.room = room.copy(windows = updatedWindows)
                            }
                    )
                }
            }
        }
    }
}


@Composable
fun WindowItemWithDropdown(window: WindowDto, onStatusChange: (WindowStatus) -> Unit) {
    var currentStatus by remember { mutableStateOf(window.windowStatus) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("Window Name: ${window.name}", style = MaterialTheme.typography.bodyLarge)
            Text("Current Status: $currentStatus", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun EditableRoomDetailContent(viewModel: RoomViewModel, modifier: Modifier = Modifier) {
    val room = viewModel.room
    if (room != null) {
        val updatedName = remember { mutableStateOf(room.name) }
        val updatedCurrentTemperature = remember { mutableStateOf(room.currentTemperature?.toString() ?: "") }
        val updatedTargetTemperature = remember { mutableStateOf(room.targetTemperature?.toString() ?: "") }

        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Room Name
            OutlinedTextField(
                value = updatedName.value,
                onValueChange = {
                    updatedName.value = it
                    viewModel.room = room.copy(name = it)
                },
                label = { Text("Room Name") },
                modifier = Modifier.fillMaxWidth()
            )

            // Current Temperature
            OutlinedTextField(
                value = updatedCurrentTemperature.value,
                onValueChange = {
                    updatedCurrentTemperature.value = it
                    viewModel.room = room.copy(currentTemperature = it.toDoubleOrNull())
                },
                label = { Text("Current Temperature (°C)") },
                modifier = Modifier.fillMaxWidth()
            )

            // Target Temperature
            OutlinedTextField(
                value = updatedTargetTemperature.value,
                onValueChange = {
                    updatedTargetTemperature.value = it
                    viewModel.room = room.copy(targetTemperature = it.toDoubleOrNull())
                },
                label = { Text("Target Temperature (°C)") },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun NoRoomFound(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        Text(
            text = "No room data available.",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
