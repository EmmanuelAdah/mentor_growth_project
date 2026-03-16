# Mentor Growth Platform

## Overview

**Mentor Growth** is a scalable mentorship platform designed to connect mentors and mentees through structured learning, professional guidance, and collaborative engagement. The platform allows mentees to discover mentors, schedule mentorship sessions, communicate in real time, and make secure payments for mentorship services.

Mentors can create detailed profiles, set pricing for sessions, manage availability, interact with mentees, and track earnings through an integrated payment system.

The platform is built with **modern backend architecture**, emphasizing scalability, performance, and security. It leverages **real-time communication, distributed messaging, caching, server-side rendering, and cloud infrastructure** to provide a reliable mentorship ecosystem.

---

# Core Features

## User Management

* Secure user registration and authentication
* Role-based access control (Mentor / Mentee)
* Profile management and updates
* Secure password hashing and credential storage

---

## Mentor Profiles

Mentors can build professional profiles that include:

* Areas of expertise
* Session pricing
* Ratings and reviews
* Earnings tracking
* Availability management

This enables mentees to easily discover mentors based on skills and availability.

---

## Mentorship Session Management

* Session scheduling between mentors and mentees
* Session lifecycle management
* Availability tracking
* Calendar-based booking integration

---

## Real-Time Communication

The platform supports **real-time messaging using WebSockets**, enabling mentors and mentees to communicate instantly.

Features include:

* Live messaging between users
* Instant session notifications
* Real-time system events
* Low-latency communication channels

WebSocket communication is implemented using **Spring WebSocket with STOMP messaging protocol**.

---

## Server-Side Rendering (SSR)

The platform incorporates **Server-Side Rendering (SSR)** to improve performance, SEO, and initial page load speed for the frontend application.

SSR allows the server to generate fully rendered HTML before sending it to the client, which provides several benefits:

* Faster initial page loads
* Improved SEO for discoverable mentor profiles
* Better performance on slower networks
* Enhanced accessibility for search engine crawlers

Server-Side Rendering (SSR) with Thymeleaf

The backend APIs serve data that is rendered on the server before the page is delivered to the browser.

---

## Payment Processing

Secure payment processing is integrated to allow mentees to pay mentors for mentorship sessions.

Supported payment gateways:

* **Paystack**
* **Flutterwave**

Features include:

* Secure payment transactions
* Payment verification through webhooks
* Transaction history tracking
* Mentor earnings management

---

## Caching with Redis

The system uses **Redis** as a high-performance caching layer to improve response times and reduce database load.

Redis is used for:

* Caching frequently accessed mentor profiles
* Temporary payment verification storage
* Session booking state management
* Token and authentication caching
* Reducing database query overhead

---

## Asynchronous Processing with RabbitMQ

The application uses **RabbitMQ** for asynchronous messaging and background processing.

Common use cases include:

* Payment webhook processing
* Notification delivery
* Background job execution
* Event-driven system updates

This architecture ensures that heavy tasks do not block user-facing operations.

---

# Technology Stack

## Backend

* **Java**
* **Spring Boot**
* **Spring Security**
* **Spring Data JPA**
* **Spring WebFlux**
* **Spring WebSocket**

---

## Frontend

* **React / Next.js** (SSR capable)
* **Server-Side Rendering (SSR)**

---

## Database

* **PostgreSQL**

---

## Messaging

* **RabbitMQ**

---

## Caching

* **Redis**

---

## Authentication & Security

* **JWT (JSON Web Token)**
* **OAuth 2.0**
* **Spring Security**

---

## Payment Gateways

* **Paystack**
* **Flutterwave**

---

## Cloud Infrastructure

* **AWS EC2** – Application hosting
* **AWS RDS (PostgreSQL)** – Managed database
* **AWS ElastiCache (Redis)** – Redis caching
* **AWS S3** – File and media storage
* **AWS CloudWatch** – Monitoring and logging

---

# System Architecture

The application follows a **layered backend architecture** designed for maintainability and scalability.

