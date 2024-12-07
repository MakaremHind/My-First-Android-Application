package com.automacorp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.automacorp.model.RoomDto
import com.automacorp.service.RoomService
import com.automacorp.ui.theme.AutomacorpTheme

class RoomDetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Retrieve the room ID from the Intent
        val roomId = intent.getStringExtra(MainActivity.ROOM_PARAM)?.toLongOrNull()

        // Use a ViewModel to manage the room details
        val viewModel: RoomViewModel by viewModels()
        viewModel.room = roomId?.let { RoomService.findById(it) }

        // Define the back navigation action to return to RoomListActivity
        val navigateBack: () -> Unit = {
            startActivity(Intent(this, RoomListActivity::class.java))
        }

        setContent {
            AutomacorpTheme {
                Scaffold(
                    topBar = { AutomacorpTopAppBar("Room Detail", navigateBack) },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    if (viewModel.room != null) {
                        RoomDetailContent(viewModel, Modifier.padding(innerPadding))
                    } else {
                        NoRoomFound(Modifier.padding(innerPadding))
                    }
                }
            }
        }
    }
}

@Composable
fun RoomDetailContent(viewModel: RoomViewModel, modifier: Modifier = Modifier) {
    val room = viewModel.room
    room?.let {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Room Name: ${room.name}",
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = "Current Temperature: ${room.currentTemperature ?: "N/A"}°C",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Target Temperature: ${room.targetTemperature ?: "N/A"}°C",
                style = MaterialTheme.typography.bodyLarge
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

@Preview(showBackground = true, name = "Room Detail Activity Preview")
@Composable
fun RoomDetailActivityPreview() {
    val mockRoom = RoomDto(
        id = 1L,
        name = "Room EF 6.10",
        currentTemperature = 18.2,
        targetTemperature = 20.0,
        windows = emptyList()
    )

    val mockViewModel = RoomViewModel().apply {
        room = mockRoom
    }

    AutomacorpTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            )
            {
                // Label for RoomDetailPreview
                Text(
                    text = "RoomDetailPreview",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                RoomDetailContent(
                    viewModel = mockViewModel,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        }
    }
}


