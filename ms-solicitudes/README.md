# ms-solicitudes - RutaLimpia

Solicitudes de retiro. Registra la solicitud al instante (folio) y la asignacion de camion se procesa de forma asincrona (worker que simula SQS + Lambda). Requiere ms-flota (8083) y ms-rutas (8084) para asignar.

## Requisitos
- Java 17
- Maven 3.9+
- MySQL 8 corriendo en `localhost:3306` (la BD `rutalimpia_solicitudes` se crea sola)

## Clonar y ejecutar
```bash
git clone <URL_DEL_REPOSITORIO>
cd ms-solicitudes

# (opcional) credenciales de BD; por defecto root / root
export DB_USER=root
export DB_PASSWORD=root

mvn clean install        # compila, ejecuta tests y empaqueta
mvn spring-boot:run      # levanta el servicio en http://localhost:8082
```

O ejecutando el JAR generado:
```bash
mvn clean package
java -jar target/ms-solicitudes-1.0.0.jar
```

MySQL rapido con Docker:
```bash
docker run -d --name mysql-rl -e MYSQL_ROOT_PASSWORD=root -p 3306:3306 mysql:8
```

## Documentacion
Swagger UI: http://localhost:8082/swagger-ui.html

## Endpoints
| Metodo | Ruta | Descripcion |
|---|---|---|
| POST | /solicitudes | Crear solicitud (devuelve folio, estado PENDIENTE) |
| GET | /solicitudes?usuarioId= | Listar solicitudes (del vecino) |
| GET | /solicitudes/folio/{folio} | Consultar por folio |
| PUT | /solicitudes/{id} | Modificar (solo PENDIENTE) |
| PUT | /solicitudes/{id}/estado | Cambiar estado |
| DELETE | /solicitudes/{id} | Cancelar (solo PENDIENTE) |
| GET/POST | /tipos-residuo | Listar / crear tipos de residuo |

## Estructura
```
controller/  -> API REST
service/     -> logica de negocio
repository/  -> acceso a datos (Spring Data JPA)
model/       -> entidades JPA
dto/         -> objetos de entrada/salida
exception/   -> manejo global de errores
config/      -> configuracion y datos iniciales
```

## Service discovery (Eureka)
Este servicio se registra en Eureka como `ms-solicitudes`. Levanta primero `eureka-server` (http://localhost:8761).
Para ejecutarlo sin Eureka: `export EUREKA_ENABLED=false`.
En produccion se accede a traves del `api-gateway` (http://localhost:8080).
