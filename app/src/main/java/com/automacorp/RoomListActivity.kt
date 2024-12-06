package com.automacorp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.automacorp.model.RoomDto
import com.automacorp.service.RoomService
import com.automacorp.ui.theme.AutomacorpTheme
import com.automacorp.ui.theme.PurpleGrey80

class RoomListActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AutomacorpTheme {
                Scaffold(
                    topBar = {
                        AutomacorpTopAppBar(
                            title = "Room List",
                            navigateBack = { finish() } // Navigate back to the previous screen
                        )
                    },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    RoomListScreen(
                        modifier = Modifier.padding(innerPadding),
                        onRoomClick = { roomId -> openRoom(roomId) }
                    )
                }
            }
        }
    }

    // Function to open the RoomActivity
    private fun openRoom(roomId: Long) {
        val intent = Intent(this, RoomActivity::class.java).apply {
            putExtra(MainActivity.ROOM_PARAM, roomId.toString())
        }
        startActivity(intent)
    }
}

@Composable
fun RoomListScreen(modifier: Modifier = Modifier, onRoomClick: (Long) -> Unit) {
    // Fetch the list of rooms from RoomService
    val rooms = remember { RoomService.findAll() }

    // Display the list using LazyColumn
    LazyColumn(
        contentPadding = PaddingValues(4.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        items(rooms, key = { it.id }) { room ->
            RoomItem(
                room = room,
                modifier = Modifier.fillMaxWidth(),
                onClick = { onRoomClick(room.id) } // Pass room ID to the click handler
            )
        }
    }
}

@Composable
fun RoomItem(room: RoomDto, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = BorderStroke(1.dp, PurpleGrey80),
        modifier = modifier.clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = room.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Target temperature: ${room.targetTemperature?.toString() ?: "?"}°",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Text(
                text = "${room.currentTemperature?.toString() ?: "?"}°",
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Right,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RoomListScreenPreview() {
    AutomacorpTheme {
        RoomListScreen(
            onRoomClick = { /* No-op for preview */ } // Provide a default value
        )
    }
}

