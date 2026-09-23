
# 🚀 DevTrack

### A Secure, Containerized Learning Goal Management API

DevTrack is a Spring Boot backend application that enables users to create, manage, and track their learning goals through secure REST APIs.

Designed with a layered architecture, it brings together authentication, authorization, data validation, structured exception handling, and progress-based business logic in a practical backend project.

## ✨ What Makes DevTrack Interesting?

- **Secure by design:** JWT-based authentication and role-based authorization.
- **User-specific data:** Users can manage their own learning goals.
- **Practical business logic:** Track progress and manage goal status.
- **Flexible data retrieval:** Filter and sort goals through API queries.
- **Containerized environment:** Run the application and MySQL using Docker Compose.
- **Tested backend:** Automated tests covering application behavior and security.


<p align="center">
  <img src="https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=openjdk" alt="Java 21"/>
  <img src="https://img.shields.io/badge/Spring%20Boot-Backend-6DB33F?style=for-the-badge&logo=springboot&logoColor=white" alt="Spring Boot"/>
  <img src="https://img.shields.io/badge/MySQL-Database-4479A1?style=for-the-badge&logo=mysql&logoColor=white" alt="MySQL"/>
  <img src="https://img.shields.io/badge/Spring%20Security-JWT-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white" alt="Spring Security and JWT"/>
  <img src="https://img.shields.io/badge/Docker-Containerized-2496ED?style=for-the-badge&logo=docker&logoColor=white" alt="Docker"/>
</p>


---

## 📌 Project Overview

Learning consistently requires more than setting goals — it requires a way to organize them, track progress, and stay accountable.

**DevTrack** provides a RESTful backend for managing learning goals, with secure access to user-specific data and APIs for tracking progress.

### 🔄 How It Works

1. **Register:** A user creates an account.
2. **Authenticate:** The user logs in and receives a JWT.
3. **Manage Goals:** Authenticated users create, view, update, and delete their learning goals.
4. **Track Progress:** Goals include progress and status information.
5. **Organize Goals:** Users can filter and sort goals through API query parameters.

The application uses a layered backend structure to separate API handling, business logic, data access, and persistence.


---

## 🏗️ Architecture

DevTrack follows a layered architecture that separates HTTP handling, business logic, and database operations.

```text
              Client
                |
                | HTTP Request + JWT
                v
       Spring Security Layer
       (Authentication & Authorization)
                |
                v
          Controller Layer
       (REST API Endpoints)
                |
                v
           Service Layer
        (Business Logic)
                |
                v
          Repository Layer
        (Data Access via JPA)
                |
                v
             MySQL
          (Persistence)
```

### Request Flow

1. A client sends an HTTP request, including a JWT for protected endpoints.
2. Spring Security validates the token and checks access permissions.
3. The controller receives the request and delegates work to the service layer.
4. The service layer applies business rules and calls the repository.
5. The repository interacts with MySQL through Spring Data JPA.
6. The application returns an HTTP response to the client.



---

## 🛠️ Tech Stack & Tools

| Technology | Purpose |
|---|---|
| **Java 21** | Core programming language |
| **Spring Boot** | Building the REST API and application structure |
| **Spring Security** | Authentication and authorization |
| **JWT** | Token-based authentication for protected endpoints |
| **Spring Data JPA** | Database access and persistence |
| **Hibernate** | ORM implementation for mapping Java objects to database tables |
| **MySQL** | Relational database |
| **Maven** | Dependency management and build automation |
| **Docker** | Containerizing the application |
| **Docker Compose** | Running the application and database together |
| **Postman** | API testing and endpoint verification |
| **JUnit & Mockito** | Automated testing |
| **Swagger / OpenAPI** | API documentation and interactive endpoint exploration |
| **Git & GitHub** | Version control and source-code hosting |


---

## ✨ Core Features

### 🔐 Authentication & Authorization
- User registration with password hashing using BCrypt.
- Login endpoint that issues a JWT.
- JWT-based authentication for protected API endpoints.
- Role-based access control for `USER` and `ADMIN` roles.

