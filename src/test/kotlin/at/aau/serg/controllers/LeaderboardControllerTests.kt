package at.aau.serg.controllers

import at.aau.serg.models.GameResult
import at.aau.serg.services.GameResultService
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import kotlin.test.Test
import kotlin.test.assertEquals
import org.mockito.Mockito.`when` as whenever // when is a reserved keyword in Kotlin

class LeaderboardControllerTests {

    private lateinit var mockedService: GameResultService
    private lateinit var controller: LeaderboardController

    @BeforeEach
    fun setup() {
        mockedService = mock<GameResultService>()
        controller = LeaderboardController(mockedService)
    }

    @Test
    fun test_getLeaderboard_correctScoreSorting() {
        val first = GameResult(1, "first", 20, 20.0)
        val second = GameResult(2, "second", 15, 10.0)
        val third = GameResult(3, "third", 10, 15.0)

        whenever(mockedService.getGameResults()).thenReturn(listOf(second, first, third))

        // null = kein rank angegeben, gibt ganzes Leaderboard zurück
        val res = controller.getLeaderboard(null)

        verify(mockedService).getGameResults()
        // .body!! weil getLeaderboard jetzt ResponseEntity zurückgibt, Liste steckt in .body
        assertEquals(3, res.body!!.size)
        assertEquals(first, res.body!![0])
        assertEquals(second, res.body!![1])
        assertEquals(third, res.body!![2])
    }

    /* Alte Testmethode, die nach der Änderung der Sortierung nicht mehr gültig ist.
    @Test
    fun test_getLeaderboard_sameScore_CorrectIdSorting() {
        val first = GameResult(1, "first", 20, 20.0)
        val second = GameResult(2, "second", 20, 10.0)
        val third = GameResult(3, "third", 20, 15.0)

        whenever(mockedService.getGameResults()).thenReturn(listOf(second, first, third))

        val res: List<GameResult> = controller.getLeaderboard()

        verify(mockedService).getGameResults()
        assertEquals(3, res.size)
        assertEquals(first, res[0])
        assertEquals(second, res[1])
        assertEquals(third, res[2])
    }
    */

    @Test
    fun test_getLeaderboard_sameScore_CorrectTimeSorting() {
        val first = GameResult(1, "first", 20, 10.0) // Bessere Zeit, sollte vor den anderen beiden liegen
        val second = GameResult(2, "second", 20, 15.0)
        val third = GameResult(3, "third", 20, 20.0) //muss letztes Element sein, da schlechteste Zeit

        whenever(mockedService.getGameResults()).thenReturn(listOf(second, first, third))

        // null = kein rank angegeben, gibt ganzes Leaderboard zurück
        val res = controller.getLeaderboard(null)

        verify(mockedService).getGameResults()
        // .body!! weil getLeaderboard jetzt ResponseEntity zurückgibt, Liste steckt in .body
        assertEquals(3, res.body!!.size)
        assertEquals(first, res.body!![0])
        assertEquals(second, res.body!![1])
        assertEquals(third, res.body!![2])

    }


}