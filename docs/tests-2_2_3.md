# 2.2.3 Tests - 100% Coverage

## Änderung der build.gradle.ktx

### build.gradle.kts
- `jacoco` Plugin hinzugefügt
- `finalizedBy("jacocoTestReport")` damit nach jedem Test ein Coverage-Report erzeugt wird
- Report liegt nach `./gradlew test` unter: `build/reports/jacoco/test/html/index.html`

### Aktueller Stand
Die bestehenden Tests kompilieren nicht mehr.
