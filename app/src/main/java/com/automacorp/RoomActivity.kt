package com.automacorp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.automacorp.model.RoomDto
import com.automacorp.service.RoomService
import com.automacorp.ui.theme.AutomacorpTheme

class RoomActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val param = intent.getStringExtra(MainActivity.ROOM_PARAM)
        val room = RoomService.findByNameOrId(param)

        setContent {
            AutomacorpTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    if (room != null) {
                        RoomDetail(room, Modifier.padding(innerPadding))
                    } else {
                        NoRoom(Modifier.padding(innerPadding))
                    }
                }
            }
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

@Composable
fun RoomDetail(roomDto: RoomDto, modifier: Modifier = Modifier) {
    var room by remember { mutableStateOf(roomDto) }

    Column(modifier = modifier.padding(16.dp)) {
        // Room Name
        OutlinedTextField(
            value = room.name,
            onValueChange = { room = room.copy(name = it) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(stringResource(R.string.act_room_name)) }
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
            onValueChange = { room = room.copy(targetTemperature = it.toDouble()) },
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

@Preview(showBackground = true)
@Composable
fun RoomDetailPreview() {
    val sampleRoom = RoomDto(
        id = 1L,
        name = "Sample Room",
        currentTemperature = 22.5,
        targetTemperature = 24.0,
        windows = emptyList() // Provide an empty list of windows for simplicity
    )

    AutomacorpTheme {
        RoomDetail(roomDto = sampleRoom)
    }
}
