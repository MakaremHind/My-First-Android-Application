package com.automacorp

import RoomCommandDto
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.automacorp.model.RoomDto
import com.automacorp.model.WindowDto
import com.automacorp.model.WindowStatus
import com.automacorp.service.ApiServices
import com.automacorp.viewmodel.RoomList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RoomViewModel : ViewModel() {
    // Current room state
    var room by mutableStateOf<RoomDto?>(null)

    // State flow to manage the list of rooms
    private val _roomsState = MutableStateFlow(RoomList())
    val roomsState: StateFlow<RoomList> get() = _roomsState

    // Helper function to log errors
    private fun logError(message: String, throwable: Throwable) {
        println("Error: $message")
        throwable.printStackTrace()
    }

    // Fetch all rooms from the API
    fun findAll() {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching { ApiServices.roomsApiService.findAll().execute() }
                .onSuccess { response ->
                    val rooms = response.body() ?: emptyList()
                    _roomsState.value = RoomList(rooms)
                }
                .onFailure {
                    logError("Failed to fetch rooms", it)
                    _roomsState.value = RoomList(emptyList(), it.localizedMessage ?: "Unknown error")
                }
        }
    }

    // Fetch a single room by its ID
    fun findRoom(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching { ApiServices.roomsApiService.findById(id).execute() }
                .onSuccess { response ->
                    room = response.body()
                }
                .onFailure {
                    logError("Failed to fetch room with ID $id", it)
                    room = null
                }
        }
    }

    // Update a room's details
    fun updateRoom(id: Long, roomDto: RoomDto) {
        val command = RoomCommandDto(
            name = roomDto.name,
            targetTemperature = roomDto.targetTemperature?.let { Math.round(it * 10) / 10.0 },
            currentTemperature = roomDto.currentTemperature
        )
        viewModelScope.launch(Dispatchers.IO) {
            runCatching { ApiServices.roomsApiService.updateRoom(id, command).execute() }
                .onSuccess { response ->
                    room = response.body()
                }
                .onFailure {
                    logError("Failed to update room with ID $id", it)
                    room = null
                }
        }
    }

    // Create a new room
    fun createRoom(roomDto: RoomCommandDto) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching { ApiServices.roomsApiService.createRoom(roomDto).execute() }
                .onSuccess { response ->
                    room = response.body()
                }
                .onFailure {
                    logError("Failed to create a new room", it)
                }
        }
    }

    // Delete a room by its ID
    fun deleteRoom(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching { ApiServices.roomsApiService.deleteRoom(id).execute() }
                .onFailure {
                    logError("Failed to delete room with ID $id", it)
                }
        }
    }

}
