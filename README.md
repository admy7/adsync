# AdSync

## Overview

Are you a marketing manager looking to streamline your ad campaigns? AdSync is a modern web application that helps you
manage your ad campaigns with ease. The application allows you to create, update, and delete ad campaigns, as well as
view detailed analytics for each campaign. With AdSync, you can track the performance of your ad campaigns and make
data-driven decisions to optimize your marketing strategy.

## Features

This project is a monorepo that includes a **Spring Boot** backend and a **React** frontend built with **Vite**. The
backend serves as the API layer for the application, while the frontend provides the user interface. The project is
designed for scalability and ease of use, leveraging modern tools and frameworks.

- **Backend**
    - Built with Spring Boot (with Java and Maven).
    - REST API to handle business logic.
    - PostgreSQL as the database.
- **Frontend**
    - Built with React and Vite for a fast development experience.
    - TailwindCSS for styling.
    - Responsive and optimized for performance.

## Prerequisites

- **Node.js** (v21 or higher)
- **Java** (v21 or higher)
- **Docker** (if running via containers)
- **Docker Compose**
- **PostgreSQL** (if running the backend locally without Docker)

---

## Installation

### Run with Docker (Recommended)

#### 1. Build Docker Images

From the root directory, copy the `.env.example` file and configure it:

```bash
cp .env.example .env
```

Run:

```bash
docker-compose build
// or docker compose build 
// (depending on your docker compose version)
```

#### 2. Start Services

Run the following command to start the backend, frontend, and database:

```bash
docker-compose up
// or docker compose up
// (depending on your docker compose version)
```

#### 3. Access the Application

- **Frontend**: `http://localhost:5173`
- **Backend API**: `http://localhost:8080`

----

### Local Development Setup

#### 1. Set Up the Backend

##### Launch the PostgreSQL Database

From the root directory, run:

```bash
docker-compose -f docker-compose.dev.yml up -d
// or docker compose -f docker-compose.dev.yml up -d
// (depending on your docker compose version)
```

##### Run the backend

```bash
cd apps/backend
./mvnw spring-boot:run
```

> The backend will start at `http://localhost:8080` by default.

---

#### 2. Set Up the Frontend

##### Navigate to the frontend directory:

```bash
cd apps/frontend
```

##### Install dependencies:

```bash
npm install
```

##### Create a `.env` file:

Copy the `.env.example` file and configure it:

```bash
cp .env.example .env
```

##### Start the development server:

```bash
npm run dev
```

> The frontend will start at `http://localhost:5173` by default.

---

## Folder Structure

```
adsync
├── .github
│   └── workflows
│       └── build-backend.yml
│       └── build-frontend.yml
├── apps
│   ├── backend
│   │   ├── src
│   │   ├── pom.xml
│   │   ├── Dockerfile
│   │   └── ...
│   ├── frontend
│   │   ├── src
│   │   ├── package.json
│   │   ├── Dockerfile
│   │   └── ...
├── .gitignore
├── docker-compose.yml
├── docker-compose.dev.yml
└── README.md
```

---

## Troubleshooting

- **Services not starting**: Check if the configuration files are properly configured (application.yml or .env) and the database is running.
- **Frontend issues**: Ensure all dependencies are installed by running `npm install`.
- **Docker issues**: Run `docker-compose logs` or `docker ps` to identify the error.
---

## Contributing

Contributions are welcome! Feel free to submit issues and pull requests to improve the project.

---

## Contact

For any inquiries, please open a GitHub issue.

