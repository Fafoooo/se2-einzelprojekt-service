package at.aau.serg.controllers

import at.aau.serg.models.GameResult
import at.aau.serg.services.GameResultService
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.springframework.http.HttpStatus // Für die Überprüfung des Statuscodes in den Tests
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

    @Test
    fun test_getLeaderboard_emptyList() {
        whenever(mockedService.getGameResults()).thenReturn(emptyList())

        val res = controller.getLeaderboard(null)

        assertEquals(HttpStatus.OK, res.statusCode)
        assertEquals(0, res.body!!.size)
    }

    @Test
    fun test_getLeaderboard_withRank_returnsWindow() {
        // 10 Spieler erstellen, jeder hat anderen Score
        val results = (1..10).map { GameResult(it.toLong(), "player$it", 100 - it, 10.0 + it) }
        whenever(mockedService.getGameResults()).thenReturn(results)

        // rank 5 = index 4, Fenster: index 1 bis 7 (3 drüber + Spieler + 3 drunter)
        val res = controller.getLeaderboard(5)

        assertEquals(HttpStatus.OK, res.statusCode)
        assertEquals(7, res.body!!.size)
        assertEquals(results[1], res.body!![0]) // Platz 2
        assertEquals(results[4], res.body!![3]) // Platz 5 (angefragter Spieler)
        assertEquals(results[7], res.body!![6]) // Platz 8
    }

    @Test
    fun test_getLeaderboard_withRank1_windowAtTop() {
        // rank 1 = index 0, kann nicht 3 drüber gehen → Fenster startet bei 0
        val results = (1..10).map { GameResult(it.toLong(), "player$it", 100 - it, 10.0 + it) }
        whenever(mockedService.getGameResults()).thenReturn(results)

        val res = controller.getLeaderboard(1)

        assertEquals(HttpStatus.OK, res.statusCode)
        assertEquals(4, res.body!!.size) // nur Spieler + 3 drunter
        assertEquals(results[0], res.body!![0])
    }

    @Test
    fun test_getLeaderboard_withLastRank_windowAtBottom() {
        // rank 10 = index 9, kann nicht 3 drunter gehen → Fenster endet bei 9
        val results = (1..10).map { GameResult(it.toLong(), "player$it", 100 - it, 10.0 + it) }
        whenever(mockedService.getGameResults()).thenReturn(results)

        val res = controller.getLeaderboard(10)

        assertEquals(HttpStatus.OK, res.statusCode)
        assertEquals(4, res.body!!.size) // nur 3 drüber + Spieler
        assertEquals(results[9], res.body!![3])
    }

    @Test
    fun test_getLeaderboard_rankZero_returns400() {
        whenever(mockedService.getGameResults()).thenReturn(listOf(GameResult(1, "p", 10, 5.0)))

        val res = controller.getLeaderboard(0)

        assertEquals(HttpStatus.BAD_REQUEST, res.statusCode)
    }

    @Test
    fun test_getLeaderboard_rankNegative_returns400() {
        whenever(mockedService.getGameResults()).thenReturn(listOf(GameResult(1, "p", 10, 5.0)))

        val res = controller.getLeaderboard(-1)

        assertEquals(HttpStatus.BAD_REQUEST, res.statusCode)
    }

    @Test
    fun test_getLeaderboard_rankTooLarge_returns400() {
        whenever(mockedService.getGameResults()).thenReturn(listOf(GameResult(1, "p", 10, 5.0)))

        // nur 1 Spieler, aber rank 5 → ungültig
        val res = controller.getLeaderboard(5)

        assertEquals(HttpStatus.BAD_REQUEST, res.statusCode)
    }

}