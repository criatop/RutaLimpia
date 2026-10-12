# ms-flota - RutaLimpia

Flota. Gestion de camiones, zonas y disponibilidad.

## Requisitos
- Java 17
- Maven 3.9+
- MySQL 8 corriendo en `localhost:3306` (la BD `rutalimpia_flota` se crea sola)

## Clonar y ejecutar
```bash
git clone <URL_DEL_REPOSITORIO>
cd ms-flota

# (opcional) credenciales de BD; por defecto root / root
export DB_USER=root
export DB_PASSWORD=root

mvn clean install        # compila, ejecuta tests y empaqueta
mvn spring-boot:run      # levanta el servicio en http://localhost:8083
```

O ejecutando el JAR generado:
```bash
mvn clean package
java -jar target/ms-flota-1.0.0.jar
```

MySQL rapido con Docker:
```bash
docker run -d --name mysql-rl -e MYSQL_ROOT_PASSWORD=root -p 3306:3306 mysql:8
```

## Documentacion
Swagger UI: http://localhost:8083/swagger-ui.html

## Endpoints
| Metodo | Ruta | Descripcion |
|---|---|---|
| POST | /camiones | Crear camion |
| GET | /camiones | Listar camiones |
| GET | /camiones/disponibles | Listar camiones disponibles |
| GET | /camiones/{id} | Obtener camion |
| PUT | /camiones/{id} | Actualizar camion |
| PUT | /camiones/{id}/disponibilidad | Cambiar disponibilidad |
| PUT | /camiones/{id}/asignar-parada | Reservar una parada (uso interno) |
| DELETE | /camiones/{id} | Eliminar camion |
| GET/POST | /zonas | Listar / crear zonas |

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
Este servicio se registra en Eureka como `ms-flota`. Levanta primero `eureka-server` (http://localhost:8761).
Para ejecutarlo sin Eureka: `export EUREKA_ENABLED=false`.
En produccion se accede a traves del `api-gateway` (http://localhost:8080).
