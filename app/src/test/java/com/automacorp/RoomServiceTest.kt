package com.automacorp

import com.automacorp.model.RoomDto
import com.automacorp.service.RoomService
import org.junit.Assert.*
import org.junit.Test

class RoomServiceTest {

    @Test
    fun testFindAll() {
        val rooms = RoomService.findAll()
        assertNotNull(rooms)
        assertTrue(rooms.isNotEmpty())
        assertTrue(rooms == rooms.sortedBy { it.name }) // Ensure sorting by name
    }

    @Test
    fun testFindById() {
        val room = RoomService.findById(1)
        assertNotNull(room)
        assertEquals(1L, room?.id) // Use Long for the comparison
    }

    @Test
    fun testFindByName() {
        val name = RoomService.findAll().first().name // Pick a known name
        val room = RoomService.findByName(name)
        assertNotNull(room)
        assertEquals(name, room?.name)
    }

    @Test
    fun testUpdateRoom() {
        val originalRoom = RoomService.findById(1)
        val updatedRoom = RoomService.updateRoom(1, originalRoom!!.copy(name = "Updated Room"))
        assertNotNull(updatedRoom)
        assertEquals("Updated Room", updatedRoom?.name)
    }

    @Test
    fun testFindByNameOrId() {
        val roomById = RoomService.findByNameOrId("1")
        val roomByName = RoomService.findByNameOrId("Updated Room")
        assertNotNull(roomById)
        assertNotNull(roomByName)
    }
}