### 👤 User-Specific Goal Management
- Create, retrieve, update, and delete learning goals.
- Associate each learning goal with its owner.
- Restrict users from accessing or modifying goals belonging to other users.

### 📈 Progress & Goal Status
- Track learning progress using a progress field.
- Manage goal status using an enum.
- Apply progress-based business logic.

### 🔎 Filtering & Sorting
- Filter learning goals using API query parameters.
- Sort goals by supported fields and direction.

### 🛡️ Validation & Error Handling
- Validate incoming request data.
- Handle application errors through a centralized exception handler.
- Return meaningful HTTP status codes for common error scenarios.

### 🧪 Testing & API Documentation
- Automated tests for application behavior and security.
- Explore and test endpoints through Swagger / OpenAPI.
- Verify API behavior using Postman.


---

## 🔌 API Endpoints

DevTrack exposes REST endpoints for user registration, authentication, learning goal management, and administrator operations.

### 🔐 Authentication & Users

| Method | Endpoint | Access | Description |
|---|---|---|---|
| `POST` | `/users` | Public | Register a new user |
| `POST` | `/login` | Public | Authenticate and receive a JWT |
| `GET` | `/users` | Authenticated | Retrieve registered users |
| `GET` | `/users/{id}` | Authenticated | Retrieve a user by ID |
| `GET` | `/admin/users` | Admin | Retrieve all users with administrator privileges |

### 📚 Learning Goals

| Method | Endpoint | Access | Description |
|---|---|---|---|
| `POST` | `/goals` | USER | Create a learning goal |
| `GET` | `/goals` | USER | Retrieve goals with filtering, sorting, searching, and pagination |
| `GET` | `/goals/{id}` | USER | Retrieve a specific goal |
| `PUT` | `/goals/{id}` | USER | Update a learning goal |
| `DELETE` | `/goals/{id}` | USER | Delete a learning goal |

### 🔎 Goal Listing Query Parameters

The `GET /goals` endpoint supports the following optional query parameters:

| Parameter | Default | Description |
|---|---|---|
| `status` | — | Filter goals by status |
| `sortBy` | `name` | Field used for sorting |
| `direction` | `asc` | Sort direction |
| `PageNumber` | `0` | Page number |
| `PageSize` | `5` | Number of goals per page |
| `search` | — | Search goals |
| `minProgress` | — | Minimum progress filter |
| `maxProgress` | — | Maximum progress filter |
| `startDateFrom` | — | Start-date lower bound |
| `startDateTo` | — | Start-date upper bound |

Example:

```http
GET /goals?sortBy=progress&direction=desc
```

This requests goals sorted by progress in descending order, using the endpoint's default pagination values.


---

## 🔐 Authentication Flow

DevTrack uses Spring Security and JSON Web Tokens (JWT) to authenticate users and protect secured API endpoints.

### 🔄 How Authentication Works

```text
  1. User registers
         |
         v
  2. User sends login credentials
         |
         v
  3. AuthenticationManager
     validates credentials
         |
         v
  4. JwtService generates a JWT
         |
         v
  5. Client receives the token
         |
         v
  6. Client sends token with
     protected API requests
         |
         v
  7. JWT filter validates token
         |
         v
  8. Spring Security checks access
         |
         v
  9. Request reaches the controller
```

### 🪪 Using the JWT

After a successful login, the client receives a JWT.

Include it in the `Authorization` header when calling protected endpoints:

```http
Authorization: Bearer <your-jwt-token>
```

### 🛡️ Access Control

- **Public endpoints:** User registration and login.
- **USER role:** Access to protected user-level functionality, including learning goal operations.
- **ADMIN role:** Access to administrator-only functionality, such as retrieving all users through `/admin/users`.

### 🚦 Security Responses

| HTTP Status | Meaning |
|---|---|
| `401 Unauthorized` | Authentication is missing or invalid |
| `403 Forbidden` | The authenticated user does not have the required permission |

DevTrack uses BCrypt to hash passwords before storing them, rather than storing the original passwords.


