package com.automacorp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.automacorp.model.RoomDto
import com.automacorp.ui.theme.AutomacorpTheme

class RoomActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Retrieve the room ID from the Intent
        val roomId = intent.getStringExtra(MainActivity.ROOM_PARAM)?.toLongOrNull()

        // Use ViewModel to manage room details
        val viewModel: RoomViewModel by viewModels()

        // Load room from the remote API
        roomId?.let { viewModel.findRoom(it) }

        // Save updated room to the remote API
        val onRoomSave: () -> Unit = {
            viewModel.room?.let { room ->
                viewModel.updateRoom(room.id, room)
                Toast.makeText(this, "Room ${room.name} was updated", Toast.LENGTH_LONG).show()
                startActivity(Intent(this, MainActivity::class.java))
            }
        }

        // Define the back navigation action
        val navigateBack: () -> Unit = {
            startActivity(Intent(this, MainActivity::class.java))
        }

        setContent {
            AutomacorpTheme {
                Scaffold(
                    topBar = { AutomacorpTopAppBar("Room", navigateBack) },
                    floatingActionButton = { RoomUpdateButton(onRoomSave) },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    if (viewModel.room != null) {
                        RoomDetail(model = viewModel, modifier = Modifier.padding(innerPadding))
                    } else {
                        NoRoom(Modifier.padding(innerPadding))
                    }
                }
            }
        }
    }
}

@Composable
fun RoomUpdateButton(onClick: () -> Unit) {
    ExtendedFloatingActionButton(
        onClick = { onClick() },
        icon = {
            Icon(
                imageVector = Icons.Filled.Done,
                contentDescription = stringResource(R.string.act_room_save)
            )
        },
        text = { Text(text = stringResource(R.string.act_room_save)) }
    )
}

@Composable
fun RoomDetail(model: RoomViewModel, modifier: Modifier = Modifier) {
    val room = model.room
    if (room != null) {
        Column(modifier = modifier.padding(16.dp)) {
            // Room Name
            OutlinedTextField(
                value = room.name,
                onValueChange = { model.room = room.copy(name = it) },
                label = { Text(text = stringResource(R.string.act_room_name)) },
                modifier = Modifier.fillMaxWidth()
            )

            // Current Temperature
            Text(
                text = stringResource(R.string.act_room_current_temperature) + ": " +
                        (room.currentTemperature?.toString() ?: "N/A"),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 16.dp)
            )

            // Target Temperature with Slider
            Text(
                text = stringResource(R.string.act_room_target_temperature),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 16.dp)
            )
            Slider(
                value = room.targetTemperature?.toFloat() ?: 18.0f,
                onValueChange = { model.room = room.copy(targetTemperature = it.toDouble()) },
                valueRange = 10f..28f,
                colors = SliderDefaults.colors(
                    thumbColor = MaterialTheme.colorScheme.secondary,
                    activeTrackColor = MaterialTheme.colorScheme.secondary,
                    inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer
                )
            )
            Text(
                text = (room.targetTemperature?.let { "%.1f".format(it) } ?: "N/A"),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
fun NoRoom(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.act_room_none),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Preview(showBackground = true, name = "Room Activity Preview with Labels")
@Composable
fun RoomActivityPreview() {
    val mockRoom = RoomDto(
        id = 1L,
        name = "Room EF 6.10",
        currentTemperature = 18.2,
        targetTemperature = 20.0,
        windows = emptyList()
    )
    // Simulate the view model data in the preview
    val mockViewModel = RoomViewModel().apply {
        room = mockRoom
    }

    AutomacorpTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Label for RoomDetailPreview
            Text(
                text = "RoomDetailPreview",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // RoomDetail section
            Scaffold(
                floatingActionButton = { RoomUpdateButton(onClick = { /* No-op for preview */ }) },
                modifier = Modifier.weight(1f)
            ) {
                RoomDetail(model = mockViewModel, modifier = Modifier.padding(it))
            }

            // Label for NoRoomDetailPreview
            Text(
                text = "NoRoomDetailPreview",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            // NoRoom section
            Scaffold(
                modifier = Modifier.weight(1f)
            ) {
                NoRoom(modifier = Modifier.padding(it))
            }
        }
    }
}
