# E-Commerce Microservices Platform

A scalable, event-driven microservices architecture for an e-commerce platform built with Spring Boot and Spring Cloud.

## 🚀 Features

- **Product Service**: Product catalog management with MongoDB
- **Inventory Service**: Real-time inventory tracking with MySQL
- **Order Service**: Order processing with event-driven architecture
- **Notification Service**: Email notifications for order updates
- **API Gateway**: Single entry point with request routing and load balancing
- **Event-Driven Architecture**: Apache Kafka for asynchronous communication
- **Containerized**: Docker and Docker Compose for easy deployment
- **API Documentation**: Integrated Swagger UI for all services

## 🛠️ Tech Stack

- **Java 21**
- **Spring Boot 3.3.12**
- **Spring Cloud 2023.0.5**
- **Spring Data MongoDB & JPA**
- **Spring Kafka**
- **Docker & Docker Compose**
- **MySQL 8.3.0**
- **MongoDB 7.0.5**
- **Apache Kafka 7.5.0**
- **OpenAPI 3.0**

## 📦 Prerequisites

- Java 21 or later
- Docker Desktop (with Docker Compose)
- Maven 3.9.x or later
- Git

## 🚀 Quick Start

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd spring-boot-microservices
   ```

2. **Build the project**
   ```bash
   mvn clean install
   ```

3. **Start the services**
   ```bash
   docker-compose up -d
   ```
   This will start all services and required infrastructure (Kafka, MySQL, MongoDB, etc.)

4. **Verify services are running**
   ```bash
   docker-compose ps
   ```

## 🌐 Access Services

| Service | URL | Port |
|---------|-----|------|
| API Gateway | http://localhost:8080 | 8080 |
| Product Service | http://localhost:8081 | 8081 |
| Order Service | http://localhost:8083 | 8083 |
| Inventory Service | http://localhost:8082 | 8082 |
| Notification Service | http://localhost:8084 | 8084 |
| Kafka UI | http://localhost:8086 | 8086 |

## 📚 API Documentation

Access Swagger UI for API documentation:

- **API Gateway (All Services)**: http://localhost:8080/swagger-ui.html
- **Product Service**: http://localhost:8081/swagger-ui.html
- **Order Service**: http://localhost:8083/swagger-ui.html
- **Inventory Service**: http://localhost:8082/swagger-ui.html

## 🧪 Running Tests

To run tests for all services:

```bash
mvn test
```

For individual service tests, navigate to the service directory and run:

```bash
cd <service-directory>
mvn test
```

## 🧩 Project Structure

```
.
├── api-gateway/           # API Gateway service
├── product-service/       # Product management service
├── order-service/         # Order processing service
├── inventory-service/     # Inventory management service
├── notification-service/  # Notification service
├── docker-compose.yml     # Docker Compose configuration
└── README.md             # This file
```

## 🔄 Service Communication

- **Synchronous**: REST APIs (HTTP/HTTPS)
- **Asynchronous**: Apache Kafka for event-driven communication

## 🔒 Environment Variables

Each service has its own configuration in `application.yml`. For local development, you can override settings using environment variables in `docker-compose.yml`.

## 🐛 Debugging

To debug a specific service:

1. Stop the service in Docker:
   ```bash
   docker-compose stop <service-name>
   ```

2. Run the service locally with debug mode:
   ```bash
   cd <service-directory>
   mvn spring-boot:run -Dspring-boot.run.jvmArguments="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005"
   ```

3. Attach your IDE's debugger to port 5005

## 🧹 Clean Up

To stop and remove all containers, networks, and volumes:

```bash
docker-compose down -v
```

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📜 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- Built with ❤️ using Spring Boot and Spring Cloud
- Thanks to all open-source projects used in this project
