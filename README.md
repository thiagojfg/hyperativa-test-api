# Hyperativa API
This project is a Spring Boot application for exposing REST APIs, using Gradle for build and dependency management, Docker for containerization, and JWT for authentication. Below are the instructions to build, run, test, and log in to the application.
## Prerequisites
Ensure the following tools are installed on your system:
- **Java 21**
- **Gradle 8.x**
- **Docker and Docker Compose**

## How to Run the Application
### 1. Run Using Gradle
To run the application locally using Gradle:
1. Open a terminal and navigate to the project's root directory.
2. Build the application:
``` bash
   ./gradlew build
```
_This command compiles the code, runs tests, and packages the application into a JAR file._
1. Run the packaged application:
``` bash
   ./gradlew bootRun
```
The application will start on port `8080` by default, and you'll be able to access the API at `http://localhost:8080`. 
For this step a MySQL server must be running according to the properties defined at src/main/resources/application.properties.
### 2. Run Using Docker
To run the application in a containerized environment with Docker:
1. Build the Docker image:
``` bash
   docker build -t hyperativa-api .
```
1. Run the application container:
``` bash
   docker run -p 8080:8080 hyperativa-api
```
This will start the application on port `8080`. 
For this step a MySQL server must be running according to the properties defined at src/main/resources/application.properties.
### 3. Run Using Docker Compose
To run the application along with its MySQL database configured in `docker-compose.yml`:
1. Start both MySQL and the Spring Boot app using `docker-compose`:
``` bash
   docker-compose up --build
```
1. Confirm the services:
    - MySQL will be available at `localhost:3306`.
    - The Spring Boot app will be available at `http://localhost:8080`.
When the application runs at the first time, it will create the database schema and insert a test user:
    - username: admin
    - password: admin123

## How to Test the Application
### 1. Using Unit and Integration Tests
Pre-configured tests are available in the project using JUnit and Testcontainers:
- Run all tests using Gradle:
``` bash
  ./gradlew test
```
_This command will test the application using the pre-defined test configuration._
PS.: For integration test the spring boot will run using a h2 in memory database
### 2. API Testing with Swagger UI
The Swagger UI is enabled and can be accessed after running the application:
1. Access the Swagger UI at: `http://localhost:8080/swagger-ui`.
2. Use the listed API endpoints to interact with the application.

### 3. Manual API Testing
Alternatively, you may test the API using tools like [Postman](https://www.postman.com/) or [curl](https://curl.se/).
## How to Log In and Obtain JWT Token
### Login API Endpoint
The login API is accessible at:
``` 
POST /api/auth/login
```
### Request Body
Send a JSON payload with the **username** and **password** as required for authentication. Example:
``` json
{
  "username": "your_username",
  "password": "your_password"
}
```
### Response
Upon successful authentication, the application will return a **JWT token** in the following response format:
``` json
{
  "token": "your-jwt-token"
}
```
### Example with `curl`
You can log in and retrieve a JWT token using the following `curl` command:
``` bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "your_username", "password": "your_password"}'
```
Upon success, the command will return:
``` json
{
  "token": "your-jwt-token"
}
```
## Configuration
### Environment Variables
The application relies on several configurations defined in `application.properties`. Key configurations include:
- **MySQL Database:**
    - URL: `spring.datasource.url`
    - Username: `spring.datasource.username`
    - Password: `spring.datasource.password`

- **JWT Secret:**
    - `jwt.secret` – Defines the signing key for token generation.
    - `jwt.tokenDuration` – Controls token validity duration.

You can override these settings using environment variables in the `docker-compose.yml` file or while running the application locally.
## Additional Notes
1. **Database Access:**
    - The application uses MySQL as the database and requires an instance of MySQL running on port `3306`.
    - Docker Compose automatically sets up the database.

2. **Swagger UI:**
    - You can explore the API and its documentation using Swagger available at `http://localhost:8080/swagger-ui`.
