# Courier API - Endpoints para Postman

**Base URL:** `http://localhost:8080`

---

## 📦 CUSTOMERS

### 1. Crear Cliente
**POST** `/api/customers`

**Request Body:**
```json
{
  "name": "Juan Pérez",
  "email": "juan@example.com",
  "password": "SecurePass123",
  "role": "USER"
}
```

**Response (201 Created):**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "name": "Juan Pérez",
  "email": "juan@example.com",
  "role": "USER",
  "active": true,
  "createdAt": "2026-04-28T10:30:00",
  "updatedAt": "2026-04-28T10:30:00"
}
```

---

### 2. Listar Todos los Clientes
**GET** `/api/customers`

**Response (200 OK):**
```json
[
  {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "name": "Juan Pérez",
    "email": "juan@example.com",
    "role": "USER",
    "active": true,
    "createdAt": "2026-04-28T10:30:00",
    "updatedAt": "2026-04-28T10:30:00"
  },
  {
    "id": "660e8400-e29b-41d4-a716-446655440001",
    "name": "María López",
    "email": "maria@example.com",
    "role": "SENDER",
    "active": true,
    "createdAt": "2026-04-28T11:00:00",
    "updatedAt": "2026-04-28T11:00:00"
  }
]
```

---

### 3. Obtener Cliente por ID
**GET** `/api/customers/{id}`

**Parámetros:**
- `id` (UUID): ID del cliente

**Ejemplo:** `GET /api/customers/550e8400-e29b-41d4-a716-446655440000`

**Response (200 OK):**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "name": "Juan Pérez",
  "email": "juan@example.com",
  "role": "USER",
  "active": true,
  "createdAt": "2026-04-28T10:30:00",
  "updatedAt": "2026-04-28T10:30:00"
}
```

---

### 4. Actualizar Cliente
**PATCH** `/api/customers/{id}`

**Parámetros:**
- `id` (UUID): ID del cliente

**Request Body:**
```json
{
  "name": "Juan Carlos Pérez",
  "role": "ADMIN"
}
```

