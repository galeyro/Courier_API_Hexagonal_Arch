# Courier API

API de gestión de envíos construida con **Spring Boot 3**, **PostgreSQL** y **Kafka**, aplicando **arquitectura hexagonal**, **Strategy** para las modalidades de envío y **Observer** mediante eventos.

## Stack elegido

- **Spring Boot + JPA**: acelera la construcción de una API robusta con validación, persistencia y configuración madura.
- **PostgreSQL**: cumple el requisito de persistencia real y permite almacenar `metadata` como `jsonb`.
- **Kafka**: encaja muy bien con el patrón Observer y desacopla notificaciones y auditoría.
- **Springdoc OpenAPI**: publica Swagger en `/api/docs`.

## Arquitectura

La solución está organizada por módulos:

- `customers`
- `shipments`
- `shared/events`
- `notifications`

Cada módulo sigue un enfoque hexagonal:

- `domain/`: modelos, excepciones y puertos
- `application/`: casos de uso, DTOs y estrategias
- `infrastructure/`: REST, persistencia, Kafka y configuración

## Patrones aplicados

- **Strategy**:
  - `ShippingStrategyPort`
  - `StandardShippingStrategy`
  - `ExpressShippingStrategy`
  - `InternationalShippingStrategy`
  - `ThirdPartyCarrierShippingStrategy`
- **Observer**:
  - `EventPublisher` como puerto
  - `KafkaEventPublisher` como adaptador
  - dos consumers independientes:
    - `notifications-consumer`
    - `audit-consumer`

## Requisitos cubiertos

- Persistencia real con PostgreSQL
- `metadata` almacenado como `jsonb`
- Mapper pattern entre dominio y entidades JPA
- DTOs con Jakarta Validation
- Excepciones de dominio mapeadas a HTTP
- Swagger disponible en `/api/docs`
- `docker-compose.yml` con PostgreSQL y Kafka

## Cómo levantar el proyecto

### 1. Levantar infraestructura

```bash
docker compose up -d
```

Servicios:

- PostgreSQL: `localhost:5432`
- Kafka: `localhost:9092`
- Kafka UI: `http://localhost:8090`

### 2. Ejecutar la aplicación

En Windows PowerShell:

```powershell
$env:JAVA_HOME="C:\Program Files\Java\jdk-23"
$env:Path="$env:JAVA_HOME\bin;$env:Path"
.\mvnw.cmd spring-boot:run
```

Si el wrapper falla en tu entorno, puedes usar Maven instalado:

```powershell
cmd /c "set JAVA_HOME=C:\Program Files\Java\jdk-23&& set PATH=%JAVA_HOME%\bin;%PATH%&& C:\workspace_maven\apache-maven-3.9.11\bin\mvn.cmd spring-boot:run"
```

### 3. URLs importantes

- Swagger UI: `http://localhost:8080/api/docs`
- OpenAPI JSON: `http://localhost:8080/api/openapi`
- Kafka UI: `http://localhost:8090`

## Variables de entorno

Revisa `.env.example`. Las principales son:

- `POSTGRES_DB`
- `POSTGRES_USER`
- `POSTGRES_PASSWORD`
- `SPRING_DATASOURCE_URL`
- `SPRING_KAFKA_BOOTSTRAP_SERVERS`

## Endpoints mínimos

### Customers

- `POST /api/customers`
- `GET /api/customers`
- `GET /api/customers/{id}`
- `PATCH /api/customers/{id}`
- `DELETE /api/customers/{id}`

### Shipments

- `POST /api/shipments`
- `GET /api/shipments/{id}`
- `GET /api/shipments/customer/{id}`

## Eventos publicados

- `shipment.dispatched` para `DELIVERED`
- `shipment.in_customs` para `IN_CUSTOMS`
- `shipment.failed` para `FAILED`

Los consumers registran:

- notificación estructurada
- auditoría con `topic`, `offset`, `shipmentId` y `timestamp`

## Ejemplos rápidos

Hay una colección manual en [requests.http](./requests.http) con:

- creación de clientes
- las 4 estrategias
- un caso de error `EXPRESS` con `weightKg=10`

## Tests

Ejecutar:

```powershell
cmd /c "set JAVA_HOME=C:\Program Files\Java\jdk-23&& set PATH=%JAVA_HOME%\bin;%PATH%&& C:\workspace_maven\apache-maven-3.9.11\bin\mvn.cmd test"
```

Actualmente hay pruebas unitarias para las cuatro estrategias de envío.
