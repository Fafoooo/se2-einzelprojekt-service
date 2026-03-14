package at.aau.serg.controllers

import at.aau.serg.models.GameResult
import at.aau.serg.services.GameResultService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/leaderboard")
class LeaderboardController(
    private val gameResultService: GameResultService
) {

    @GetMapping 
    fun getLeaderboard(@RequestParam(required = false) rank: Int?): ResponseEntity<List<GameResult>> {
        //bisherige logik beibehalten, sortieren nach score (absteigend) und timeInSeconds (aufsteigend) 
        val sorted = gameResultService.getGameResults()
            .sortedWith(compareBy({ -it.score }, { it.timeInSeconds }))
        // Kein rank angegeben → ganzes Leaderboard (wie bisher, wenn rank nicht angegeben wird)
        if (rank == null) {
            return ResponseEntity.ok(sorted)
        }


        // Ungültiger rank → HTTP 400
        if (rank < 1 || rank > sorted.size) {
            return ResponseEntity.badRequest().build()
        }
        //Berechnung der 3 unteren/oberen Plätze, abhängig von der Position des Spielers
        val index = rank - 1
        val lowerLimit = maxOf(0, index - 3) //bei 0 Grenze
        val upperLimit = minOf(sorted.size, index + 4) //bei Ende Grenze (listenende)

        return ResponseEntity.ok(sorted.subList(lowerLimit, upperLimit))
    }
}
