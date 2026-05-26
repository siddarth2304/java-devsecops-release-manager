# Java DevSecOps Release Manager

## Problem Statement
Enterprise software teams often struggle with poor visibility across build, deployment, approval, rollback, and code quality workflows. This project solves that problem by providing a Java Spring Boot based backend system that tracks software projects, builds, deployments, code quality reports, approvals, and audit logs in one structured release automation platform.


## Features
- Project management
- Build tracking
- Deployment tracking
- Code quality reports
- Approval workflow
- Rollback management
- Audit logs
- REST APIs
- PostgreSQL integration
- Docker support
- GitHub Actions CI pipeline

## Tech Stack
- Java 17
- Spring Boot 3
- Spring Data JPA
- PostgreSQL
- H2 for testing
- Maven
- JUnit 5
- Mockito
- Docker
- GitHub Actions
- Swagger/OpenAPI

## Architecture
This project follows a layered architecture:

`Controller -> Service -> Repository -> Database`

- `controller`: REST endpoints and request handling
- `service`: business rules, validation orchestration, audit logging
- `repository`: JPA persistence layer
- `entity`: database model
- `dto`: request/response contracts
- `exception`: centralized error handling
- `config`: OpenAPI configuration

## API Endpoints

| Module | Method | Endpoint | Description |
| --- | --- | --- | --- |
| Projects | POST | `/api/projects` | Create a project |
| Projects | GET | `/api/projects` | List all projects |
| Projects | GET | `/api/projects/{id}` | Get project by id |
| Projects | PUT | `/api/projects/{id}` | Update project |
| Projects | DELETE | `/api/projects/{id}` | Delete project |
| Builds | POST | `/api/builds` | Create a build |
| Builds | GET | `/api/builds` | List all builds |
| Builds | GET | `/api/builds/{id}` | Get build by id |
| Builds | GET | `/api/builds/project/{projectId}` | Get builds by project |
| Builds | PUT | `/api/builds/{id}/status` | Update build status |
| Deployments | POST | `/api/deployments` | Create a deployment |
| Deployments | GET | `/api/deployments` | List all deployments |
| Deployments | GET | `/api/deployments/{id}` | Get deployment by id |
| Deployments | GET | `/api/deployments/build/{buildId}` | Get deployments by build |
| Deployments | PUT | `/api/deployments/{id}/status` | Update deployment status |
| Deployments | PUT | `/api/deployments/{id}/rollback` | Roll back a failed deployment |
| Quality Reports | POST | `/api/quality-reports` | Create a quality report |
| Quality Reports | GET | `/api/quality-reports` | List all quality reports |
| Quality Reports | GET | `/api/quality-reports/{id}` | Get quality report by id |
| Quality Reports | GET | `/api/quality-reports/project/{projectId}` | Get reports by project |
| Approvals | POST | `/api/approvals` | Create approval request |
| Approvals | GET | `/api/approvals` | List all approvals |
| Approvals | GET | `/api/approvals/{id}` | Get approval by id |
| Approvals | GET | `/api/approvals/deployment/{deploymentId}` | Get approvals by deployment |
| Approvals | PUT | `/api/approvals/{id}/decision` | Update approval decision |
| Audit Logs | GET | `/api/audit-logs` | List all audit logs |
| Audit Logs | GET | `/api/audit-logs/{id}` | Get audit log by id |
| Audit Logs | GET | `/api/audit-logs/entity/{entityType}/{entityId}` | Filter audit logs by entity |

## How to Run Locally

### Step 1
Create PostgreSQL database:

```sql
CREATE DATABASE devsecops_db;
```

### Step 2
Update database username and password in:

`src/main/resources/application-dev.properties`

### Step 3
Run the app:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### Step 4
Open Swagger:

`http://localhost:8080/swagger-ui/index.html`

## How to Run with Docker

```bash
docker compose up --build
```

## How to Run Tests

```bash
mvn clean test
```

## How to Run Checkstyle

```bash
mvn checkstyle:check
```

## Future Improvements
- Add JWT authentication
- Add frontend dashboard
- Add SonarQube integration
- Add GitHub webhook integration
- Add deployment analytics dashboard

## Author
Sahith Siddarth Earlapally  
GitHub: https://github.com/siddarth2304  
Portfolio: sahithsiddarth.in
