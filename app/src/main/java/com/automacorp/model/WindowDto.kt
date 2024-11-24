package com.automacorp.model

enum class WindowStatus {
    OPENED, // Window is open
    CLOSED  // Window is closed
}

data class WindowDto(
    val id: Long, // Unique identifier for the window
    val name: String, // Window name
    val roomName: String, // Name of the room containing the window
    val roomId: Long, // ID of the room containing the window
    val windowStatus: WindowStatus // Status of the window (OPENED or CLOSED)
)
