package com.automacorp.viewmodel

import com.automacorp.model.RoomDto

data class RoomList(
    val rooms: List<RoomDto> = emptyList(),
    val error: String? = null
)
