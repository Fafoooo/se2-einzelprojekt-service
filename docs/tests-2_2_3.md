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