**Response (200 OK):**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "name": "Juan Carlos Pérez",
  "email": "juan@example.com",
  "role": "ADMIN",
  "active": true,
  "createdAt": "2026-04-28T10:30:00",
  "updatedAt": "2026-04-28T11:45:00"
}
```

---

### 5. Desactivar Cliente (Soft Delete)
**DELETE** `/api/customers/{id}`

**Parámetros:**
- `id` (UUID): ID del cliente

**Ejemplo:** `DELETE /api/customers/550e8400-e29b-41d4-a716-446655440000`

**Response (204 No Content)**

---

## 🚚 SHIPMENTS

### 1. Crear Envío
**POST** `/api/shipments`

⚠️ **Campos requeridos en metadata según el tipo:**

| Tipo | Campos Requeridos | Restricciones |
|------|------|---|
| `STANDARD` | `weightKg` | peso ≤ 20 kg |
| `EXPRESS` | `weightKg` | peso ≤ 5 kg, declaredValue ≤ 3,000,000 |
| `INTERNATIONAL` | `destinationCountry`, `customsDeclaration` | declaredValue ≤ 50,000,000 |
| `THIRD_PARTY_CARRIER` | `carrierName`, `externalTrackingId` | ninguna |

**Request Body (STANDARD):**
```json
{
  "senderId": "550e8400-e29b-41d4-a716-446655440000",
  "recipientId": "660e8400-e29b-41d4-a716-446655440001",
  "declaredValue": 50.00,
  "type": "STANDARD",
  "metadata": {
    "description": "Paquete frágil",
    "weightKg": 2.5,
    "dimensions": "20x15x10 cm"
  }
}
```

**Request Body (EXPRESS):**
```json
{
  "senderId": "550e8400-e29b-41d4-a716-446655440000",
  "recipientId": "660e8400-e29b-41d4-a716-446655440001",
  "declaredValue": 30.00,
  "type": "EXPRESS",
  "metadata": {
    "description": "Paquete urgente",
    "weightKg": 1.5
  }
}
```

**Request Body (INTERNATIONAL):**
```json
{
  "senderId": "550e8400-e29b-41d4-a716-446655440000",
  "recipientId": "660e8400-e29b-41d4-a716-446655440001",
  "declaredValue": 100.00,
  "type": "INTERNATIONAL",
  "metadata": {
    "description": "Envío internacional",
    "destinationCountry": "España",
    "customsDeclaration": "Contenido comercial"
  }
}
```

**Request Body (THIRD_PARTY_CARRIER):**
```json
{
  "senderId": "550e8400-e29b-41d4-a716-446655440000",
  "recipientId": "660e8400-e29b-41d4-a716-446655440001",
  "declaredValue": 75.00,
  "type": "THIRD_PARTY_CARRIER",
  "metadata": {
    "description": "Envío por tercero",
    "carrierName": "DHL",
    "externalTrackingId": "DHL123456"
  }
}
```

**Response (201 Created):**
```json
{
  "id": "770e8400-e29b-41d4-a716-446655440002",
  "senderId": "550e8400-e29b-41d4-a716-446655440000",
  "recipientId": "660e8400-e29b-41d4-a716-446655440001",
  "declaredValue": 50.00,
  "shippingCost": 15.99,
  "type": "STANDARD",
  "status": "PENDING",
  "metadata": {
    "description": "Paquete frágil",
    "weight": 2.5,
    "dimensions": "20x15x10 cm"
  },
  "createdAt": "2026-04-28T12:00:00",
  "updatedAt": "2026-04-28T12:00:00"
}
```

---

### 2. Obtener Envío por ID
**GET** `/api/shipments/{id}`

**Parámetros:**
- `id` (UUID): ID del envío

**Ejemplo:** `GET /api/shipments/770e8400-e29b-41d4-a716-446655440002`

**Response (200 OK):**
```json
{
  "id": "770e8400-e29b-41d4-a716-446655440002",
  "senderId": "550e8400-e29b-41d4-a716-446655440000",
  "recipientId": "660e8400-e29b-41d4-a716-446655440001",
  "declaredValue": 50.00,
  "shippingCost": 15.99,
  "type": "STANDARD",
  "status": "PENDING",
  "metadata": {
    "description": "Paquete frágil",
    "weight": 2.5,
    "dimensions": "20x15x10 cm"
  },
  "createdAt": "2026-04-28T12:00:00",
  "updatedAt": "2026-04-28T12:00:00"
}
```

---

### 3. Listar Envíos de un Cliente
**GET** `/api/shipments/customer/{id}`

**Parámetros:**
- `id` (UUID): ID del cliente (remitente o destinatario)

**Ejemplo:** `GET /api/shipments/customer/550e8400-e29b-41d4-a716-446655440000`

**Response (200 OK):**
```json
[
  {
    "id": "770e8400-e29b-41d4-a716-446655440002",
    "senderId": "550e8400-e29b-41d4-a716-446655440000",
    "recipientId": "660e8400-e29b-41d4-a716-446655440001",
    "declaredValue": 50.00,
    "shippingCost": 15.99,
    "type": "STANDARD",
    "status": "PENDING",
    "metadata": {
      "description": "Paquete frágil",
      "weight": 2.5,
      "dimensions": "20x15x10 cm"
    },
    "createdAt": "2026-04-28T12:00:00",
    "updatedAt": "2026-04-28T12:00:00"
  }
]
```

---

## 📋 Enums de Referencia

### CustomerRole
- `USER`
- `ADMIN`
- `CARRIER`

### ShipmentType
- `STANDARD`
- `EXPRESS`
- `INTERNATIONAL`
- `THIRD_PARTY_CARRIER`

### ShipmentStatus
- `PENDING`
- `DELIVERED`
- `IN_CUSTOMS`
- `FAILED`

---

## 🔧 Orden Recomendado para Pruebas

1. **Crear 2 clientes** (POST `/api/customers`) - Guarda los IDs
2. **Listar clientes** (GET `/api/customers`)
3. **Obtener cliente específico** (GET `/api/customers/{id}`)
4. **Actualizar cliente** (PATCH `/api/customers/{id}`)
5. **Crear envío** (POST `/api/shipments`) - Usa los IDs de los clientes creados
6. **Obtener envío** (GET `/api/shipments/{id}`)
7. **Listar envíos de cliente** (GET `/api/shipments/customer/{id}`)
8. **Desactivar cliente** (DELETE `/api/customers/{id}`)

---

## 📌 Notas Importantes

- Todos los **UUID** se generan automáticamente en el servidor.
- El **password** NO se devuelve en las respuestas por seguridad.
- El **shippingCost** se calcula automáticamente según la estrategia de precios.
- Los **timestamps** se generan automáticamente (createdAt, updatedAt).
- El **metadata** es flexible y acepta cualquier estructura JSON.

---

## 🌐 Swagger UI

Puedes explorar los endpoints interactivamente:
**URL:** `http://localhost:8080/api/docs`

También disponibles:
- OpenAPI JSON: `http://localhost:8080/api/openapi`

