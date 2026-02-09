# Index

 - [Introduction](#introduction)
 - [Technologies](#technologies)
 - [Tools](#tools)
 - [Architecture](#architecture)
 - [Quality Control](#quality-control)
 - [Development Process](#development-process)
 - [Code execution & Editing](#code-edit--execution)

## Introduction

> **GoEventsNow** is a full-stack web application designed following a SPA (Single Page Application) architecture. In this architecture, the frontend is responsible for rendering the user interface dynamically as the user interacts with it, obtaining the data when the backend exposes with a API REST that handles the application logic and data persistence.

The application is divided into three main layers:

- **Client (Frontend)**: Implemented using Angular, it manages user interaction, navigation, and dynamic content rendering. It communicates with the backend via HTTP requests.
- **Server (Backend)**: Developed using Spring Boot (Java), the backend provides a API REST that manages business logic and exposes the data obtained from the Database.
- **Database (Persistence)**: A MySQL relational database is used to ensure storage and persistence of application data.

| Concept | Description | 
| :--- | :--- |
| **Type** | Web Application SPA (Single Page Application) + API REST |
| **Technologies** | **Backend**: Spring Boot, Java 21. **Frontend**: Angular, TypeScript, HTML, CSS. **Database**: MySQL |
| **Tools** | VsCode, Maven, Git, Postman |
| **Quality Control** | **Tests**: Unit, Integration and System (JUnit, REST Assured, Selenium). **Coverage**: JaCoCo and Vitest. **Analysis**: Static code with Sonar |
| **Deployment** | Docker (no implemented yet, but planned for future phases) |
| **Development process** | Iterative and Incremental methodology using GitFlow and GitHub Actions for CI |

## Technologies

> Main technologies used for the execution of the project, with a description about the purpose in the project and its official URLs.

- **Java 21**: Programming language used for the backend logic and REST API implementation.  
    Official URL: [https://www.oracle.com/java/](https://www.oracle.com/java/)

- **Spring Boot**: Java framework used to create the backend. It manages the application logic, handles the main functionalities and entities, and provides data to the Angular frontend through the API REST.  
    Official URL: [https://spring.io/projects/spring-boot](https://spring.io/projects/spring-boot)

- **Angular**: Frontend framework for building the Single Page Application (SPA). It manages client-side routing, dynamic content rendering and HTTP communication with the backend API.  
    Official URL: [https://angular.dev/](https://angular.dev/)

- **MySQL**: Relational database used for persistent storage of events, ensuring data integrity.  
    Official URL: [https://www.mysql.com/](https://www.mysql.com/)

- **TypeScript**: Programming language used in the frontend to add components, model and services. 
    Official URL: [https://www.typescriptlang.org/](https://www.typescriptlang.org/)

# Tools 

> Main tools and IDEs used during development.

- **Visual Studio Code**: Code editor used for both Backend/Frontend development.
    Official URL: [https://code.visualstudio.com/](https://code.visualstudio.com/)

- **Postman**: Tool for testing and documenting API REST endpoints.
    Official URL: [https://www.postman.com/](https://www.postman.com/)

- **Git**: Version control system used to track changes in source code, in addition to manage the tasks development and the implementation of GitFlow workflows.
    Official URL: [https://github.com/](https://github.com/)

- **Maven**: Build automation tool used to manage the backend dependencies and lifecycle of the Java application.
    Official URL: [https://maven.apache.org/](https://maven.apache.org/)

- **Figma**: Interface design tool used to create the screen mockups and navigation flows defined in the analysis phase.
    Official URL: [https://www.figma.com/](https://www.figma.com/)

## Architecture

### Deployment

> The deployment architecture is based on independent processes communicating through HTTP following the API REST architectural style:

The application is deployed as several independent processes:

1. Frontend (Port 4200): It handles the user interface and presentation logic. It communicates with the backend via HTTP comunication and using JSON as the data interchange format.

2. Backend (Port 8080): The Spring Boot application runs on the server, and it acts as an API REST that handles the business logic and data persitence. It communicates with the Database to obtain data, and with the Frontend to listen the requests. 

3. MySQL (Port 3306): The MySQL database runs, ensuring data integrity and persistence. It allows the communication with the Backend to send data, ensuring security.

### API REST

> The communication between the Client and the Server is documented using OpenAPI and served as a web page using [https://raw.githack.com](https://raw.githack.com).

- You can view the complete and interactive API documentation here: [OpenAPI HTML Documentation](https://raw.githack.com/codeurjc-students/2025-GoEventsNow/main/backend/docs/api/api-docs.html)

## Quality Control

> This section details the quality controls and automated tests implemented during this stage of development to ensure the reliability and correctness of the application.

### Automated Tests

> The project includes automated tests at diffrent levels to verify the functionality and effectiveness of both backend and frontend. The following table summarizes the types of tests implemented, the technologies used and their descriptions:

| Test Type | Technology | Description & Traceability |
| :--- | :--- | :--- |
| Unit (Server) | JUnit / Mockito | EventTest.java - Tests basic functionalities implemented in EventService, such as GET and POST operations for events. **Traceability**: Verifies Objective#5 (Content Control) and Objective#1 (Discovery) |
| Unit (Client) | Vitest / TestBed | app.component.spec.ts - Tests the component and data rendering using mocks. **Traceability**: Verifies Objective#1 (Discovery)  |
| Integration (Server) | JUnit / Spring Boot |  EventBBDDTest.java - Test the repository persistence and the database interaction. **Traceability**: Verifies Objective#5 (Content Control) persistence  |
| Integration (Client) | HttpClient |  event.service.spec.ts - Test the Frontend service communication and HTTP response handling. **Traceability**: Ensures data flow for Objective#1  |
| System (Server) | Rest Assured |  EventApiTest.java - Test the REST API, verifying status codes and JSON response. **Traceability**: Verifies for all Objectives |
| System (Client) | Selenium Webdriver |  SeleniumTest.java - E2E testing simulating a real user navigation flow in the the main page in Headless Chrome. **Traceability**: Verifies Objective#1 (Discovery) flow |

### Test Stadistics

> This section summarizes the results obtained from the execution of the automated tests implemented in the project, including the number of tests executed, coverage percentages and results.

| Test Type | Total Tests | Passed | Failed | Coverage (%) |
| :--- | :---: | :---: | :---: | :---: |
| Unit (Server) | 2 | 2 | 0 | 100% (Service) / 23% (Controller) |
| Unit (Client) | 3 | 3 | 0 | 94.73% |
| Integration (Server) | 2 | 2 | 0 | Included in total |
| Integration (Client) | 1 | 1 | 0 | 100% |
| System (Server) | 1 | 1 | 0 | N/A |
| System (Client) | 1 | 1 | 0 | N/A |
| Server Total | 7 | 7 | 0 | 74% |
| Client Total | 4 | 4 | 0 | 95.65% |

#### Backend Test Execution & Code Coverage Report (JaCoCo)

![Client Tests](https://github.com/user-attachments/assets/9449c4c2-5d3c-4dcc-a4e7-8bd5c76e0bdb)

![Client coverage](https://github.com/user-attachments/assets/7aa5c878-ddbf-4c90-9879-b20cc7fb9360)

#### Frontend Test Execution & Code Coverage Report (Vitest)

![Server Tests & Coverage](https://github.com/user-attachments/assets/9c0226dc-930e-40c0-833f-e97e56b10db0)

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

## Development Process

> The project follows an iterative and incremental process based on Agile principles, incorporating Extreme Programming (XP) practices like CI and Kanban (GitHub Project) for workflow management.

### Task Management

> Managed through GitHub Projects (Kanban board) to organize and proritize the GitHub Issues created for the development of new features, bug fixes and improvements.

### Git Strategy

> The project uses GitHub as the version control platform, implementing GitFlow as the branching strategy to manage the development process effectively. There are several types of branches used to organize the work:

- main: Stable production code.
- develop: Integration branch to develop new features or to fix errors.
- feature/**: Branch to add and develop new functionalities.
- fix/**: Branch to update and correct previous functionalities already created.

### Git Metrics

- Number of Commits: 73 commits made.
- Number of Branches: Not more than 4 at the same time (Main, Develop, Feature/** or Fix/** ).
- Number of Pull Requests: 20 Pull Requests made.

### Continous Integration (CI)

> The CI workflows are implemented using GitHub Actions to automate the building, testing and quality control of the project, ensuring that both frontend and backend code is correct before merging into the main branch. There are 2 different types of workflows made, depending on the kind of tests they analyze and when its applied.

#### Workflow: CI - Basic (Basic Quality Control)

> Runs on every commit made in a feature branch to ensure that changes do not break the application and its tests.

- Jobs:
    - Backend (Server) + Frontend (Client) + Sonar (Static Code Analysis):
        - Objective: Compile backend, build Frontend, generate code coverage, static code analysis and run unit tests.
        - Steps:
            1. Set up the MySQL database.
            2. Checkout repository.
            3. Set up JDK 21 (Temurin distribution).
            4. Compile backend and run unit test (`EventTest`) using Maven.
            5. Set up Node.js 20 with npm.
            6. Install all dependencies.
            7. Build Angular project in production mode.
            8. Run frontend unit tests (`app.component.spec.ts`) with code coverage.
            9. Execute sonar-scanner for static code analysis.

#### Workflow: CI - Complete (Complete Quality Control)

> Runs on Pull Request made on a feature branch, with base in the main branch.

- Job:
    - Objective: Run unit, integration and system tests for both backend and frontend.
    - Steps:
        1. Set up MySQL database.
        2. Checkout repository.
        3. Set up JDK 21 and Node.js 20.
        4. Install all frontend dependencies.
        5. Compile backend and run unit & integration tests (`EventTest`, `EventBBDDTest`).
        6. Run frontend unit & integration tests (`app.component.spec.ts`, `event.service.spec.ts`)
        7. Install wait tools to check port availability.
        8. Execute sonar-scanner for static code analysis.
        9. Start backend and wait for port 8080.
        10. Start frontend and wait for port 4200.
        11. Run system tests:
            - Backend (Server): API tests using REST Assured (`EventApiTest`).
            - Frontend (Client): Selenium e2e test (`SeleniumTest`).

## Code Edit & Execution

> This section provides instruction to clone the repository, execute the application locally, the tools used during development, and how to run the tests implemented.

### Cloning the Repository

> To clone the project repository, ensure you have Git installed. Then use the following command in your terminal or command prompt:

```bash
git clone https://github.com/codeurjc-students/2025-GoEventsNow
```

### Execution

> The application requires a MySQL server installed and running on your local machine. You can use the following credentials to connect to the database:

- Database Name: `goeventsnow`
- Username/Password: Ensure they match the configuration in the `application.properties` file of the backend. In the default configuration, it uses `root` as username and `password` as password.

> You can execute the Backend and start the Spring Boot application:

```bash
cd backend
mvn spring-boot:run
```
The Backend will start on port 8080.

> In a separate terminal, launch the Frontend Angular application:

```bash
cd frontend
npm install ## Only the first time used
npm start
```
The Frontend will start on port 4200. 

### Accessing the Application

> Once both backend and frontend are running, you can access the application through your web browser:

Access the application at: http://localhost:4200/.

### CI Workflows

> The CI workflows are defined in the `.github/workflows/` directory of the repository. It contains two main workflow files and they can only be run depending on the trigger.

- `basic-quality.yml`: This workflow runs on every commit to a feature/fix branch, performing basic quality control by compiling the backend, building the frontend, running unit tests, and performing static code analysis with SonarCloud.
- `complete-quality.yml`: This workflow runs on pull requests to the main branch, executing a complete quality control process that includes unit, integration, and system tests for both backend and frontend, including code analysis with SonarCloud.

### Used Tools

> This section describes the main tools used during development for code editing, testing and interaction with the application.

- IDEs (Visual Studio Code): Use of Visual Studio Code for both backend and frontend development, providing a unified environment for coding, debugging and testing. It offers extensions for Java, Angular, and Docker, facilitating the development process.
- Postman: Used to test and interact with the API REST.
- Browser: To access to the application at the port previously commented.

### API Interaction (Postman)

> Postman is used to test and interact with the API REST provided by the backend. This allows to verify the endpoints, check JSON responses and simulate requests.

**How to use Postman:**
1. Download and install Postman from [https://www.postman.com/](https://www.postman.com/).
2. Open Postman and create a new request.
3. Set the request method (GET, POST, etc.) and the URL (e.g., `http://localhost:8080/api/v1/events/`).
4. Add any necessary headers or body data for the request.
5. Send the request and review the response from the server.

**Collection File:**
- You can find the Postman collection file generated here: [GoEventsNow Postman Collection](GoEventsNow_Collection.postman_collection.json).

### Test Execution

> This section describes the steps to execute the automated tests implemented in the project for both Backend and Frontend.

#### Backend Tests

> To run all the backend tests, ensure you have Maven installed. Then execute the following commands:

```bash
cd backend
mvn clean verify
```
This command will run all the unit, integration, and system tests defined in the backend.

> To run a specific test, use the following command, replacing `TEST_CLASS_NAME` with the name of the test class you want to execute:

```bash
cd backend
mvn test -Dtest="${TEST_CLASS_NAME}"
```

#### Frontend Tests

> To run all the frontend tests, ensure you have Node.js and npm installed. Then execute the following commands:

```bash
cd frontend
npm test
```
This command will run all the unit, integration, and system tests defined in the frontend.

> To run the frontend tests with code coverage, use the following command:

```bash
cd frontend
ng test --coverage
```

This will generate a coverage report you can view by opening: `frontend/coverage/index.html` in your web browser.

### Release

> The project uses GitHub releases to package stable versions of the application.

The release process follows these steps:
1. Ensure that all tests are passing and the code is stable.
2. Create a new release in GitHub, providing a version number and description of the changes included in the release.
3. The release will be tagged in the repository, and you can download the source code.

You can find the list of releases here:
[GoEventsNow Releases](https://github.com/codeurjc-students/2025-GoEventsNow/releases)

