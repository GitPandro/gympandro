# gympandro

Applicazione per **personal trainer** per creare e gestire schede di
allenamento (clienti, piani, esercizi) con una UI semplice in Bootstrap
e un backend Java leggero con Postgres.

------------------------------------------------------------------------

## 🚀 Funzionalità attuali

-   📄 **Clienti**: creazione e lista clienti
-   🗒 **Schede (Sheets)**: elenco schede per cliente
-   🌐 API REST leggere (`/api/clients`, `/api/sheets`)
-   🖥 **UI** HTML/Bootstrap (`index.html`, `sheets.html`)
-   🗄 **Migrazioni DB** automatiche via [Flyway](https://flywaydb.org/)

------------------------------------------------------------------------

## 🛠 Stack Tecnologico

-   **Java 25** + `HttpServer` JDK (no Spring)
-   **Gradle 9.1+** (wrapper incluso)
-   **PostgreSQL 16** (via Docker)
-   **Flyway** per versioning schema
-   **Bootstrap 5** per UI
-   **Adminer** come DB viewer (http://localhost:8080)

------------------------------------------------------------------------

## 📦 Setup rapido

### 1. Avvia il database

``` bash
cd ops
docker compose up -d
```

Adminer è su <http://localhost:8080>\
Server: `db` -- DB: `gympandro` -- User: `gp_user` -- Pass: `gp_pass`

### 2. Compila & esegui

``` bash
cd backend
./gradlew run
```

Se funziona vedrai:

    DB connected: jdbc:postgresql://localhost:5432/gympandro
    GymPandro running on http://localhost:8081

------------------------------------------------------------------------

## 🌐 API di esempio

### Health check

``` bash
curl http://localhost:8081/api/health
# {"status":"UP"}
```

### Crea cliente

``` bash
curl -X POST http://localhost:8081/api/clients   -H "Content-Type: application/json"   -d '{"full_name":"Mario Rossi","email":"mario@example.com"}'
```

### Lista schede

``` bash
curl http://localhost:8081/api/sheets
```

------------------------------------------------------------------------

## 🧱 Struttura progetto

    backend/               → codice Java
      src/main/java/...
      src/main/resources/
        db/migration/      → file SQL per Flyway (V1__init.sql, V2__....sql)
    public/                → pagine HTML + Bootstrap
    ops/docker-compose.yml → Postgres + Adminer

------------------------------------------------------------------------

## 🗺 Roadmap

-    Form HTML per creare schede
-    Aggiunta esercizi a ogni scheda
-    Autenticazione trainer
-    Esportazione scheda in PDF
-    Statistiche di progresso cliente

------------------------------------------------------------------------

## 🤝 Contributi

PR benvenute!\
Apri issue per segnalare bug o proporre feature.

------------------------------------------------------------------------

## 📜 Licenza

MIT --- libera per uso personale e commerciale.