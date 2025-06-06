# Resources Enefit Service

A Spring Boot REST API for managing resources, their locations, and characteristics — with Kafka notifications.

---

## Setup Instructions

### Prerequisites

- Java 21
- Maven
- Docker & Docker Compose

---

## Step 1: Start Services with Docker Compose

This project includes a `compose.yaml` file. To start the necessary services (Kafka, Zookeeper, PostgreSQL), run:

```bash
docker compose up -d
```

To stop the services:

```bash
docker compose down
```

---

## Step 2: Application Configuration

The app is configured using `application.properties`. The Kafka and database settings should match those in your `compose.yaml`.

Make sure the ports and credentials align with your local setup.

---

## Step 3: Run the Application

Build the project:

```bash
mvn clean install
```

Run the Spring Boot app:

```bash
mvn spring-boot:run
```

Or launch it via your IDE.

When the application starts, Flyway will automatically manage and apply database migrations, creating the necessary tables and schema in PostgreSQL.

---

## Kafka Notifications

The app sends resource updates to a Kafka topic named `resource-updates`.

### Automatic Trigger

Updating a resource via:

```http
PUT /api/resources/{id}
```

will produce a message to Kafka with the updated `ResourceDto`.

### Manual Trigger

To send all resources to Kafka manually:

```http
POST /api/resources/notify-all
```

---

## API Documentation

You can also use [OpenApi docs](http://localhost:8080/swagger-ui/index.html#/)

### Create Resource

```http
POST /api/resources
Content-Type: application/json
```

Example JSON:

```json
{
  "type": "METERING_POINT",
  "countryCode": "EE",
  "location": {
    "streetAddress": "Tartu mnt 1",
    "city": "Tallinn",
    "postalCode": "12345",
    "countryCode": "EE"
  },
  "characteristics": [
    {
      "code": "C007",
      "type": "CONSUMPTION_TYPE",
      "value": "Industrial"
    },
    {
      "code": "C008",
      "type": "CHARGING_POINT",
      "value": "Fast"
    }
  ]
}
```

### Get All Resources

```http
GET /api/resources
```

### Get Resource by ID

```http
GET /api/resources/{id}
```

### Update Resource

```http
PUT /api/resources/{id}
Content-Type: application/json
```

Example JSON:

```json
{
  "type": "CONNECTION_POINT",
  "countryCode": "EE",
  "location": {
    "streetAddress": "Tartu mnt 2",
    "city": "Tallinn",
    "postalCode": "12345",
    "countryCode": "EE"
  },
  "characteristics": [
    {
      "code": "C007",
      "type": "CONSUMPTION_TYPE",
      "value": "Industrial"
    },
    {
      "code": "C008",
      "type": "CHARGING_POINT",
      "value": "Fast"
    }
  ]
}
```

### Delete Resource

```http
DELETE /api/resources/{id}
```

### Notify All Resources

```http
POST /api/resources/notify-all
```

---

## Run Tests

To run unit and integration tests:

```bash
mvn test
```

---

## Useful Dev Commands

### Rebuild and restart the app

```bash
mvn clean install && mvn spring-boot:run
```

### Stop all Docker containers

```bash
docker compose down
```
