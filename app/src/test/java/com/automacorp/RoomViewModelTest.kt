import com.automacorp.RoomViewModel
import com.automacorp.model.RoomDto
import com.automacorp.service.RoomService
import org.junit.Assert.*
import org.junit.Test

class RoomViewModelTest {
    @Test
    fun testRoomInitialization() {
        val viewModel = RoomViewModel()
        val room = RoomService.findById(1L)
        viewModel.room = room

        assertNotNull(viewModel.room)
        assertEquals(room?.name, viewModel.room?.name)
    }

    @Test
    fun testRoomUpdate() {
        val viewModel = RoomViewModel()
        val room = RoomService.findById(1L)
        viewModel.room = room

        viewModel.room = viewModel.room?.copy(name = "Updated Room")
        assertEquals("Updated Room", viewModel.room?.name)
    }
}
