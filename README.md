# User Service

A Spring Boot microservice for managing user accounts, preferences, and authentication in the Travel ecosystem. This service provides RESTful APIs and GraphQL endpoints for user management operations.

## Overview

The User Service is a production-ready Spring Boot application built with modern Java technologies. It handles user creation, authentication, preference management, and session caching using Redis.

## Features

- **User Management**: Create, retrieve, and manage user profiles
- **User Preferences**: Store and manage user-specific preferences
- **Authentication & Security**: Spring Security integration for secure API access
- **Session Caching**: Redis-based session management and caching
- **GraphQL Support**: Full GraphQL API alongside REST endpoints
- **Database Migrations**: Automated schema management using Flyway
- **Validation**: Input validation using Jakarta Bean Validation
- **Testing**: Comprehensive test coverage

## Technology Stack

### Core Framework
- **Java 17**: Latest long-term support Java version
- **Spring Boot 4.0.2**: Modern Spring Boot framework
- **Spring Data JPA**: Object-relational mapping and data access
- **Spring Security**: Authentication and authorization

### APIs & Messaging
- **Spring GraphQL**: GraphQL API support
- **Spring WebFlux**: Reactive web support
- **Spring WebMVC**: Traditional web support
- **Spring WebClient**: Declarative HTTP client

### Caching & Session Management
- **Spring Data Redis**: Redis integration for caching and session management
- **Redis**: In-memory data store for session caching

### Database
- **PostgreSQL**: Primary relational database
- **Flyway**: Database migration tool
- **Flyway PostgreSQL**: PostgreSQL dialect support

### Development Tools
- **Lombok**: Reduces boilerplate code
- **Spring Boot DevTools**: Development-time features like live reload
- **Docker Compose**: Container orchestration for local development

## Prerequisites

- Java 17 or higher
- Docker and Docker Compose
- Gradle (included via Gradle wrapper)

## Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/prateek-tomar2425/user-service.git
cd user-service
```

### 2. Start Dependencies with Docker Compose

```bash
docker-compose up -d
```

This will start:
- PostgreSQL database on port 5432
- Redis cache on port 6379

### 3. Build the Application

```bash
./gradlew build
```

### 4. Run the Application

```bash
./gradlew bootRun
```

The service will start on `http://localhost:8081`

## Configuration

### Application Properties

Configuration is managed through `src/main/resources/application.yaml`:

```yaml
spring:
  application:
    name: user-service
  
  datasource:
    url: jdbc:postgresql://localhost:5432/user_db
    username: postgres
    password: <password>
  
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: true
  
  data:
    redis:
      host: localhost
      port: 6379

server:
  port: 8081
```

### Key Configuration Parameters

| Property | Description | Default |
|----------|-------------|---------|
| `spring.datasource.url` | PostgreSQL connection URL | `jdbc:postgresql://localhost:5432/user_db` |
| `spring.data.redis.host` | Redis host | `localhost` |
| `spring.data.redis.port` | Redis port | `6379` |
| `server.port` | Server port | `8081` |

## API Endpoints

### REST API

#### Create User
```
POST /users
Content-Type: application/json

{
  "email": "user@example.com",
  "name": "John Doe",
  ...
}
```

#### Get User
```
GET /users/{id}
```

#### Update User Preferences
```
POST /users/{id}/preferences
Content-Type: application/json

{
  "preferences": {...}
}
```

#### Get User Preferences
```
GET /users/{id}/preferences
```

### GraphQL API

GraphQL endpoint: `POST /graphql`

Query and mutation definitions are located in `src/main/resources/graphql/`

## Project Structure

```
src/
├── main/
│   ├── java/com/travel/user/
│   │   ├── UserServiceApplication.java    # Main application class
│   │   ├── cache/                         # Redis caching services
│   │   │   └── SessionCacheService.java
│   │   ├── config/                        # Configuration classes
│   │   │   ├── RedisConfig.java
│   │   │   └── SecurityConfig.java
│   │   ├── controller/                    # REST API controllers
│   │   │   └── UserController.java
│   │   ├── service/                       # Business logic layer
│   │   ├── repository/                    # Data access layer
│   │   ├── model/                         # JPA entities
│   │   ├── dto/                           # Data transfer objects
│   │   ├── exception/                     # Custom exceptions
│   │   └── mapper/                        # Entity-DTO mappers
│   └── resources/
│       ├── application.yaml               # Application configuration
│       ├── db/migration/                  # Flyway SQL migrations
│       │   ├── V1__init.sql
│       │   └── V2__user_preferences.sql
│       └── graphql/                       # GraphQL schema definitions
└── test/
    └── java/com/example/wandersphere/    # Unit and integration tests
```

## Database Migrations

Flyway manages all database schema migrations. Migration files are located in `src/main/resources/db/migration/`:

- **V1__init.sql**: Initial schema setup
- **V2__user_preferences.sql**: User preferences table

Migrations run automatically on application startup.

## Building & Running

### Build JAR
```bash
./gradlew clean build
```

### Run with Gradle
```bash
./gradlew bootRun
```

### Run JAR Directly
```bash
java -jar build/libs/user-service-0.0.1-SNAPSHOT.jar
```

### Run Tests
```bash
./gradlew test
```

## Development

### IDE Setup

This project uses Lombok to reduce boilerplate. Make sure to enable annotation processing in your IDE:

**IntelliJ IDEA**:
- File → Settings → Build, Execution, Deployment → Compiler → Annotation Processors
- Enable "Obtain processors from project classpath"

### Code Style

The project follows Spring Boot conventions and Google Java Style Guide patterns.

### Hot Reload

During development, Spring Boot DevTools enables automatic restart when files change. Simply save your files and the application will reload.

## Troubleshooting

### Connection to PostgreSQL fails
- Ensure PostgreSQL is running: `docker-compose up -d postgres`
- Check credentials in `application.yaml`
- Verify connection string: `jdbc:postgresql://localhost:5432/user_db`

### Redis connection issues
- Ensure Redis is running: `docker-compose up -d redis`
- Check Redis port: `6379`
- Verify connection settings in `application.yaml`

### Tests fail
- Ensure Docker containers are running
- Clear Gradle cache: `./gradlew clean`
- Run tests: `./gradlew test`

## Contributing

1. Create a feature branch: `git checkout -b feature/your-feature`
2. Commit changes: `git commit -am 'Add feature'`
3. Push to branch: `git push origin feature/your-feature`
4. Submit a pull request

## License


## Support

For issues or questions, please open an issue in the repository or contact the development team.
