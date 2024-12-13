package com.automacorp

import RoomCommandDto
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.automacorp.model.RoomDto
import com.automacorp.ui.theme.AutomacorpTheme

class RoomListActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel: RoomViewModel by viewModels()

        val navigateBack: () -> Unit = { navigateToMainActivity() }
        val openRoom: (id: Long) -> Unit = { roomId -> openRoomDetail(roomId) }

        setContent {
            val roomsState by viewModel.roomsState.collectAsState()
            val context = LocalContext.current

            Scaffold(
                topBar = {
                    AutomacorpTopAppBar("Rooms", navigateBack) {
                        // Add "Create Room" button in the App Bar
                        IconButton(onClick = { showCreateRoomDialog(viewModel, context) }) {
                            Icon(Icons.Filled.Add, contentDescription = "Create Room")
                        }
                    }
                }
            ) { innerPadding -> // Use innerPadding here
                if (roomsState.error != null) {
                    Toast.makeText(context, "Error loading rooms: ${roomsState.error}", Toast.LENGTH_LONG).show()
                    RoomList(emptyList(), navigateBack, openRoom, Modifier.padding(innerPadding)) // Pass innerPadding
                } else {
                    RoomList(roomsState.rooms, navigateBack, openRoom, Modifier.padding(innerPadding)) // Pass innerPadding
                }
            }
        }

        viewModel.findAll()
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    private fun openRoomDetail(roomId: Long) {
        val intent = Intent(this, RoomDetailActivity::class.java).apply {
            putExtra(MainActivity.ROOM_PARAM, roomId.toString())
        }
        startActivity(intent)
    }

    private fun showCreateRoomDialog(viewModel: RoomViewModel, context: android.content.Context) {
        val newRoom = RoomCommandDto(name = "New Room", targetTemperature = 22.0, currentTemperature = 20.0)
        viewModel.createRoom(newRoom)
        Toast.makeText(context, "Room created successfully!", Toast.LENGTH_SHORT).show()
        viewModel.findAll() // Refresh room list
    }
}


@Composable
fun RoomList(
    rooms: List<RoomDto>,
    navigateBack: () -> Unit,
    openRoom: (id: Long) -> Unit,
    modifier: Modifier = Modifier // Add modifier parameter with a default value
) {
    AutomacorpTheme {
        Scaffold(
            topBar = { AutomacorpTopAppBar("Rooms", navigateBack) }
        ) { innerPadding ->
            if (rooms.isEmpty()) {
                Text(
                    text = "No room found",
                    modifier = modifier.padding(innerPadding), // Use modifier with innerPadding
                    style = MaterialTheme.typography.bodyLarge
                )
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(4.dp),
                    verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp),
                    modifier = modifier.padding(innerPadding) // Use modifier with innerPadding
                ) {
                    items(rooms, key = { it.id }) { room ->
                        RoomItem(
                            room = room,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { openRoom(room.id) },
                            onClick = { openRoom(room.id) }
                        )
                    }
                }
            }
        }
    }
}



@Composable
fun RoomItem(room: RoomDto, modifier: Modifier = Modifier, onClick: () -> Unit) {
    androidx.compose.material3.Card(
        modifier = modifier.clickable { onClick() }
    ) {
        androidx.compose.foundation.layout.Row(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
        ) {
            androidx.compose.foundation.layout.Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = room.name,
                    style = androidx.compose.material3.MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "Target temperature: ${room.targetTemperature ?: "?"}°",
                    style = androidx.compose.material3.MaterialTheme.typography.bodySmall
                )
            }
            Text(
                text = "${room.currentTemperature ?: "?"}°",
                style = androidx.compose.material3.MaterialTheme.typography.headlineMedium
            )
        }
    }
}
