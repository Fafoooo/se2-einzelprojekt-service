package at.aau.serg.controllers

import at.aau.serg.models.GameResult
import at.aau.serg.services.GameResultService
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mockito.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import org.mockito.Mockito.`when` as whenever

class GameResultControllerTests {

    private lateinit var mockedService: GameResultService
    private lateinit var controller: GameResultController

    @BeforeEach
    fun setup() {
        mockedService = mock<GameResultService>()
        controller = GameResultController(mockedService)
    }

    @Test
    fun test_getGameResult_existingId_returnsResult() {
        val gameResult = GameResult(1, "player1", 100, 15.0)
        
        whenever(mockedService.getGameResult(1)).thenReturn(gameResult) // mock gibt das GameResult zurück wenn ID 1 abgefragt wird

        val res = controller.getGameResult(1)

        verify(mockedService).getGameResult(1) // prüft ob Service aufgerufen wurde
        assertEquals(gameResult, res)
    }

    @Test
    fun test_getGameResult_nonExistingId_returnsNull() {
        
        whenever(mockedService.getGameResult(99)).thenReturn(null) // ID 99 existiert nicht, mock gibt null zurück

        val res = controller.getGameResult(99)

        verify(mockedService).getGameResult(99)
        assertNull(res) // kein Ergebnis gefunden
    }

    @Test
    fun test_addGameResult_callsService() {
        val gameResult = GameResult(0, "player1", 100, 15.0)

        controller.addGameResult(gameResult)

        
        verify(mockedService).addGameResult(gameResult) // prüft ob addGameResult am Service aufgerufen wurde
    }
}
