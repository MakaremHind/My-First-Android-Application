package com.automacorp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.automacorp.model.RoomDto
import com.automacorp.service.ApiServices
import com.automacorp.ui.theme.AutomacorpTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RoomListActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Navigation functions
        val navigateBack: () -> Unit = { navigateToMainActivity() }
        val openRoom: (id: Long) -> Unit = { roomId -> openRoomDetail(roomId) }

        // Fetch rooms from the API
        ApiServices.roomsApiService.findAll().enqueue(object : Callback<List<RoomDto>> {
            override fun onResponse(call: Call<List<RoomDto>>, response: Response<List<RoomDto>>) {
                val rooms = response.body() ?: emptyList()
                // Display the RoomList composable with the fetched rooms
                setContent {
                    RoomList(rooms = rooms, navigateBack = navigateBack, openRoom = openRoom)
                }
            }

            override fun onFailure(call: Call<List<RoomDto>>, t: Throwable) {
                // Handle failure case
                setContent {
                    RoomList(rooms = emptyList(), navigateBack = navigateBack, openRoom = openRoom)
                }
                t.printStackTrace() // Log the error
                Toast.makeText(this@RoomListActivity, "Error loading rooms: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
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
