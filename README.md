# Task Manager

A full-stack task management application built with Spring Boot and Next.js.

## Tech Stack

**Backend**
- Java 21 + Spring Boot 3.2.5
- Spring Security + JWT authentication
- Spring Data JPA + Hibernate
- Flyway (database migrations)
- PostgreSQL 16

**Frontend**
- Next.js 14 (App Router)
- TypeScript
- Tailwind CSS

**Infrastructure**
- Docker + Docker Compose

---

## Architecture

### Backend — Layered Architecture

```
src/main/java/com/codemized/taskmanager/
├── controller/       # REST endpoints (HTTP layer)
├── service/          # Business logic interfaces
│   └── impl/         # Business logic implementations
├── repository/       # Data access (Spring Data JPA)
├── entity/           # JPA entities (User, Project, Task, Comment)
├── dto/
│   ├── request/      # Incoming request bodies
│   └── response/     # Outgoing response bodies
├── security/         # JWT filter, UserDetailsService, JwtService
├── config/           # SecurityConfig (CORS, CSRF, auth provider)
└── exception/        # GlobalExceptionHandler, custom exceptions
```

### Frontend — Next.js App Router

```
frontend/app/
├── login/            # Login page
├── register/         # Registration page
└── dashboard/
    ├── page.tsx                          # Project list
    └── projects/
        └── [id]/
            ├── page.tsx                  # Project detail + task list
            └── tasks/
                └── [taskId]/
                    └── page.tsx          # Task detail + comments
frontend/lib/
├── api.ts            # Axios instance with JWT interceptor
├── auth-context.tsx  # Auth state (login, logout, token validation)
└── types.ts          # TypeScript interfaces
```

### Data Model

```
User (1) ──── (N) Project
User (1) ──── (N) Task (as assignee)
Project (1) ── (N) Task
Task (1) ───── (N) Comment
User (1) ───── (N) Comment (as author)
```

### Authentication Flow

1. User registers or logs in → backend returns a JWT
2. Frontend stores the JWT in `localStorage`
3. Every API request includes `Authorization: Bearer <token>` via Axios interceptor
4. On app load, the token is validated against `GET /api/users/me` — if it fails, the user is redirected to login

---

## Running the Application

### Requirements

- Docker Desktop

### Start

```bash
git clone <your-repo-url>
cd taskmanager
docker compose up
```

That's it. Docker Compose will:
1. Start PostgreSQL
2. Build and start the Spring Boot backend (port 8080)
3. Build and start the Next.js frontend (port 3000)
4. Run Flyway migrations automatically

Open [http://localhost:3000](http://localhost:3000) in your browser.

### Stop

```bash
docker compose down
```

To also remove the database volume (reset all data):

```bash
docker compose down -v
```

---

## API Endpoints

### Auth
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/auth/register | Register new user |
| POST | /api/auth/login | Login, returns JWT |

### Users
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /api/users | List all users |
| GET | /api/users/me | Get authenticated user |

### Projects
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /api/projects | List user's projects |
| POST | /api/projects | Create project |
| GET | /api/projects/{id} | Get project by ID |
| PUT | /api/projects/{id} | Update project |
| DELETE | /api/projects/{id} | Delete project |

### Tasks
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /api/projects/{id}/tasks | List tasks by project |
| POST | /api/projects/{id}/tasks | Create task |
| GET | /api/tasks/{id} | Get task by ID |
| PUT | /api/tasks/{id} | Update task |
| DELETE | /api/tasks/{id} | Delete task |
| PATCH | /api/tasks/{id}/assign/{userId} | Assign task to user |

### Comments
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /api/tasks/{taskId}/comments | List comments by task |
| POST | /api/tasks/{taskId}/comments | Create comment |
| PUT | /api/tasks/{taskId}/comments/{id} | Update comment |
| DELETE | /api/tasks/{taskId}/comments/{id} | Delete comment |

---

## Environment Variables

The application uses default values suitable for local development. These are configured in `docker-compose.yml` and `application.yml`.

| Variable | Default | Description |
|----------|---------|-------------|
| DB_HOST | db | PostgreSQL host |
| DB_PORT | 5432 | PostgreSQL port |
| DB_NAME | taskmanager | Database name |
| DB_USER | postgres | Database user |
| DB_PASSWORD | postgres | Database password |
| JWT_SECRET | (default hex key) | JWT signing secret |
| NEXT_PUBLIC_API_URL | http://localhost:8080/api | Backend URL for frontend |
