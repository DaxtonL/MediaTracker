# Media Tracker

A web app for tracking the movies, shows, games, books and manga you're watching, playing or reading, all in one place instead of spread across separate sites.

It began as a Java Swing desktop app and has been rebuilt as a Spring Boot REST API with a React frontend, backed by PostgreSQL.

## Features

- **Multiple trackers.** Keep separate lists, e.g. one for personal media and one for a shared room.
- **Five media types:** movies (minutes), shows (episodes), games (hours), books (pages) and manga (chapters).
- **Status tracking:** Waitlist, Viewing, Finished, On hold, Dropped.
- **Progress logging.** Log a dated viewing with how much you got through. Logging sets the status to Viewing, or Finished once total progress reaches the media's length. The most recent log can be undone.
- **Priority** for waitlisted media and a **rating** from 0 to 10.
- **Search, filter and sort** within a tracker by name, status, type and rating range.

## Tech stack

| Part      | Technology                                                       |
|-----------|------------------------------------------------------------------|
| Backend   | Java 21, Spring Boot 4, Spring Data JPA, Bean Validation, Maven  |
| Database  | PostgreSQL 17, schema managed by Flyway                          |
| Frontend  | React 19, React Router, Vite                                     |
| Tests     | JUnit 5, Spring Boot Test, MockMvc, H2 (PostgreSQL mode)         |
| Deploying | Docker Compose, Caddy (static files, API proxy, automatic HTTPS) |

## Project structure

```text
MediaTracker/
├── backend/                     Spring Boot API
│   ├── src/main/java/com/daxtonl/mediatracker/
│   │   ├── controller/          REST endpoints
│   │   ├── service/             Business logic and transactions
│   │   ├── repository/          Spring Data repositories
│   │   ├── model/               JPA entities (Tracker, Media, ViewLog) and enums
│   │   ├── dto/                 Request/response records with validation
│   │   └── importer/            One-time import of old Swing app JSON files
│   ├── src/main/resources/
│   │   ├── application.properties
│   │   └── db/migration/        Flyway SQL migrations
│   ├── api.http                 Example requests for every endpoint
│   ├── docker-compose.yml       Local PostgreSQL only (for development)
│   └── Dockerfile
├── frontend/                    React app
│   ├── src/pages/               Tracker list and tracker pages
│   ├── src/components/          Modals and shared UI pieces
│   ├── Caddyfile                Production web server config
│   └── Dockerfile
├── data/                        Your old Swing app JSON files (git-ignored)
├── docker-compose.yml           Full stack for deployment
└── .env.example                 Deployment settings template
```

## Running locally

### Requirements

- Java 21
- Node.js 22+
- PostgreSQL 17, installed locally or run with Docker

### 1. Start PostgreSQL

The backend connects to database `mediatracker` on `localhost:5432` with user `mediatracker`, password `mediatracker`.

With Homebrew:

```bash
brew install postgresql@17
brew services start postgresql@17
createuser -s mediatracker
psql -d postgres -c "alter user mediatracker password 'mediatracker'"
createdb -O mediatracker mediatracker
```

Or with Docker:

```bash
cd backend
docker compose up -d
```

To use a different database, set `DB_URL`, `DB_USERNAME` and `DB_PASSWORD` before starting the backend.

### 2. Start the backend

```bash
cd backend
./mvnw spring-boot:run
```

The API runs on http://localhost:8080. On startup, Flyway creates or updates the tables. Any tracker files in `data/` are then imported. Trackers that already exist in the database are skipped, so restarting doesn't create duplicates. Set `mediatracker.import.enabled=false` to turn the import off.

In VS Code, you can use the **Backend (Spring Boot)** launch configuration instead.

### 3. Start the frontend

```bash
cd frontend
npm install
npm run dev
```

Open http://localhost:5173. Vite forwards `/api` requests to the backend on port 8080.

## Tests

```bash
cd backend
./mvnw test
```

The tests use an in-memory H2 database in PostgreSQL mode, so they don't need a running Postgres. Flyway still applies the real migrations to it, so a broken migration fails the tests.

## API

All endpoints are under `/api`. [`backend/api.http`](backend/api.http) has example requests for each one, including error cases.

| Method | Path                              | Description                                                                            |
|--------|-----------------------------------|----------------------------------------------------------------------------------------|
| GET    | `/api/trackers`                   | List trackers with their media counts                                                  |
| POST   | `/api/trackers`                   | Create a tracker: `{ "name": "Games" }`                                                |
| GET    | `/api/trackers/{id}`              | Get one tracker                                                                        |
| GET    | `/api/trackers/{id}/media`        | List a tracker's media. Optional filters: `status`, `type`, `minRating`, `maxRating`   |
| POST   | `/api/trackers/{id}/media`        | Add media: `{ "name", "type", "length", "priority" }`                                  |
| GET    | `/api/media/{id}`                 | Get media with its full viewing history                                                |
| PATCH  | `/api/media/{id}`                 | Update any of `name`, `type`, `status`, `length`, `priority`, `rating`                 |
| DELETE | `/api/media/{id}`                 | Remove media                                                                           |
| POST   | `/api/media/{id}/logs`            | Log a viewing: `{ "viewedOn": "2026-11-23", "viewProgress": 3 }`                        |
| DELETE | `/api/media/{id}/logs/latest`     | Undo the most recent viewing                                                           |

Invalid input returns `400`, unknown IDs return `404`, and a duplicate media name within a tracker returns `409`.

## Database changes

Hibernate checks the schema but never changes it. To change an entity:

1. Update the entity class in `model/`.
2. Add a new migration in `backend/src/main/resources/db/migration/`, named with the next version number, e.g. `V2__add_notes_to_media.sql`. Never edit a migration that has already been applied.
3. Run `./mvnw test` to check that the migration and entities match.

Adding a value to `MediaType` or `Status` also needs a migration, because the `media` table has check constraints listing the allowed values.

## Deploying

The root `docker-compose.yml` runs three containers:

- **web:** Caddy serves the built frontend and forwards `/api` to the backend.
- **backend:** the Spring Boot API.
- **db:** PostgreSQL, with its data in a Docker volume.

Only `web` is reachable from outside, on ports 80 and 443.

On a Linux server with Docker installed:

```bash
git clone <repo-url> && cd MediaTracker
cp .env.example .env   # set DB_PASSWORD, and SITE_ADDRESS if you have a domain
docker compose up -d --build
```

If `SITE_ADDRESS` is set to a domain whose DNS points at the server, Caddy gets an HTTPS certificate for it automatically. Otherwise the site is served over plain HTTP on port 80.

To update, run `git pull && docker compose up -d --build`.

The deployed app starts with an empty database; the JSON import is turned off there. There are no user accounts, so anyone with the link can edit the data.
