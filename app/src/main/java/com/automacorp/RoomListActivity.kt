package com.automacorp

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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

        val viewModel: RoomViewModel by viewModels() // Attach ViewModel to the Activity

        // Navigation functions
        val navigateBack: () -> Unit = { navigateToMainActivity() }
        val openRoom: (id: Long) -> Unit = { roomId -> openRoomDetail(roomId) }

        setContent {
            val roomsState by viewModel.roomsState.collectAsState() // Observe roomsState
            val context = LocalContext.current

            if (roomsState.error != null) { // Error Handling
                RoomList(emptyList(), navigateBack, openRoom)
                Toast.makeText(context, "Error loading rooms: ${roomsState.error}", Toast.LENGTH_LONG).show()
            } else {
                RoomList(roomsState.rooms, navigateBack, openRoom) // Display Room List
            }
        }

        // Trigger API call in ViewModel
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
}

@Composable
fun RoomList(
    rooms: List<RoomDto>,
    navigateBack: () -> Unit,
    openRoom: (id: Long) -> Unit
) {
    AutomacorpTheme {
        Scaffold(
            topBar = { AutomacorpTopAppBar("Rooms", navigateBack) }
        ) { innerPadding ->
            if (rooms.isEmpty()) {
                Text(
                    text = "No room found",
                    modifier = Modifier.padding(innerPadding),
                    style = MaterialTheme.typography.bodyLarge
                )
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(4.dp),
                    verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(innerPadding)
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
