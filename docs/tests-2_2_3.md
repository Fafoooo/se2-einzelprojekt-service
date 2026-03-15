# 2.2.3 Tests - 100% Coverage

## Änderung der build.gradle.kts

### build.gradle.kts
- `jacoco` Plugin hinzugefügt
- `finalizedBy("jacocoTestReport")` damit nach jedem Test ein Coverage-Report erzeugt wird
- Report liegt nach `./gradlew test` unter: `build/reports/jacoco/test/html/index.html`

### Aktueller Stand
Die bestehenden LeaderboardControllerTests haben nicht mehr kompiliert, weil:
- `getLeaderboard()` jetzt `ResponseEntity` zurückgibt statt `List`
- `getLeaderboard()` jetzt einen Parameter `rank: Int?` erwartet

### Fix der bestehenden Tests
- `getLeaderboard()` → `getLeaderboard(null)` (null = kein rank)
- `val res: List<GameResult>` → `val res =` (Typ automatisch erkennen lassen)
- `res.size` → `res.body!!.size` (Liste steckt jetzt in .body)
- `res[0]` → `res.body!![0]` (gleicher Grund)

### Jacoco Ergebnis nach Fix (noch nicht 100%)
| Package | Instructions | Branches |
|---|---|---|
| at.aau.serg | 55% | 30% |
| at.aau.serg.controllers | 39% | 16% |
| at.aau.serg.services | 73% | 75% |
| at.aau.serg.models | 65% | n/a |


## Öffnen des Tests
`open build/reports/jacoco/test/html/index.html`

## GameResultServiceTests erweitert

### Änderung
- 2 neue Tests für `deleteGameResult` hinzugefügt:
  - `test_deleteGameResult_existingId_removesElement` → prüft ob Element entfernt wird (assertTrue)
  - `test_deleteGameResult_nonexistentId_returnsFalse` → prüft ob false zurückkommt wenn ID nicht existiert (assertFalse)
- Neue Imports: `assertTrue`, `assertFalse`

### Jacoco Ergebnis nach Änderung
| Package | Instructions | Branches |
|---|---|---|
| at.aau.serg | 57% | 42% |
| at.aau.serg.controllers | 39% | 16% |
| at.aau.serg.models | 65% | n/a |
| at.aau.serg.services | 100% | 100% |

## LeaderboardControllerTests erweitert

### Änderung
- 7 neue Tests für den `rank` Parameter hinzugefügt:
  - `test_getLeaderboard_emptyList` → leere Liste, kein rank
  - `test_getLeaderboard_withRank_returnsWindow` → rank in der Mitte, Fenster ±3
  - `test_getLeaderboard_withRank1_windowAtTop` → rank 1, Fenster am oberen Rand
  - `test_getLeaderboard_withLastRank_windowAtBottom` → letzter rank, Fenster am unteren Rand
  - `test_getLeaderboard_rankZero_returns400` → rank 0 → HTTP 400
  - `test_getLeaderboard_rankNegative_returns400` → rank -1 → HTTP 400
  - `test_getLeaderboard_rankTooLarge_returns400` → rank größer als Spieleranzahl → HTTP 400
- Neuer Import: `HttpStatus` für Statuscode-Prüfung

### Jacoco Ergebnis nach Änderung
| Package | Instructions | Branches |
|---|---|---|
| at.aau.serg | 62% | 55% |
| at.aau.serg.controllers | 76% | 100% |
| at.aau.serg.models | 65% | n/a |
| at.aau.serg.services | 100% | 100% |