# Quick Start

## 1. Install Java 17
- Windows: install Eclipse Temurin 17 or Oracle JDK 17, then verify with `java -version`
- macOS: install Temurin 17 and verify with `java -version`
- Ubuntu/Linux: install OpenJDK 17 and verify with `java -version`

## 2. Install PostgreSQL
- Download PostgreSQL from the official installer for your OS
- During setup, keep note of the username and password you choose
- Verify PostgreSQL is running on port `5432`

## 3. Create the Database
Run this SQL command:

```sql
CREATE DATABASE devsecops_db;
```

## 4. Change Database Username and Password
Edit:

`src/main/resources/application-dev.properties`

Update:

```properties
spring.datasource.username=postgres
spring.datasource.password=postgres
```

## 5. Run the Project Locally
From the project root:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

## 6. Test APIs in Swagger
- Start the application
- Open `http://localhost:8080/swagger-ui/index.html`
- Use the endpoints in this order for a clean demo:
  1. Create Project
  2. Create Build
  3. Update Build Status to `SUCCESS`
  4. Create Deployment
  5. Create Approval for PROD deployment
  6. Approve deployment
  7. Update deployment status
  8. Create quality report
  9. View audit logs

## 7. Push Project to GitHub

```bash
git init
git add .
git commit -m "Initial commit - Java DevSecOps Release Manager"
git branch -M main
git remote add origin https://github.com/<your-username>/java-devsecops-release-manager.git
git push -u origin main
```

## 8. Deploy Quickly with Docker

```bash
docker compose up --build
```

This starts PostgreSQL and the Spring Boot application together.
