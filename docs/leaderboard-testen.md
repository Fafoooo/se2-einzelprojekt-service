# Leaderboard manuell testen

## Server starten

```bash
./gradlew bootRun
```

## Spieler anlegen

```bash
curl -X POST http://localhost:8080/game-results -H "Content-Type: application/json" -d '{"id":0,"playerName":"Alice","score":100,"timeInSeconds":12.5}'
curl -X POST http://localhost:8080/game-results -H "Content-Type: application/json" -d '{"id":0,"playerName":"Bob","score":80,"timeInSeconds":10.0}'
curl -X POST http://localhost:8080/game-results -H "Content-Type: application/json" -d '{"id":0,"playerName":"Charlie","score":100,"timeInSeconds":15.0}'
```

## Leaderboard abrufen (ohne rank)

```bash
curl http://localhost:8080/leaderboard
```

Erwartete Reihenfolge:
1. Alice (Score 100, Zeit 12.5s)
2. Charlie (Score 100, Zeit 15.0s)
3. Bob (Score 80, Zeit 10.0s)

Sortierung: Score absteigend, bei gleichem Score nach kuerzerer Zeit.

## Leaderboard mit rank

```bash
curl "http://localhost:8080/leaderboard?rank=1"
```

Gibt den Spieler auf Platz 1 + bis zu 3 Plaetze darueber/darunter zurueck.

## Ungueltiger rank (HTTP 400)

```bash
curl -v "http://localhost:8080/leaderboard?rank=0"
curl -v "http://localhost:8080/leaderboard?rank=99"
```

Gibt HTTP 400 Bad Request zurueck wenn rank kleiner 1 oder groesser als Anzahl Spieler.

## Swagger UI

Alternativ im Browser testen: http://localhost:8080/swagger-ui/index.html