---

## 🧪 Testing

DevTrack includes automated tests to verify application behavior, business logic, and security-related functionality.

### Testing Tools

- **JUnit 5** — Writing and running automated tests.
- **Mockito** — Isolating dependencies and testing service-layer behavior.
- **Spring Boot Test** — Testing Spring application components.
- **MockMvc** — Testing HTTP endpoints and responses.

### Areas Covered

- User registration and authentication.
- Learning goal operations and business logic.
- Request validation and exception handling.
- JWT authentication and role-based authorization.
- Access restrictions for user-specific resources.

### Run the Tests

From the project root, run:

```bash
mvn clean verify
```

This runs the Maven build and verification lifecycle, including tests configured for the project.


---

## 🐳 Docker & Setup

DevTrack is containerized with Docker and Docker Compose, allowing the Spring Boot application and MySQL database to run together.

### Prerequisites

- Docker Desktop or Docker Engine with Docker Compose
- Git

### 1. Clone the Repository

```bash
git clone https://github.com/rajujindam/DevTrack.git
cd DevTrack
```

### 2. Configure Environment Variables

Create a `.env` file in the project root and configure the environment variables required by your Compose setup.

Use the variable names from your project's `docker-compose.yml` and application configuration. Do not commit real passwords or JWT secrets to GitHub.

### 3. Start the Application

```bash
docker compose up -d --build
```

This builds the application image if needed and starts the application and database services in the background.

### 4. Check the Running Containers

```bash
docker compose ps
```

### 5. View Application Logs

```bash
docker compose logs -f devtrack
```

### 6. Stop the Application

```bash
docker compose down
```

The Compose configuration uses a named volume for MySQL data, so stopping the services does not itself remove that volume.

To remove the containers and the Compose-managed network while retaining the named volume:

```bash
docker compose down
```


---

## 🚀 API Usage Examples

Once DevTrack is running, you can interact with its REST APIs using Postman or another HTTP client.

### 1. Register a User

**Endpoint:** `POST /users`

Create a user account by sending a JSON request body matching `UserRequest`.

### 2. Authenticate

**Endpoint:** `POST /login`

Send your login credentials to receive a JWT.

### 3. Access Protected Endpoints

Include the JWT in the `Authorization` header:

```http
Authorization: Bearer <your-jwt-token>
```

### 4. Retrieve Learning Goals

**Endpoint:** `GET /goals`

Retrieve your learning goals. The endpoint supports filtering, searching, sorting, pagination, progress ranges, and start-date ranges.

Example:

```http
GET /goals?sortBy=progress&direction=desc
```

### 5. Retrieve a Specific Goal

**Endpoint:** `GET /goals/{id}`

Replace `{id}` with the ID of the learning goal you want to retrieve.

### 6. Update or Delete a Goal

Use the following endpoints to manage an existing learning goal:

- `PUT /goals/{id}` — Update a goal.
- `DELETE /goals/{id}` — Delete a goal.

Both operations require authentication and enforce goal ownership.

## 📁 Project Structure

