package com.automacorp.service

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
            windowStatus = WindowStatus.entries.random()
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
        return ROOMS.sortedBy { it.name }
    }

    // Add this function to provide access to the ROOMS for testing or previews
    fun getRoomAt(index: Int): RoomDto? {
        return ROOMS.getOrNull(index)
    }

    fun findById(id: Long): RoomDto? {
        return ROOMS.find { it.id == id }
    }

    fun findByName(name: String): RoomDto? {
        return ROOMS.find { it.name.equals(name, ignoreCase = true) }
    }

    fun updateRoom(id: Long, room: RoomDto): RoomDto {
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
        return if (nameOrId != null) {
            if (nameOrId.all { it.isDigit() }) findById(nameOrId.toLong())
            else findByName(nameOrId)
        } else null
    }
}

