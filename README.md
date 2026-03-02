# BetterCare Monitoring System

A Spring Boot–based environmental monitoring platform that processes sensor data (UV index, pollution, traffic), generates intelligent alerts, and provides a dashboard for users to monitor environmental conditions in real time.

This project was developed as part of an Integration / Software Engineering course, focusing on layered architecture, clean design principles, and real-world backend structure.

---

## Tech Stack

* **Java 21**
* **Spring Boot 3**
* **Spring MVC**
* **JDBC** (custom repositories)
* **H2** (demo mode) / **PostgreSQL** (production)
* **Thymeleaf**
* **ONNX Runtime** (AI integration)
* **Bootstrap** (UI styling)

---

## Architecture

The application follows a clean layered structure:
**presentation → business → data**

### Presentation Layer
* Controllers (MVC + REST)
* ViewModels
* Thymeleaf templates

### Business Layer
* Services
* AI logic
* Notification logic
* Environmental risk calculation

### Data Layer
* JDBC repositories
* RowMappers
* SQL schema

**The project applies:**
* **SOLID** principles
* **Separation of Concerns**
* **Clean controller-service-repository** structure

## Core Features

###  Sensor Data Monitoring
* Processes UV index and pollution readings
* Stores environmental observations
* Displays historical data on dashboard
* Calculates pollution average dynamically

### Traffic Monitoring
* Tracks traffic congestion levels
* Visualizes traffic jams over time
* Integrated into main dashboard

### AI-Based Future Prediction
* Uses **ONNX runtime** to predict future air quality
* Provides environmental forecasting
* Displays predicted AQ on home dashboard

###  Notification System
* Generates environmental alerts
* Stores user-specific notifications
* Shows latest notifications on dashboard
* Supports notification history

### Email Alert System
* Sends email alerts for environmental risks
* **SMTP integration** (Brevo)
* Triggered through scheduled checks

---

## Backend Logic

### Scheduled Background Processing
* Periodic environmental checks
* Automatic alert generation
* Runs via `@EnableScheduling`

### User Management
* User registration and login
* Profile management
* Session-based authentication
* User-specific notification tracking

### Risk & Advice System
* Calculates environmental risk level
* Determines highest danger level based on:
    * **Pollution level**
    * **UV index**
* Generates health advice dynamically

---

## Demo Mode vs Production Mode

The application supports two database configurations to balance development speed with production stability:

1. **PostgreSQL** (Production / Infrastructure server)
2. **H2 In-Memory Database** (Local demo mode)

**In demo mode:**
* Infrastructure dependencies are removed
* Scheduled logic can be disabled
* UI can be demonstrated without an external database

**This separation allows:**
* Easier local testing
* Client demos without deployment complexity

## Project Structure

```text
org.bettercare
 ├── business
 │   ├── services        # Business logic & Notification handling
 │   ├── intelligence    # AI prediction & Risk calculation
 │   └── entities        # Plain Old Java Objects (POJOs)
 ├── data
 │   ├── repository      # Manual JDBC implementation
 │   └── rowmappers      # SQL to Object mapping logic
 ├── presentation
 │   └── controller      # MVC & REST Endpoints
 └── resources
     ├── templates       # Thymeleaf HTML files
     ├── static          # CSS, JS, and Images
     ├── schema.sql      # Database structure
     └── data.sql        # Initial seed data
```

## Learning Outcomes

Through this project, I practiced:

* **Designing layered enterprise applications** with clear separation of concerns.
* **Implementing JDBC repositories manually** to understand low-level data access.
* **Applying SOLID principles** in a real-world project structure.
* **Integrating AI prediction (ONNX)** directly inside the Spring Boot ecosystem.
* **Handling scheduled background tasks** for automated monitoring.
* **Building environment-aware setups** for seamless transitions between demo and production.
* **Structuring clean MVC applications** using modern Java standards.

---

## Future Improvements

* **Add JWT-based authentication** for enhanced security.
* **Convert JDBC to Spring Data JPA** to reduce boilerplate code.
* **Improve error handling** with a global exception handling strategy (`@ControllerAdvice`).
* **Add Docker containerization** for easier deployment and scaling.
* **Deploy to cloud platforms** such as Render or Railway.

---

## Author

**Tanmoy Das** [Applied Computer Science] 
* **Karel de Grote University College**