```text
DevTrack/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/raju/DevTrack/
│   │   │       ├── config/
│   │   │       │   ├── CustomAccessDeniedHandler.java
│   │   │       │   ├── CustomAuthenticationEntryPoint.java
│   │   │       │   ├── CustomUserDetailsService.java
│   │   │       │   ├── OpenAPIConfiguration.java
│   │   │       │   └── SecurityConfig.java
│   │   │       ├── controller/
│   │   │       │   ├── AdminController.java
│   │   │       │   ├── AuthController.java
│   │   │       │   ├── LearningGoalController.java
│   │   │       │   └── UserController.java
│   │   │       ├── dto/
│   │   │       │   ├── ErrorResponse.java
│   │   │       │   ├── LearningGoalRequest.java
│   │   │       │   ├── LearningGoalResponse.java
│   │   │       │   ├── LoginRequest.java
│   │   │       │   ├── PaginatedGoalResponse.java
│   │   │       │   ├── UserRequest.java
│   │   │       │   └── UserResponse.java
│   │   │       ├── exceptions/
│   │   │       │   ├── GlobalExceptionHandler.java
│   │   │       │   ├── ResourceNotFoundException.java
│   │   │       │   └── UserNotMatched.java
│   │   │       ├── filter/
│   │   │       │   └── JwtAuthenticationFilter.java
│   │   │       ├── model/
│   │   │       │   ├── GoalStatus.java
│   │   │       │   ├── LearningGoal.java
│   │   │       │   ├── Role.java
│   │   │       │   └── User.java
│   │   │       ├── repository/
│   │   │       │   ├── LearningGoalRepository.java
│   │   │       │   └── UserRepository.java
│   │   │       ├── service/
│   │   │       │   ├── JwtService.java
│   │   │       │   ├── LearningGoalService.java
│   │   │       │   └── UserService.java
│   │   │       ├── specification/
│   │   │       │   └── LearningGoalSpecification.java
│   │   │       └── DevTrackApplication.java
│   │   └── resources/
│   └── test/
├── .dockerignore
├── .env
├── .gitattributes
├── .gitignore
├── compose.yaml
├── Dockerfile
├── HELP.md
└── mvnw
```

### 📦 Package Responsibilities

| Package | Responsibility |
|---|---|
| `config` | Security, authentication handlers, user details, and OpenAPI configuration |
| `controller` | REST API endpoints for users, authentication, learning goals, and admin operations |
| `dto` | Request and response objects for API communication |
| `exceptions` | Global exception handling and custom exceptions |
| `filter` | JWT authentication filter |
| `model` | User and learning-goal entities, roles, and goal status |
| `repository` | Spring Data JPA database access |
| `service` | Authentication, JWT, user, and learning-goal logic |
| `specification` | Dynamic learning-goal filtering |

## ⚙️ Configuration & Environment Variables

DevTrack uses environment variables to configure database connectivity and JWT authentication.

### Environment Configuration

Create a `.env` file in the project root and configure the required values based on your application configuration.

```env
# MySQL Database
MYSQL_DATABASE=your_database_name
MYSQL_USER=your_database_user
MYSQL_PASSWORD=your_database_password
MYSQL_ROOT_PASSWORD=your_root_password

# JWT Configuration
JWT_SECRET=your_base64_encoded_secret
JWT_EXPIRATION=your_token_expiration
```

> **Note:** These are example variable names. Use the exact names and values expected by your `compose.yaml` and Spring Boot configuration.

### Security Best Practices

- Never commit real database passwords or JWT secrets to GitHub.
- Keep your `.env` file private.
- Use strong, randomly generated secrets for JWT signing.
- Share a `.env.example` containing placeholder values if you want to help others configure the project.

## 📖 API Documentation

DevTrack uses **Swagger UI (OpenAPI)** to make its REST API easier to explore and test.

### Access Swagger UI

1. Start the application using Docker Compose.
2. Open the following URL in your browser:

   ```text
   http://localhost:8080/swagger-ui/index.html
   ```

3. Explore the available endpoints and their request/response details.
4. Use the **Authorize** button to provide your JWT bearer token when testing protected endpoints.

### Authentication

For protected endpoints:

1. Register a user and log in.
2. Copy the JWT returned by the login endpoint.
3. Click **Authorize** in Swagger UI.
4. Enter your token using the format required by the Swagger security configuration.

## 🚧 Future Improvements

Potential enhancements for future versions of DevTrack:

- [ ] Refresh token support for improved session management.
- [ ] More comprehensive integration and security tests.
- [ ] CI/CD pipeline for automated builds and testing.
- [ ] Cloud deployment and production environment configuration.
- [ ] Enhanced API documentation and usage examples.

> These are planned enhancements and are not part of the current implementation.

## 👨‍💻 Author

**Raju Jindam**

Java Backend Developer | Spring Boot | REST APIs

- GitHub: [@rajujindam](https://github.com/rajujindam)
- LinkedIn: [Connect with me](https://www.linkedin.com/in/rajujindam)