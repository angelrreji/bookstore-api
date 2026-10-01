# Book Store

A Spring Boot REST API for managing books, with user registration and JWT-based authentication. Data is stored in MongoDB.

## Tech stack

- Java 21, Spring Boot 4.0.2 (Web, Security, Data MongoDB)
- JWT via [jjwt](https://github.com/jwtk/jjwt) 0.12.6
- MongoDB (started automatically through Docker Compose)
- Lombok, Maven wrapper

## Project structure

```
src/main/java/com/myproject/book_store
├── config/       SecurityConfig, JwtAuthenticationFilter
├── controller/   BookController, UserInfoController
├── dto/          BookDto, UserInfoDto
├── entity/       Book, UserInfo (MongoDB documents)
├── exception/    Custom exceptions + GlobalExceptionHandler
├── mapper/       Entity <-> DTO mappers, UserDetails adapter
├── repository/   BookRepository, UserInfoRepository
└── service/      Service interfaces and impl/ implementations (incl. JWT)
```

## Getting started

### Prerequisites
- JDK 21
- Docker (for MongoDB)

### Configuration

| Property / env var | Default | Description |
|---|---|---|
| `JWT_SECRET` (`jwt.secret`) | *(empty)* | Base64-encoded HMAC key, at least 32 bytes. If unset, a random key is generated at startup and tokens stop working after a restart. |
| `jwt.expiration-ms` | `1800000` | Token lifetime (30 minutes). |

Generate a secret, for example: `openssl rand -base64 32`

### Run

```bash
# Windows: use mvnw.cmd
export JWT_SECRET=$(openssl rand -base64 32)
./mvnw spring-boot:run
```

`spring-boot-docker-compose` starts the MongoDB container from [compose.yaml](compose.yaml) (host port 27020) automatically.

### Test

```bash
./mvnw test
```

Tests need MongoDB running (the Docker Compose service).

## API

Authentication: send `Authorization: Bearer <token>` (HTTP Basic also works).

| Method | Path | Access | Description |
|---|---|---|---|
| GET | `/book-store/welcome` | public | Welcome message |
| POST | `/user-info/register` | public | Register a user (always gets `ROLE_USER`) |
| POST | `/user-info/login` | public | Returns a JWT |
| GET | `/book-store/{bookId}` | USER, ADMIN | Get a book |
| GET | `/book-store` | ADMIN | List all books |
| POST | `/book-store` | ADMIN | Create a book (201) |
| PUT | `/book-store` | ADMIN | Update a book's name (requires `bookId`) |
| DELETE | `/book-store/{bookId}` | ADMIN | Delete a book |

Errors: `400` invalid input, `401` bad credentials or missing/invalid token, `403` insufficient role, `404` unknown book, `409` username already exists.

### Example

```bash
curl -X POST localhost:8080/user-info/register -H 'Content-Type: application/json' \
  -d '{"userName":"alice","password":"secret"}'

TOKEN=$(curl -s -X POST localhost:8080/user-info/login -H 'Content-Type: application/json' \
  -d '{"userName":"alice","password":"secret"}')

curl localhost:8080/book-store/<bookId> -H "Authorization: Bearer $TOKEN"
```

## Admin users

Registration never grants `ROLE_ADMIN`. To create an admin, insert or update a document in the `users` collection with `roles: "ROLE_ADMIN"` (the password must be a BCrypt hash).

## Security notes

- `compose.yaml` contains a throwaway MongoDB root user/password for **local development only**. Change them and use environment variables or a secret manager for any shared or deployed environment.
- Never commit `JWT_SECRET`, `.env` files or real credentials (they are git-ignored).
