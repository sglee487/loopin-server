# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build and Development Commands

### Build and Run Services
- **Build all services**: `./gradlew build` (run from root directory)
- **Build specific service**: `cd <service-name> && ./gradlew build`
- **Run service locally**: `cd <service-name> && ./gradlew bootRun`
- **Run full stack**: `cd loopin-deployment/docker && docker compose up --build`

### Testing
- **Run all tests**: `./gradlew test`
- **Run tests for specific service**: `cd <service-name> && ./gradlew test`
- **Run single test class**: `cd <service-name> && ./gradlew test --tests "com.loopin.*Test"`

### Database Operations
- **Database migrations**: Handled automatically by Flyway on service startup
- **Migration files**: Located in `*/src/main/resources/db/migration/`
- **PostgreSQL access**: Connect to `localhost:54322` (user/password)

## Architecture Overview

### Microservice Architecture
This is a microservices platform with 4 main services communicating through REST APIs:

1. **gateway-service** (Port 59000): Spring Cloud Gateway with OAuth2, session management via Redis, circuit breakers for downstream services
2. **media-catalog-service** (Port 59001): R2DBC/PostgreSQL for media items and playlists, Redis caching, Bucket4j rate limiting, YouTube API integration
3. **playback-service** (Port 59002): R2DBC/PostgreSQL for user playback sessions, communicates with media-catalog-service
4. **youtube-fetcher-service** (Port 59011): Google YouTube Data API client, pushes data to media-catalog-service

### Key Patterns
- **Reactive Programming**: All services use Spring WebFlux with R2DBC for non-blocking operations
- **Circuit Breakers**: Gateway service implements Resilience4j circuit breakers for downstream calls
- **Authentication Flow**: Keycloak OAuth2 → Gateway (session) → Services (JWT resource server)
- **Database Per Service**: Each service owns its PostgreSQL database with Flyway migrations
- **Caching Strategy**: media-catalog-service uses Redis for hot data caching

### Inter-Service Communication
- Gateway routes `/api/v1/user-play-session/**` → playback-service
- Gateway routes `/api/v1/playlists/**` → media-catalog-service
- media-catalog-service calls youtube-fetcher-service for YouTube data
- playback-service calls media-catalog-service for media metadata

### Configuration Management
- **Environment Variables**: Services use extensive env var configuration (see application.yml files)
- **Profiles**: `application-youtube-sync.yml` profile disables web/Redis for batch operations
- **Service Discovery**: Uses Kubernetes client config in production, hardcoded URLs in development

### Database Schema
- **media-catalog-service**: media_item, media_playlist, playlist_item_mapping tables with fractional indexing for ordering
- **playback-service**: play_session table tracking user media consumption
- **Recent Changes**: videoId field added to MediaItem (V4 migration)

### Special Features
- **YouTube Sync**: Cron job batch process (`YoutubePlaylistSynchronizer`) syncs YouTube playlists
- **Rate Limiting**: Bucket4j limits YouTube playlist operations (5 requests/day per user)
- **Observability**: Full OpenTelemetry integration with Grafana/Prometheus/Tempo stack
- **Fractional Indexing**: Uses davidarvelo/fractional-indexing for playlist item ordering

### Development Notes
- **Hot Reload**: Use `./gradlew bootRun` for individual service development
- **Integration Testing**: Services use TestContainers for PostgreSQL and Redis
- **Security**: OAuth2 resource server pattern, JWT validation, session-based gateway auth
- **API Versioning**: All APIs prefixed with `/api/v1/`