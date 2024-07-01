# LibraryApp

LibraryApp is a Spring Boot-based application designed for managing a library system, including functionalities
for book and customer management, as well as book borrowing and returning processes.

## REST API Documentation
```http request
GET http://localhost:8080/swagger-ui/index.html
```
<img width="1340" alt="Screenshot 2024-07-01 at 20 43 21" src="https://github.com/dawidkol/LibraryApp/assets/15035709/a5af6ca6-8c3a-40cd-a7a8-6dd60b891fa9">

## Features

- **Book Management**
    - Add, update, delete, and retrieve books.
    - Validation and unique ISBN indexing.
    - Pagination support for listing books.

- **Customer Management**
    - Add, update, delete, and retrieve customers.
    - Validation and pagination support for listing customers.

- **Book Loan Management**
    - Borrow and return books.
    - Track user's book loan history.

- **Error Handling**
    - Comprehensive error handling for validation and database exceptions.

## Technologies

- **Spring Boot**
    - Spring Boot Starter Data MongoDB
    - Spring Boot Starter Validation
    - Spring Boot Starter Web
    - Spring Boot Docker Compose (runtime)
    - Spring Boot Starter Test
    - Spring Boot Testcontainers (test scope)

- **Database**
    - MongoDB

- **Testing**
    - JUnit Jupiter
    - Testcontainers for MongoDB

- **Utilities**
    - Lombok
    - JSON Patch for partial updates
    - SpringDoc OpenAPI for API documentation

## Requirements

- Java 22
- Maven
- Docker (for Testcontainers and MongoDB integration)

## Getting Started

### Prerequisites

Ensure you have the following installed on your local machine:

- Java 22
- Maven
- Docker

### Remember to add your MongoDB username and password as environment variables in application.yml

### Installation

1. Clone the repository:
```bash
git clone https://github.com/dawidkol/LibraryApp.git
cd LibraryApp
```

2. Build the project
```bash
mvc clean install
```

3. Run the application
```bash
mvn spring-boot:run
```