```id="d6b5hp"

Client Applications (Web / Mobile)
        │
        ▼
SSR Frontend (React.js / Next.js / Angular Universal)
        │
        ▼
API Gateway / Controllers
        │
        ▼
Service Layer (Business Logic)
        │
        ▼
Repository Layer (Data Access)
        │
        ▼
PostgreSQL Database
```

Supporting infrastructure:

* **Redis** → caching layer
* **RabbitMQ** → asynchronous messaging
* **WebSocket Gateway** → real-time communication
* **Payment APIs** → transaction processing

---

# Database Design

The system uses **JPA entity inheritance with the JOINED strategy** to maintain normalized relational tables.

```id="sypgze"
users
 ├── id
 ├── email
 ├── password
 ├── role
 ├── phoneNumber
 ├── bio
 ├── profession
 ├── profileImage
 ├── isApproved
 ├── created_at

mentor
 ├── id (FK → users.id)
 ├── pricing
 ├── rating
 ├── earnings
 ├── services []

mentee
 ├── id (FK → users.id)
 ├── careerStage
 ├── rating
 ├── earnings
```

This design ensures authentication data remains centralized while mentor-specific fields remain modular.

---

# Authentication Flow

1. User registers using email and password.
2. Passwords are securely hashed using Spring Security.
3. User logs in and receives a **JWT access token**.
4. The token is included in API requests for authentication and authorization.

---

# Payment Workflow

1. Mentee initiates payment for a mentorship session.
2. Payment is processed through **Paystack or Flutterwave**.
3. The payment provider sends a **webhook callback**.
4. The webhook is verified by the backend.
5. Transaction status is stored and the mentorship session is confirmed.

Redis may temporarily store pending transactions during verification.

---

# WebSocket Communication Flow

1. Client establishes WebSocket connection.
2. Authentication token is validated.
3. Messages are sent via STOMP channels.
4. Subscribed clients receive updates instantly.

This enables real-time chat and system notifications.

---

# Caching Strategy

Redis caching improves performance by:

* Reducing repeated database queries
* Storing frequently requested mentor profiles
* Managing session availability
* Improving API response speed

---

# AWS Deployment

The platform is designed for **cloud-native deployment using AWS infrastructure**.

Typical deployment architecture includes:

* **AWS EC2** → Application servers
* **AWS RDS (PostgreSQL)** → Managed relational database
* **AWS ElastiCache (Redis)** → In-memory caching
* **AWS S3** → Storage for files and assets
* **AWS CloudWatch** → Monitoring and logging

Containerized deployment using **Docker** ensures consistent environments across development, staging, and production.

---

# Project Structure

```id="o3ow0t"
mentor-growth
 ├── controllers
 ├── services
 ├── repositories
 ├── models
 ├── dtos
 ├── security
 ├── configs
 ├── messaging
 ├── websocket
 ├── caching
 ├── exceptions
 └── utils
```

---

# Running the Project

## Prerequisites

Ensure the following tools are installed:

* Java 17+
* Maven
* PostgreSQL
* Redis
* RabbitMQ
* Docker (optional)

---

## Clone the Repository

```id="r91a5m"
git clone https://github.com/EmmanuelAdah/mentor_growth_project.git
cd mentor-growth
```

---

## Configure Environment

Update the `application.properties` file:

```id="n8v8og"
spring.datasource.url=jdbc:postgresql://localhost:5432/mentor_growth
spring.datasource.username=your_username
spring.datasource.password=your_password

spring.redis.host=localhost
spring.redis.port=6379

spring.rabbitmq.host=localhost
spring.rabbitmq.port=5672
```

---

## Run the Application

```id="ut14t2"
mvn spring-boot:run
```

The application will start at:

```id="b5uhkp"
http://localhost:8080
```

---

# Future Improvements

Planned improvements include:

* AI-powered mentor recommendation system
* Integrated video conferencing for mentorship sessions
* Mobile application support
* Mentor analytics dashboard
* Advanced scheduling and availability algorithms

---

# Contribution Guidelines

Contributions are welcome.

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to your branch
5. Submit a pull request

---

# License

This project is licensed under the MIT License.

---

# Author

**Emmanuel Adah**

Backend Engineer specializing in scalable backend systems, distributed architecture, and secure API design.

---
