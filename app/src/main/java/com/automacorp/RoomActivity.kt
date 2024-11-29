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
import com.automacorp.service.RoomService
import com.automacorp.ui.theme.AutomacorpTheme
import androidx.compose.ui.unit.dp

class RoomActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Retrieve the room parameter from the Intent
        val param = intent.getStringExtra(MainActivity.ROOM_PARAM)

        // Use a ViewModel to store the room state
        val viewModel: RoomViewModel by viewModels()
        viewModel.room = RoomService.findByNameOrId(param)

        // Define the save action for the Floating Action Button
        val onRoomSave: () -> Unit = {
            viewModel.room?.let { room ->
                RoomService.updateRoom(room.id, room)
                Toast.makeText(this, "Room ${room.name} was updated", Toast.LENGTH_LONG).show()
                startActivity(Intent(this, MainActivity::class.java))
            }
        }

        // Set the content of the activity
        setContent {
            AutomacorpTheme {
                Scaffold(
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

// Composable for the Floating Action Button
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

// Composable for Room Detail
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

// Composable for No Room Found
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
