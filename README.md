# GameHok PvP Tournament Backend System

Backend assignment solution for the :contentReference[oaicite:0]{index=0} Backend Developer role.

## Project Overview

This project extends an existing Battle Royale tournament platform to support PvP tournament formats such as:

- 1v1 PvP
- 2v2 / 3v3 / 4v4 Clash Squad
- 5v5 Matches

The system supports:
- Team registration
- Bracket generation
- Match management
- Winner progression
- Redis caching
- REST APIs
- Swagger documentation

---

# Tech Stack

- Java 17
- Spring Boot
- PostgreSQL
- Redis
- Docker
- Swagger/OpenAPI

---

# Features

## Tournament Management
- Create tournaments
- Store tournament metadata

## Team Management
- Register teams
- Fetch registered teams

## Bracket System
- Generate tournament brackets
- Single elimination support

## Match Management
- Store match details
- Submit match results
- Fetch match information

## Redis Integration
- Cache active match details
- Reduce database load
- Improve response time

---

# Project Structure

```text
src/main/java/com/gamehok
│
├── controller
├── service
├── repository
├── entity
├── dto
└── config
```

---

# API Endpoints

## Tournament APIs

### Create Tournament
```http
POST /api/tournaments
```

---

## Team APIs

### Register Team
```http
POST /api/teams/register
```

### Get All Teams
```http
GET /api/teams
```

---

## Bracket APIs

### Generate Bracket
```http
POST /api/brackets/generate
```

---

## Match APIs

### Submit Match Result
```http
POST /api/matches/{matchId}/result
```

### Fetch Match Details
```http
GET /api/matches/{id}
```

---

# Redis Caching Flow

```text
Client Request
      ↓
Check Redis Cache
      ↓
If Present → Return Cached Data
      ↓
Else Fetch From PostgreSQL
      ↓
Store In Redis
      ↓
Return Response
```

---

# Database Entities

- Tournament
- Team
- TeamMember
- Match
- Bracket

---

# Swagger Documentation

Swagger UI:

```text
http://localhost:8087/swagger-ui/index.html
```

OpenAPI Docs:

```text
http://localhost:8087/v3/api-docs
```

---

# Running the Project

## 1. Clone Repository

```bash
git clone <repository-url>
```

---

## 2. Configure PostgreSQL

Update:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/gamehok
    username: postgres
    password: postgres
```

---

## 3. Configure Redis

```yaml
spring:
  redis:
    host: localhost
    port: 6379
```

---

## 4. Run Redis

```bash
docker run -d --name redis -p 6379:6379 redis
```

---

## 5. Run Application

```bash
./mvnw spring-boot:run
```

---

# Docker Support

## Dockerfile

```dockerfile
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY target/*.jar app.jar

EXPOSE 8087

ENTRYPOINT [\"java\", \"-jar\", \"app.jar\"]
```

---

# Future Improvements

- Double elimination brackets
- WebSocket live updates
- JWT authentication
- Match dispute system
- Kubernetes deployment
- Azure deployment
- Event-driven architecture

---

# Conclusion

This backend system provides a scalable and modular architecture for PvP tournament management while supporting future gaming tournament formats and real-time scalability.