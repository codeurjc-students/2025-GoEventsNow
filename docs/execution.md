# 🚀 Execution Guide

> This section explains how to execute the application using Docker, including the requirements and the steps to run the application from the published Docker Compose in DockerHub. It also provides instructions for running the application locally using Docker Compose.

## Requirements

> To run the application, Docker must be installed.

- On Windows, Docker Desktop is required: https://docs.docker.com/desktop/setup/install/windows-install/
- On macOS, Docker Desktop is required: https://docs.docker.com/desktop/setup/install/mac-install/
- On Linux, Docker Engine and Docker Compose are required:
  - Docker Engine: https://docs.docker.com/engine/install/
  - Docker Compose plugin: https://docs.docker.com/compose/install/linux/

> To download the Docker Compose artifact published as an OCI artifact, ORAS is also required:

-  ORAS installation: https://oras.land/docs/installation/
- On Windows, ORAS can be installed with:

```powershell
winget install oras
```

## Running the application from DockerHub Compose OCI artifact

> Create an empty folder and move into it:

```bash
mkdir goeventsnow-run
cd goeventsnow-run
```

> Download the Docker Compose file published in DockerHub:

```bash
oras pull docker.io/albertoml1999/goeventsnow-app-compose:0.1.0
```

> Start the application:

```bash
docker compose -f docker/docker-compose.yml up -d
```

> This starts two containers:

- `goeventsnow-app`: the Spring Boot backend with the Angular frontend served as static resources.
- `goeventsnow-db`: the MySQL database used by the application.

> To check that both containers are running:

```bash
docker ps
```

> The application will be available at:

```text
https://localhost
```

> To stop the application:

```bash
docker compose -f docker/docker-compose.yml down
```

## Alternative execution using the Docker image

> The application image can also be pulled directly from DockerHub:

```bash
docker pull albertoml1999/goeventsnow-app:0.1.0
```

> However, the application requires a MySQL database, so the recommended way to run it is through Docker Compose.

> If the repository is cloned locally, it can be executed with:

```bash
docker compose -f docker/docker-compose.yml up -d
```

> The image used by this compose file is:

```text
albertoml1999/goeventsnow-app:0.1.0
```

## Access credentials and sample data

> The application includes two example users:

- Username: `admin` / Password: `adminpass`: Administrator user access for managing events and participants.
- Username: `user` / Password: `pass`: Registered user access for viewing the profile and purchasing tickets.

> Anonymous users can browse the events and participants list and details without logging in.

> The backend also loads sample data in the database to show the main application features, including:

- Events from several categories, such as music, sports, cinema, technology, comedy and gastronomy.
- Participants linked to those events, including artists, athletes, actors, chefs and technology speakers.
- Images for events and participants associated with them.