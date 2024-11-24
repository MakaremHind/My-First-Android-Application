package com.automacorp.service

import androidx.core.text.isDigitsOnly
import com.automacorp.model.RoomDto
import com.automacorp.model.WindowDto
import com.automacorp.model.WindowStatus

object RoomService {
    private val ROOM_KIND: List<String> = listOf("Room", "Meeting", "Laboratory", "Office", "Boardroom")
    private val ROOM_NUMBER: List<Char> = ('A'..'Z').toList()
    private val WINDOW_KIND: List<String> = listOf("Sliding", "Bay", "Casement", "Hung", "Fixed")

    private fun generateWindow(id: Long, roomId: Long, roomName: String): WindowDto {
        return WindowDto(
            id = id,
            name = "${WINDOW_KIND.random()} Window $id",
            roomName = roomName,
            roomId = roomId,
            windowStatus = WindowStatus.values().random()
        )
    }

    private fun generateRoom(id: Long): RoomDto {
        val roomName = "${ROOM_NUMBER.random()}$id ${ROOM_KIND.random()}"
        val windows = (1..(1..6).random()).map { generateWindow(it.toLong(), id, roomName) }
        return RoomDto(
            id = id,
            name = roomName,
            currentTemperature = (15..30).random().toDouble(),
            targetTemperature = (15..22).random().toDouble(),
            windows = windows
        )
    }

    // Generate 50 rooms
    private val ROOMS = (1..50).map { generateRoom(it.toLong()) }.toMutableList()

    fun findAll(): List<RoomDto> {
        // Return all rooms sorted by name
        return ROOMS.sortedBy { it.name }
    }

    fun findById(id: Long): RoomDto? {
        // Find a room by ID
        return ROOMS.find { it.id == id }
    }

    fun findByName(name: String): RoomDto? {
        // Find a room by name
        return ROOMS.find { it.name.equals(name, ignoreCase = true) }
    }

    fun updateRoom(id: Long, room: RoomDto): RoomDto? {
        // Update an existing room with the given values
        val index = ROOMS.indexOfFirst { it.id == id }
        if (index == -1) throw IllegalArgumentException("Room with ID $id not found")

        val updatedRoom = ROOMS[index].copy(
            name = room.name,
            targetTemperature = room.targetTemperature,
            currentTemperature = room.currentTemperature
        )
        ROOMS[index] = updatedRoom
        return updatedRoom
    }

    fun findByNameOrId(nameOrId: String?): RoomDto? {
        if (nameOrId != null) {
            return if (nameOrId.all { it.isDigit() }) { // Kotlin-based check for digits
                findById(nameOrId.toLong())
            } else {
                findByName(nameOrId)
            }
        }
        return null
    }
}
