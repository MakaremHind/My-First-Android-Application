package com.automacorp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.automacorp.model.RoomDto
import com.automacorp.service.RoomService
import com.automacorp.ui.theme.AutomacorpTheme

class RoomListActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AutomacorpTheme {
                Scaffold(
                    topBar = {
                        AutomacorpTopAppBar(
                            title = "Room List",
                            navigateBack = { finish() }, // Close this activity and return to the previous one
                            context = this
                        )
                    },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    RoomListScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun RoomListScreen(modifier: Modifier = Modifier) {
    // Fetch the list of rooms from RoomService
    val rooms = remember { RoomService.findAll() }

    // Display the list using LazyColumn
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(rooms) { room ->
            RoomListItem(room)
        }
    }
}

@Composable
fun RoomListItem(room: RoomDto) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Room Name: ${room.name}",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Current Temperature: ${room.currentTemperature ?: "N/A"}°C",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Target Temperature: ${room.targetTemperature ?: "N/A"}°C",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RoomListScreenPreview() {
    AutomacorpTheme {
        RoomListScreen()
    }
}
