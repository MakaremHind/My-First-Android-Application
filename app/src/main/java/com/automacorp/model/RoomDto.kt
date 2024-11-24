package com.automacorp.model

data class RoomDto(
    val id: Long, // Unique identifier for the room
    val name: String, // Room name
    val currentTemperature: Double?, // Nullable current temperature
    val targetTemperature: Double?, // Nullable target temperature
    val windows: List<WindowDto>, // List of windows in the room
)
