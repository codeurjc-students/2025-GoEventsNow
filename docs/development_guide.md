# Index

 - [Introduction](#introduction)
 - [Technologies](#technologies)
 - [Tools](#tools)
 - [Architecture](#architecture)
 - [Quality Control](#quality-control)
 - [Docker Deployment](#docker-deployment)
 - [Development Process](#development-process)
 - [Code Edit & Execution](#code-edit--execution)

## Introduction

> **GoEventsNow** is a full-stack web application designed following a SPA (Single Page Application) architecture. In this architecture, the frontend is responsible for rendering the user interface dynamically as the user interacts with it, obtaining the data when the backend exposes with a REST API that handles the application logic and data persistence.

> In this phase, the project includes a stable minimum viable product (MVP) with core functionalities such as user registration, log in/logout, event and participant browsing and management for administrators, detailed event and participant pages, images handling, and ticket purchasing.

The application is divided into three main layers:

- **Client (Frontend)**: Implemented using Angular, it manages user interaction, navigation, and dynamic content rendering. It communicates with the backend via HTTP requests.
- **Server (Backend)**: Developed using Spring Boot (Java), the backend provides a REST API that manages business logic and exposes the data obtained from the Database.
- **Database (Persistence)**: A MySQL relational database is used to ensure storage and persistence of application data.

| Concept | Description | 
| :--- | :--- |
| **Type** | Web Application SPA (Single Page Application) + REST API. |
| **Technologies** | **Backend**: Java 21, Spring Boot, Spring Security + JWT, Spring Data JPA, Bean Validation, MapStruct, OpenAPI. **Frontend**: Angular 21, TypeScript, Bootstrap, ng-bootstrap, RxJS. **Database**: MySQL |
| **Tools** | VS Code, Maven, Git/GitHub, Postman, Docker/Docker Compose, DockerHub, ORAS, SonarCloud, GitHub Actions |
| **Quality Control** | **Tests**: Unit, Integration and System (JUnit, REST Assured, Selenium) for backend and frontend (TestBed / Vitest). **Coverage**: JaCoCo and Vitest. **Analysis**: Static code with Sonar |
| **Deployment** | Docker image published in DockerHub. Execution with Docker Compose using MySQL container and the application container. Docker Compose is also published as an OCI artifact. |
| **Development process** | Iterative and Incremental methodology using GitFlow and GitHub Actions for CI/CD. |

## Technologies

> Main technologies used for the execution of the project, with a description about the purpose in the project and its official URLs.

- **Java 21**: Programming language used for the backend logic and REST API implementation.  
    Official URL: [https://www.oracle.com/java/](https://www.oracle.com/java/)

- **Spring Boot**: Java framework used to create the backend. It manages the application logic, handles the main functionalities and entities, and provides data to the Angular frontend through the REST API.  
    Official URL: [https://spring.io/projects/spring-boot](https://spring.io/projects/spring-boot)

- **Spring Security + JWT**: Used to secure the REST API and manage user authentication and authorization.  
    Official URLs: [https://spring.io/projects/spring-security](https://spring.io/projects/spring-security), [https://jwt.io/](https://jwt.io/)

- **Spring Data JPA**: Used to implement the persistence layer and database access through repositories and entity mappings.  
    Official URL: [https://spring.io/projects/spring-data-jpa](https://spring.io/projects/spring-data-jpa)

- **Bean Validation**: Used to validate DTOs and incoming data before processing them in the backend.  
    Official URL: [https://beanvalidation.org/](https://beanvalidation.org/)

- **MapStruct**: Used to map entities and DTOs in the backend.  
    Official URL: [https://mapstruct.org/](https://mapstruct.org/)

- **Angular**: Frontend framework for building the Single Page Application (SPA). It manages client-side routing, dynamic content rendering and HTTP communication with the backend API.  
    Official URL: [https://angular.dev/](https://angular.dev/)

- **MySQL**: Relational database used for persistent storage of events, ensuring data integrity.  
    Official URL: [https://www.mysql.com/](https://www.mysql.com/)

- **TypeScript**: Programming language used in the frontend to add components, model and services. 
    Official URL: [https://www.typescriptlang.org/](https://www.typescriptlang.org/)

- **OpenAPI / Swagger UI**: Used to generate and visualize REST API documentation.  
    Official URL: [https://springdoc.org/](https://springdoc.org/),[https://swagger.io/tools/swagger-ui/](https://swagger.io/tools/swagger-ui/)
    
- **Bootstrap / ng-bootstrap**: CSS framework and Angular components for building Bootstrap components adapted for Angular.  
    Official URLs: [https://getbootstrap.com/](https://getbootstrap.com/), [https://ng-bootstrap.github.io/](https://ng-bootstrap.github.io/)

## Tools 

> Main tools and IDEs used during development.

- **Visual Studio Code**: Code editor used for both Backend/Frontend development.
    Official URL: [https://code.visualstudio.com/](https://code.visualstudio.com/)

- **Postman**: Tool for testing and documenting REST API endpoints.
    Official URL: [https://www.postman.com/](https://www.postman.com/)

- **Git**: Version control system used to track changes in source code, in addition to manage the tasks development and the implementation of GitFlow workflows.
    Official URL: [https://github.com/](https://github.com/)

- **GitHub Actions**: CI/CD platform used to automate build deployment, test and quality-control workflows for backend and frontend.
    Official URL: [https://github.com/features/actions](https://github.com/features/actions)

- **Maven**: Build automation tool used to manage the backend dependencies and lifecycle of the Java application.
    Official URL: [https://maven.apache.org/](https://maven.apache.org/)

- **Docker + Docker Compose**: Containerization tools used to package and execute the application with MySQL in reproducible environments.
    Official URLs: [https://www.docker.com/](https://www.docker.com/), [https://docs.docker.com/compose/](https://docs.docker.com/compose/)

- **DockerHub**: Container registry used to publish the GoEventsNow application image and the Docker Compose OCI artifact.  
    Official URL: [https://hub.docker.com/](https://hub.docker.com/)

- **ORAS**: OCI artifact client used to pull the published Docker Compose artifact from DockerHub.
    Official URL: [https://oras.land/](https://oras.land/)

- **SonarCloud**: Static code analysis platform used to monitor code quality and maintainability.
    Official URL: [https://sonarcloud.io/](https://sonarcloud.io/)

- **Figma**: Interface design tool used to create the screen mockups and navigation flows defined in the analysis phase.
    Official URL: [https://www.figma.com/](https://www.figma.com/)

- **Lucidchart**: Diagramming tool used to create the architecture diagrams for the backend and frontend.
    Official URL: [https://www.lucidchart.com/](https://www.lucidchart.com/)

## Architecture

### Deployment

> The deployment architecture is based on independent processes communicating through HTTP following the REST API architectural style:

The application is deployed locally as several independent processes:

1. Frontend (Port 4200): It handles the user interface and presentation logic. It communicates with the backend via HTTP communication and using JSON as the data interchange format.

2. Backend (Port 8443): The Spring Boot application runs on the server, and it acts as a REST API that handles the business logic and data persistence. It communicates with the Database to obtain data, and with the Frontend to listen the requests. 

3. MySQL (Port 3306): The MySQL database runs, ensuring data integrity and persistence. It allows the communication with the backend to send data, ensuring security.

### Domain Model

> The domain model of the application is based on the main entities, such as User, Event, Participant and Ticket, and their relationships. The User entity represents the users of the application, the Event entity represents the events available in the platform, the Participant entity represents the participants of the events, and the Ticket entity represents the tickets purchased by users for specific events.

[NEED TO INSERT DOMAIN MODEL DIAGRAM]

### REST API

> The communication between the frontend and backend is facilitated through a REST API. It follows RESTful principles and is secured with Spring Security for authentication and providing role-based access control.

#### Swagger / OpenAPI Documentation

> The API is fully documented using OpenAPI and Swagger UI, providing interactive documentation to understand and test the endpoints. The documentation includes details about each endpoint, such as request parameters or response formats.

- **Swagger UI**: Available at `https://localhost:8443/swagger-ui/index.html#/` when running the backend locally. Available at `https://localhost/swagger-ui/index.html#/` when running with Docker.
- **OpenAPI Specification (YAML)**: Available at `https://localhost:8443/v3/api-docs.yaml`, or available at `https://localhost/v3/api-docs.yaml` when running the application with Docker.
- **Published HTML Documentation**: [OpenAPI HTML Documentation](https://raw.githack.com/codeurjc-students/2025-GoEventsNow/main/backend/docs/api/api-docs.html) (GitHub-hosted)

#### API Base Configuration

- **Base URL with Docker**: `https://localhost/api/v1`
- **Base URL with local backend**: `https://localhost:8443/api/v1`
- **Authentication**: JWT-based authentication using secure cookies.
- **Response Format**: JSON

#### Authentication Endpoints

| Method | Route | Description | Authentication |
| :--- | :--- | :--- | :--- |
| POST | `/auth/login` | User login with credentials | None |
| POST | `/auth/register` | User registration | None |
| POST | `/auth/refresh` | Refresh authentication token using refresh token cookie | Refresh Token |
| POST | `/auth/logout` | User logout | None |

#### Events Endpoints

| Method | Route | Description | Authentication |
| :--- | :--- | :--- | :--- |
| GET | `/events/` | Get all events (paginated) | None |
| GET | `/events/?participantId={participantId}` | Get all events filtered by participant ID (paginated) | None |
| GET | `/events/{id}` | Get event details by ID | None |
| POST | `/events/` | Create new event | Admin only |
| PUT | `/events/{id}` | Update event | Admin only |
| DELETE | `/events/{id}` | Delete event | Admin only |
| POST | `/events/{id}/image` | Upload event image | Admin only |
| PUT | `/events/{id}/image` | Update event image | Admin only |
| DELETE | `/events/{id}/image` | Delete event image | Admin only |
| GET | `/events/{id}/image` | Get event image | None |

#### Participants Endpoints

| Method | Route | Description | Authentication |
| :--- | :--- | :--- | :--- |
| GET | `/participants/` | Get all participants (paginated) | None |
| GET | `/participants/{id}` | Get participant details by ID | None |
| POST | `/participants/` | Create new participant | Admin only |
| PUT | `/participants/{id}` | Update participant | Admin only |
| DELETE | `/participants/{id}` | Delete participant | Admin only |
| POST | `/participants/{id}/image` | Upload participant image | Admin only |
| PUT | `/participants/{id}/image` | Update participant image | Admin only |
| DELETE | `/participants/{id}/image` | Delete participant image | Admin only |
| GET | `/participants/{id}/image` | Get participant image | None |

#### Users Endpoints

| Method | Route | Description | Authentication |
| :--- | :--- | :--- | :--- |
| GET | `/users/me` | Get current authenticated user profile | User/Admin |
| GET | `/users/exists?username={username}` | Check if a username already exists | None |
| PUT | `/users/{id}` | Update user profile | User/Admin (own profile only) |
| GET | `/users/{id}/image` | Get user profile photo | User/Admin (own profile only) |
| POST | `/users/{id}/image` | Upload user profile photo | User/Admin (own profile only) |
| PUT | `/users/{id}/image` | Update user profile photo | User/Admin (own profile only) |
| DELETE | `/users/{id}/image` | Delete user profile photo | User/Admin (own profile only) |

#### Tickets Endpoints

| Method | Route | Description | Authentication |
| :--- | :--- | :--- | :--- |
| GET | `/tickets/` | Get user's tickets (paginated) | User/Admin |
| GET | `/tickets/{id}` | Get specific ticket details | User/Admin |
| POST | `/tickets/` | Purchase/create new ticket | User/Admin |

#### API Testing

- **Postman Collection**: Use the provided [GoEventsNow Postman Collection](GoEventsNow_Collection.postman_collection.json) to test all endpoints
- **Swagger UI**: Testing directly available from the browser.

### Server Architecture

> The backend architecture is based on a layered structure based on the principles of separation of responsibilities. The main layers, their responsibilities and the connections between them are:

1. **Controller Layer**: It contains the REST controllers to handle incoming HTTP requests, make the service calls, and return appropriate HTTP responses. It also manages authentication and authorization using Spring Security. It contains SpaController to serve the Angular frontend as static resources.
2. **Service Layer**: It contains the business logic of the application and the interaction with the persistence layer.
3. **Persistence/Repository Layer**: It manages the interaction with the MySQL database using Spring Data JPA repositories, ensuring data integrity.
4. **Model Layer**: It contains the entities that represent the application's domain objects.
5. **Security Layer**: It manages the security configuration, including JWT authentication and role-based access control.

[NEED TO INSERT BACKEND ARCHITECTURE DIAGRAM]

### Client Architecture

> The frontend architecture is based on Angular's component-based structure, following best practices for separation of responsibilities. The main layers and their responsibilities are:

1. **Components**: They are responsible for the presentation of the application and the features related to user interaction. They manage the views and templates, and they call the services to obtain data from the backend and render it in the UI. 
2. **Services**: They manage the communication with the backend REST API.

[NEED TO INSERT FRONTEND ARCHITECTURE DIAGRAM]

## Quality Control

> This section details the quality controls and automated tests implemented during this stage of development to ensure the reliability and effectiveness of the application.

### Automated Tests

> The project includes automated tests for both backend and frontend, covering unit, integration and system levels. These tests were developing to validate the functionalities implemented, ensuring that the application behaves as expected.

#### Backend Tests

| Test Level | Test Class | Technology | Description | Traceability |
| :--- | :--- | :--- | :--- | :--- |
| **Unit (Services)** | `EventServiceTest.java` | JUnit / Mockito | Verifies event service behavior: pagination, get-by-id, filtering by participant, create/update/delete flows and validation. | F1 Discovery & Search, F5 Content Control, T3 Automated Testing |
| | `UserServiceTest.java` | JUnit / Mockito | Tests user profile updates, user retrieval and role/authorization-related logic. | F2 User Activity, T3 Automated Testing |
| | `ParticipantServiceTest.java` | JUnit / Mockito | Verifies participant service behavior: create/update/delete operations, pagination and get-by-id cases. | F1 Discovery & Search, F5 Content Control, T3 Automated Testing |
| | `TicketServiceTest.java` | JUnit / Mockito | Verifies ticket creation, purchase logic and ticket-user-event relationships. | F3 Ticketing & History, T3 Automated Testing |
| **Integration (Repositories)** | `EventRepositoryTest.java` | JUnit / Spring Boot | Verifies event repository behavior and persistence operations. | F1 Discovery & Search, F5 Content Control, T5 Data Persistence, T3 Automated Testing |
| | `UserRepositoryTest.java` | JUnit / Spring Boot | Verifies user repository behavior and persistence operations. | F2 User Activity, T5 Data Persistence, T3 Automated Testing |
| | `ParticipantRepositoryTest.java` | JUnit / Spring Boot | Verifies participant repository behavior and persistence operations. | F1 Discovery & Search, F5 Content Control, T5 Data Persistence, T3 Automated Testing |
| | `TicketRepositoryTest.java` | JUnit / Spring Boot | Verifies ticket repository behavior and relationships with events and users. | F3 Ticketing & History, T5 Data Persistence, T3 Automated Testing |
| **API/System Tests (REST Assured)** | `EventApiTest.java` | REST Assured | Verifies event API operations: list, detail, filters, validation, role restrictions, image endpoints and creation flows. | F1 Discovery & Search, F5 Content Control, T1 Modern Technologies, T3 Automated Testing |
| | `UserApiTest.java` | REST Assured | Verifies user API operations: current user retrieval, profile updates, image endpoints and HTTP status codes. | F2 User Activity, T1 Modern Technologies, T3 Automated Testing |
| | `ParticipantApiTest.java` | REST Assured | Verifies participant API operations: list, detail, create, update, delete, image endpoints and error handling. | F1 Discovery & Search, F5 Content Control, T1 Modern Technologies, T3 Automated Testing |
| | `TicketApiTest.java` | REST Assured | Verifies ticket API operations: purchasing, ownership validation, retrieval and cancellation if available. | F3 Ticketing & History, T1 Modern Technologies, T3 Automated Testing |
| | `AuthApiTest.java` | REST Assured | Verifies authentication and security flows: login, refresh, logout, registration and invalid input cases. | F2 User Activity, T1 Modern Technologies, T3 Automated Testing |

#### Frontend Tests

| Test Level | Test Class | Technology | Description | Traceability |
| :--- | :--- | :--- | :--- | :--- |
| **Unit (Components)** | `app.component.spec.ts` | Vitest / TestBed | Verifies root component initialization. | T1 Modern Technologies, T3 Automated Testing |
| | `event-list.component.spec.ts` | Vitest / TestBed | Verifies rendering of main page. | F1 Discovery & Search, T3 Automated Testing |
| | `event-detail.component.spec.ts` | Vitest / TestBed | Verifies rendering of event details. | F1 Discovery & Search, T3 Automated Testing |
| | `all-events.component.spec.ts` | Vitest / TestBed | Verifies the full events page, including pagination behavior. | F1 Discovery & Search, T3 Automated Testing |
| | `participants-list.component.spec.ts` | Vitest / TestBed | Verifies the participants list page, including pagination behavior. | F1 Discovery & Search, T3 Automated Testing |
| | `participant-detail.component.spec.ts` | Vitest / TestBed | Verifies rendering of participant detail pages. | F1 Discovery & Search, T3 Automated Testing |
| | `login.component.spec.ts` | Vitest / TestBed | Verifies login form validation and authentication behavior. | F2 User Activity, T3 Automated Testing |
| | `register.component.spec.ts` | Vitest / TestBed | Verifies registration form validation and error handling. | F2 User Activity, T3 Automated Testing |
| | `user-page.component.spec.ts` | Vitest / TestBed | Verifies user profile page rendering, profile editing and purchased tickets display. | F2 User Activity, F3 Ticketing & History, T3 Automated Testing |
| | `ticket-selection.component.spec.ts` | Vitest / TestBed | Verifies ticket selection UI, quantity selection and price summary behavior. | F3 Ticketing & History, T3 Automated Testing |
| | `manage-events.component.spec.ts` | Vitest / TestBed | Verifies administrator event management behavior. | F5 Content Control, T3 Automated Testing |
| | `manage-participants.component.spec.ts` | Vitest / TestBed | Verifies administrator participant management behavior. | F5 Content Control, T3 Automated Testing |
| | `header.component.spec.ts` | Vitest / TestBed | Verifies navigation behavior and role-based menu rendering. | F1 Discovery & Search, F2 User Activity, F5 Content Control, T3 Automated Testing |
| | `error.component.spec.ts` | Vitest / TestBed | Verifies error display and global error handling UI. | T1 Modern Technologies, T3 Automated Testing |
| | `add-participants.component.spec.ts` | Vitest / TestBed | Verifies add/edit participant form behavior. | F5 Content Control, T3 Automated Testing |
| | `add-event.component.spec.ts` | Vitest / TestBed | Verifies add/edit event form validation. | F5 Content Control, T3 Automated Testing |
| **Integration (Services)** | `event.service.spec.ts` | HttpClient / TestBed | Verifies real HTTP interactions with the event API. | F1 Discovery & Search, F5 Content Control, T1 Modern Technologies, T3 Automated Testing |
| | `user.service.spec.ts` | HttpClient / TestBed | Verifies real HTTP interactions with user-related API endpoints. | F2 User Activity, T1 Modern Technologies, T3 Automated Testing |
| | `participant.service.spec.ts` | HttpClient / TestBed | Verifies real HTTP interactions with the participant API. | F1 Discovery & Search, F5 Content Control, T1 Modern Technologies, T3 Automated Testing |
| | `ticket.service.spec.ts` | HttpClient / TestBed | Verifies real HTTP interactions with ticket API endpoints. | F3 Ticketing & History, T1 Modern Technologies, T3 Automated Testing |
| | `auth.service.spec.ts` | HttpClient / TestBed | Verifies real HTTP interactions with authentication API endpoints. | F2 User Activity, T1 Modern Technologies, T3 Automated Testing |
| **System (E2E)** | `EventE2ETest.java` | Selenium | Verifies event browsing, event creation and event detail workflows. | F1 Discovery & Search, F5 Content Control, T3 Automated Testing |
| | `UserE2ETest.java` | Selenium | Verifies user flows such as login, profile management and user page behavior. | F2 User Activity, T3 Automated Testing |
| | `ParticipantE2ETest.java` | Selenium | Verifies participant browsing, participant detail and management workflows. | F1 Discovery & Search, F5 Content Control, T3 Automated Testing |
| | `TicketE2ETest.java` | Selenium | Verifies the complete ticket purchase flow. | F3 Ticketing & History, T3 Automated Testing |

### Test Statistics

> This section summarizes the results obtained from the execution of the automated tests implemented in the project, including the number of tests executed, coverage percentages and results.

| Test Type | Total Tests | Passed | Failed | Coverage (%) |
| :--- | :---: | :---: | :---: | :---: |
| Unit (Server) | 64 | 64 | 0 | Included in backend total |
| Unit (Client) | 113 | 113 | 0 | 81.37% line coverage and 80.31% statement coverage |
| Integration (Server) | 40 | 40 | 0 | Included in backend total |
| Integration (Client) | 29 | 29 | 0 | 100% line coverage and 100% statement coverage |
| System (Server) | 103 | 103 | 0 | Included in backend total |
| System (Client) | 19 | 19 | 0 | N/A |
| Server Total | 207 | 207 | 0 | 91% instruction coverage / 58% branch coverage / 90.11% line coverage |
| Client Total | 161 | 161 | 0 | Unit coverage: 81.55% line / Integration coverage: 100% |

#### Backend Test Execution & Code Coverage Report (JaCoCo)

![Backend Tests](https://github.com/user-attachments/assets/9449c4c2-5d3c-4dcc-a4e7-8bd5c76e0bdb)

![Backend Coverage](https://github.com/user-attachments/assets/7aa5c878-ddbf-4c90-9879-b20cc7fb9360)

#### Frontend Test Execution & Code Coverage Report (Vitest)

![Frontend Tests & Coverage](https://github.com/user-attachments/assets/9c0226dc-930e-40c0-833f-e97e56b10db0)

### Static Code Analysis

> The project uses SonarCloud to perform static code analysis, identifying code smells, bugs and security vulnerabilities. The following table summarizes the main metrics obtained from the analysis, including number of classes, lines of code, etc.

| Metric | Count | Rating |
| :--- | :---: | :---: |
| Lines of Code | 3.3k | - |
| Reliability | 3 | B |
| Security | 1 | E |
| Maintainability | 12 | A |
| Security Hotspots | 1 | - |
| Duplicated Lines (%) | 0.0% | - |

![SonarCloud Analysis](https://github.com/user-attachments/assets/f57bbe34-4abf-45fc-bab7-31c10b3a46c2)

## Docker Deployment

> GoEventsNow is packaged and deployed using Docker, which allows the application to be executed in any environment with Docker installed. The deployment is based on a single Docker image for the web application and a Docker Compose file to coordinate the application container with the MySQL database container.

### Packaging & Publishing

> The application is packaged as a Docker image and published in DockerHub. Kubernetes and serverless deployment are not used in this version. The image includes both the backend and the frontend, so both parts of the application run together in the same container. This is made by using a multi-stage Dockerfile:

1. The Angular frontend is built.
2. The generated frontend files are copied into the Spring Boot static resources.
3. The Spring Boot backend is packaged.
4. The final container runs the generated application as `app.jar`.

### Docker Compose

> The compose files defined two services:

- `goeventsnow-app`: This service runs the Docker image containing the backend and frontend of the application.
- `goeventsnow-db`: This service runs the MySQL database container, which is required for the application to work.

> Two different docker-compose files for different purposes:

- `docker-compose.yml`: Used for final stable application version. In this case this will execute the `albertoml1999/goeventsnow-app:0.1.0` image, which is the stable version of the application published in DockerHub.
- `docker-compose-dev.yml`: Used for development and testing. This will execute the `albertoml1999/goeventsnow-app:dev` image, which is the development version of the application published in DockerHub.

> The stable version can be executed using the following command:

```bash
docker-compose -f docker-compose.yml up
```

> The development version can be executed using the following command:

```bash
docker-compose -f docker-compose-dev.yml up
```

> The application is exposed through HTTPS by mapping the host port 443 to the application port 8443 inside the container:

```text
https://localhost
```

### DockerHub Artifacts

> The application Docker image is published in DockerHub:

```text
https://hub.docker.com/r/albertoml1999/goeventsnow-app
```

> The Docker Compose file is also published in DockerHub as an OCI artifact:

```text
https://hub.docker.com/r/albertoml1999/goeventsnow-app-compose
```

> The stable release is published using the following tags:

```text
albertoml1999/goeventsnow-app:0.1.0
albertoml1999/goeventsnow-app:latest
albertoml1999/goeventsnow-app-compose:0.1.0
albertoml1999/goeventsnow-app-compose:latest
```

> The development version is published with the dev tag:

```text
albertoml1999/goeventsnow-app:dev
albertoml1999/goeventsnow-app-compose:dev
```

## Development Process

> The project follows an iterative and incremental process based on Agile principles, incorporating Extreme Programming (XP) practices like CI/CD and Kanban (GitHub Project) for workflow management.

### Task Management

> Managed through GitHub Projects (Kanban board) to organize and prioritize the GitHub Issues created for the development of new features, bug fixes and improvements.

### Git Strategy

> The project uses GitHub as the version control platform, implementing GitFlow as the branching strategy to manage the development process effectively. There are several types of branches used to organize the work:

- main: Stable production code.
- develop: Integration branch to develop new features or to fix errors.
- feature/**: Branch to add and develop new functionalities.
- fix/**: Branch to update and correct previous functionalities already created.

### Git Metrics

- Number of Commits: 248 commits made.
- Number of Branches: Not more than 4 at the same time (Main, Develop, Feature/** or Fix/** ).
- Number of Pull Requests: 61 Pull Requests made.

### Continuous Integration (CI)

> The CI workflows are implemented using GitHub Actions to automate the building, testing and quality control of the project, ensuring that both frontend and backend code is correct before merging into the main branch. There are 2 different types of workflows made, depending on the kind of tests they analyze and when its applied.

#### Workflow: CI - Basic (Basic Quality Control)

> Runs on every commit made in a feature/fix branch to ensure that changes do not break the application and its tests.

- Job:
    - Backend (Server) + Frontend (Client) + Sonar (Static Code Analysis):
        - Objective: Compile backend, build frontend, generate code coverage, static code analysis and run unit tests.
        - Steps:
            1. Set up the MySQL database.
            2. Checkout repository.
            3. Set up JDK 21 (Temurin distribution).
            4. Compile backend and run unit tests (`EventServiceTest,ParticipantServiceTest,TicketServiceTest,UserServiceTest`) using Maven.
            5. Set up Node.js 20 with npm.
            6. Install all dependencies.
            7. Build Angular project in production mode.
            8. Run frontend unit tests (`**/*.component.spec.ts`) with code coverage.
            9. Execute sonar-scanner for static code analysis.

#### Workflow: CI - Complete (Complete Quality Control)

> Runs on Pull Request made on a feature branch, with base in the main branch.

- Job:
    - Objective: Run unit, integration and system tests for both backend and frontend, and static code analysis.
    - Steps:
        1. Set up MySQL database.
        2. Checkout repository.
        3. Install wait tools.
        4. Set up JDK 21 and Node.js 20.
        5. Install all frontend dependencies.
        6. Compile backend and run Unit (`EventServiceTest`, `ParticipantServiceTest`, `TicketServiceTest`, `UserServiceTest`), Integration (`EventRepositoryTest`, `ParticipantRepositoryTest`, `TicketRepositoryTest`, `UserRepositoryTest`) & System tests (`AuthApiTest`, `EventApiTest`, `ParticipantApiTest`, `TicketApiTest`, `UserApiTest`).
        7. Build Angular project in production mode.
        8. Start backend and wait for port 8443.
        9. Run frontend Unit (`**/*.component.spec.ts`) & Integration tests ( `**/*.service.spec.ts`) with code coverage.
        10. Start frontend and wait for port 4200.
        11. Run frontend System tests (`EventE2ETest,ParticipantE2ETest,TicketE2ETest,UserE2ETest`).
        12. Execute sonar-scanner for static code analysis.

### Continuous Deployment (CD)

> The project uses GitHub Actions to automate the deployment process, ensuring that the application is packaged and published in DockerHub. There are three different workflows for deployment, depending on the trigger:

#### Workflow: CD - Dev to Main

> Runs when changes are pushed to the main branch.

- Job: `publish-dev`
    - Objective: Build and publish the application Docker image with the `dev` tag to DockerHub.
    - Trigger: Push events on the main branch.
    - Steps:
        1. Call the `reusable-build-publish` workflow with the `dev` tag as input to build and publish the development version of the application.

#### Workflow: CD - Release

> Runs when a release is published in the GitHub repository.

- Job: `publish-release`
    - Objective: Build and publish the application Docker image with the release tag and latest tag to DockerHub.
    - Trigger: Release published event.
    - Steps:
        1. Call the `reusable-build-publish` workflow with the release tag and `latest` as input to build and publish the stable version of the application.

#### Workflow: CD - Manual Trigger Dispatch

> Runs when manually triggered through GitHub Actions workflow dispatch.

- Job: `calculate-tag` & `publish-manual`
    - Objective: Build and publish the application Docker image with a custom tag based on the branch, date and commit.
    - Trigger: Manual workflow dispatch with optional input.
    - Steps:
        1. Calculate a custom tag using the current branch name, date and commit hash.
        2. Call the `reusable-build-publish` workflow with the calculated custom tag as input to build and publish the application.

#### Workflow: Reusable Build and Publish Workflow

> Reusable workflow called by the CD workflows to avoid duplicating the Docker build and publishing logic.

- Job: `build-and-push`
    - Objective: Build and publish the application Docker image and publish the Docker Compose file as an OCI artifact in DockerHub.
    - Trigger: It is called with `workflow_call`, so it is not executed directly. It is called by other workflows such as `CD - Dev to Main`, `CD - Release` and `CD - Manual Trigger Dispatch`.
    - Inputs:
        - `image_tag`: Tag used to publish the Docker image.
        - `push_latest`: Indicates if the `latest` tag must also be published.
        - `compose_tag`: Optional tag for the Docker Compose OCI artifact. If empty, it uses the same value as `image_tag`.
        - `compose_path`: Path to the Docker Compose file to publish.
        - `dockerfile`: Path to the Dockerfile used to build the application image.
        - `build_context`: Docker build context.
    - Steps:
        1. Checkout the repository.
        2. Set up Docker Buildx to build the Docker image.
        3. Log in to DockerHub using the repository secrets.
        4. Build and push the application Docker image using the received `image_tag`.
        5. If `push_latest` is enabled, also publish the Docker image with the `latest` tag.
        6. Install ORAS to publish OCI artifacts.
        7. Log in to DockerHub using ORAS.
        8. Check that the Docker Compose file exists.
        9. Publish the Docker Compose file as an OCI artifact in DockerHub using the selected tag.
        10. If `push_latest` is enabled, also publish the Docker Compose OCI artifact with the `latest` tag.
    - Published artifacts:
        - Docker image:
            - `albertoml1999/goeventsnow-app:<image_tag>`
            - `albertoml1999/goeventsnow-app:latest` when `push_latest` is enabled.
        - Docker Compose OCI artifact:
            - `albertoml1999/goeventsnow-app-compose:<compose_tag>`
            - `albertoml1999/goeventsnow-app-compose:latest` when `push_latest` is enabled.

### Release

> The project uses GitHub releases to package stable versions of the application.

The release process follows these steps:

1. Ensure that all tests are passing and the code is stable.
2. Update the project versions before creating the release:
    - Update the `version` field in the `pom.xml` file of the backend to match the new release version (e.g., `0.1.0`).
    - Update the `version` field in the `package.json` file of the frontend to match the new release version (e.g., `0.1.0`).
    - Update the `image` field in the `docker-compose.yml` file to use the new release version of the Docker image (e.g., `albertoml1999/goeventsnow-app:0.1.0`).
3. Create a new release in GitHub, providing the corresponding version number and a description of the changes included in the release.
4. The release will trigger the `CD - Release` workflow, which will build and publish the application Docker image and the Docker Compose OCI artifact with the release tag and latest tag to DockerHub.
5. After the release, the `main` branch is updated with the next development version (e.g., `0.2.0-SNAPSHOT`).

### Published Versions

| Version | Release Date | Type | High-level Description |
| :--- | :---: | :--- | :--- |
| `v0.0.1` | 10/02/2026 | Stable release | First stable release, which establishes the project architecture and quality standards. It includes Spring Boot backend, Angular frontend and MySQL database structure, together with CI workflows, SonarCloud analysis, automated testing and coverage reporting.|
| `0.0.1` | 13/05/2026 | Test release | First test release of the MVP version, used to verify that the release CD workflow published the Docker image and Docker Compose OCI artifact correctly.|
| `0.0.2` | 13/05/2026 | Test release | Second test release of the MVP version, used to verify the release CD workflow after fixing the Dockerfile and the deployment issue caused by the versioned `.jar` file name. |
| `0.1.0` | 15/05/2026 | Stable release | MVP release with user authentication, event and participant browsing, ticket purchasing, user profile management, administrator event/participant management and image management. It also adds Docker deployment, Docker Compose with MySQL, OCI compose artifacts and GitHub Actions workflows for development, manual and release builds. |

You can find the list of releases here:
[GoEventsNow Releases](https://github.com/codeurjc-students/2025-GoEventsNow/releases)


## Code Edit & Execution

> This section provides instructions to clone the repository, execute the application locally or with Docker, interact with the API, the tools used during development, and how to run the tests implemented.

### Cloning the Repository

> To clone the project repository, ensure you have Git installed. Then use the following command in your terminal or command prompt:

```bash
git clone https://github.com/codeurjc-students/2025-GoEventsNow
```

### Execution

> The application can be executed in two different ways:

1. **Local Execution**: Run the backend and frontend locally on your machine.
2. **Docker Execution**: Run the application using Docker and Docker Compose, which includes both the backend and frontend in a single container, and a separate container for the MySQL database.

#### Local Execution

> The application requires a MySQL server installed and running on your local machine. You can use the following credentials to connect to the database or start one using Docker:

```bash
docker run --name goeventsnow-db -e MYSQL_ROOT_PASSWORD=password -e MYSQL_DATABASE=goeventsnow_db -p 3306:3306 -d mysql:8
```

- Database Name: `goeventsnow_db`
- Username/Password: Ensure they match the configuration in the `application.properties` file of the backend. In the default configuration, it uses `root` as username and `password` as password.

> You can execute the backend and start the Spring Boot application:

```bash
cd backend
mvn spring-boot:run
```
The backend will start with HTTPS on port 8443.

```text
https://localhost:8443
```

The REST API will be available at:

```text
https://localhost:8443/api/v1
```

> In a separate terminal, launch the frontend Angular application:

```bash
cd frontend
npm install ## Only the first time used
npm start
```
The frontend will start on port 4200. 

#### Docker Execution

> To run the application using Docker, ensure you have Docker installed and running on your machine.

GoEventsNow is published in DockerHub as a Docker image and can be executed with Docker Compose. The project includes two Docker Compose files:

- `docker-compose.yml`: runs the stable version of the application.
- `docker-compose-dev.yml`: runs the development version of the application.

##### Run from DockerHub Image

> Stable version:

```bash
docker-compose -f docker-compose.yml up -d
```

> Development version:

```bash
docker-compose -f docker-compose-dev.yml up -d
```

> To stop the stable version:

```bash
docker-compose -f docker-compose.yml down
```

> To stop the development version:

```bash
docker-compose -f docker-compose-dev.yml down
```

##### Run from the DockerHub Compose OCI Artifact

> The Docker Compose file is also published in DockerHub as an OCI artifact, which can be pulled and executed using Docker Compose. To do this, you need to have ORAS installed to pull the OCI artifact and then execute it with Docker Compose. If you don't have ORAS installed, you can follow the instructions in the official documentation: [ORAS - OCI Artifacts](https://oras.land/docs/). Once you have ORAS installed, you need to create an empty folder and pull the Docker Compose artifact using the following command:

```bash
oras pull docker.io/albertoml1999/goeventsnow-app-compose:0.1.0
```

> The compose file will be downloaded in the folder previously created, and you can start the application with the following command:

```bash
docker-compose -f docker-compose.yml up -d
```

> To stop the application:

```bash
docker-compose -f docker-compose.yml down
```

##### Access URLs

> For Docker, the application will be available at:

```text
https://localhost
```

> The REST API will be available at:

```text 
https://localhost/api/v1
```

> If the browser shows a certificate warning, it can be accepted because the application uses a self-signed certificate for local HTTPS execution.

### Accessing the Application

> Once both backend and frontend are running locally, you can access the application through your web browser:

Access the application at: http://localhost:4200/.

> If you are running the application with Docker, you can access the application through your web browser: 

Access the application at: https://localhost/.

### CI Workflows

> The CI workflows are defined in the `.github/workflows/` directory of the repository. It contains two main workflows files and they can only be run depending on the trigger.

- `basic-quality.yml`: This workflow runs on every commit to a feature/fix branch, performing basic quality control by compiling the backend, building the frontend, running unit tests, and performing static code analysis with SonarCloud.
- `complete-quality.yml`: This workflow runs on pull requests to the main branch, executing a complete quality control process that includes unit, integration, and system tests for both backend and frontend, including code analysis with SonarCloud.

### CD Workflows

> The CD workflows are defined in the `.github/workflows/` directory of the repository. It contains three main workflow files for deployment, depending on the trigger.

- `cd-dev.yml`: This workflow runs when changes are pushed to the main branch, building and publishing the development version of the application Docker image and Docker Compose OCI artifact to DockerHub.
- `cd-release.yml`: This workflow runs when a release is published in the GitHub repository, building and publishing the stable version of the application Docker image and Docker Compose OCI artifact to DockerHub.
- `cd-manual-dispatch.yml`: This workflow can be manually triggered through GitHub Actions workflow dispatch, allowing to build and publish a custom version of the application Docker image and Docker Compose OCI artifact to DockerHub based on the branch, date and commit.

> The reusable workflow `reusable-build-publish.yml` is called by the CD workflows to perform the actual building and publishing of the Docker images and Docker Compose OCI artifacts, avoiding duplication of code in the CD workflows.

### Used Tools

> This section describes the main tools used during development, testing, deployment and interaction with the application.

- **Visual Studio Code**: Main IDE used for both backend and frontend development. It provides a unified environment for editing, debugging and testing the application. Extensions for Java, Angular, Docker, Git and SonarQube for IDE were used to improve the development workflow.

- **Postman**: Used to manually test and interact with the REST API. It allows sending HTTP requests, checking JSON responses, testing authentication endpoints and validating protected operations.

- **Web Browser**: Used to access and test the web application from the user interface. It was also used to validate SPA routing, authentication flows and HTTPS access both in local development and Docker execution.

- **GitHub**: Platform used to host the repository, version control, manage pull requests and commits, create releases and store the project source code.

- **GitHub Actions**: Used to CI/CD workflows, including backend/frontend tests, static code analysis and Docker image publication.

- **Docker/DockerHub**: Used as the registry where the application Docker image and the Docker Compose OCI artifact are published.

- **ORAS**: Used to publish and pull the Docker Compose file as an OCI artifact from DockerHub.

- **SonarCloud**: Used to perform static code analysis and monitor code quality, maintainability, bugs, vulnerabilities and duplicated code.

- **MySQL**: Used to manage the MySQL database during development.

### API Interaction (Postman)

> Postman is used to test and interact with the REST API provided by the backend. This allows to verify the endpoints, check JSON responses and simulate requests.

**How to use Postman:**
1. Download and install Postman from [https://www.postman.com/](https://www.postman.com/).
2. Open Postman and create a new request.
3. Set the request method (GET, POST, etc.) and the URL (e.g., `https://localhost:8443/api/v1/events/`) locally or (e.g., `https://localhost/api/v1/events/`) in Docker.
4. Add any necessary headers or body data for the request.
5. Send the request and review the response from the server.

**Collection File:**
- You can find the Postman collection file generated here: [GoEventsNow Postman Collection](GoEventsNow_Collection.postman_collection.json).

### Test Execution

> This section describes the steps to execute the automated tests implemented in the project for both backend and frontend.

#### Backend Tests

> To run all the backend tests, ensure you have Maven installed. Then execute the following commands to execute the different types of tests:

##### Unit tests

```bash
cd backend
mvn -Dtest="EventServiceTest,ParticipantServiceTest,TicketServiceTest,UserServiceTest" test
```

##### Integration tests

```bash
cd backend
mvn -Dtest="EventRepositoryTest,ParticipantRepositoryTest,TicketRepositoryTest,UserRepositoryTest" test
```

##### System tests

```bash
cd backend
mvn -Dtest="AuthApiTest,EventApiTest,ParticipantApiTest,TicketApiTest,UserApiTest" test
``` 

This command will run the unit, integration and system tests defined in the backend.

> To run a specific test, use the following command, replacing `TEST_CLASS_NAME` with the name of the test class you want to execute:

```bash
cd backend
mvn test -Dtest="${TEST_CLASS_NAME}"
```

#### Frontend Tests

> To run the frontend tests, ensure you have Node.js and npm installed. Then execute the following commands to run the different types of tests:

##### Unit Tests

```bash
cd frontend
ng test --include="**/*.component.spec.ts" --watch=false --coverage
```

##### Integration Tests

> To run the integration test, the backend application must be running. Then execute the following command to run the integration tests with code coverage:

```bash
cd frontend
npm run test:integration
```
This command will run the unit and the integration tests defined in the frontend, with code coverage. You can view the coverage report by opening: `frontend/coverage/index.html` in your web browser.

##### System Tests

> To run the system tests, ensure the backend application and the frontend application are running. Then execute the following command:

```bash
cd backend
mvn -Dtest="EventE2ETest,ParticipantE2ETest,TicketE2ETest,UserE2ETest" test
```

