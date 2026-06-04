Courier API
===========

API de gestion de envios construida con **Spring Boot 3**, **PostgreSQL** y **Kafka**, aplicando **arquitectura hexagonal**, **Strategy** para las modalidades de envio y **Observer** mediante eventos.

Modulos
-------

- **customers** - Gestion de clientes (CRUD, activacion/desactivacion)
- **shipments** - Gestion de envios (creacion con estrategia, consulta)
- **shared/events** - Publicacion de eventos via Kafka
- **notifications** - Consumidores de eventos para notificaciones y auditoria

Herramientas de calidad
-----------------------

| Herramienta   | Fase      | Proposito                          |
|---------------|-----------|------------------------------------|
| Checkstyle    | validate  | Estilo y formato de codigo        |
| PMD           | validate  | Calidad de codigo y bugs potenciales |
| SpotBugs      | verify    | Deteccion de bugs en bytecode      |
| JaCoCo        | test      | Cobertura de codigo                |
| Surefire      | test      | Ejecucion de tests unitarios       |

Endpoints
---------

### Customers

- `POST /api/customers` - Crear cliente
- `GET /api/customers` - Listar clientes
- `GET /api/customers/{id}` - Buscar cliente por ID
- `PATCH /api/customers/{id}` - Actualizar cliente
- `DELETE /api/customers/{id}` - Desactivar cliente

### Shipments

- `POST /api/shipments` - Crear envio
- `GET /api/shipments/{id}` - Buscar envio por ID
- `GET /api/shipments/customer/{id}` - Buscar envios por cliente

Estrategias de envio
--------------------

| Estrategia            | Costo                              | Estado resultante |
|-----------------------|------------------------------------|--------------------|
| STANDARD              | max(declaredValue*0.001, 5000)     | DELIVERED          |
| EXPRESS               | 15000                              | DELIVERED          |
| INTERNATIONAL         | 50000 + declaredValue*0.02         | IN_CUSTOMS         |
| THIRD_PARTY_CARRIER   | declaredValue*0.05                 | DELIVERED          |