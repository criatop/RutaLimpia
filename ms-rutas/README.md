# ms-rutas - RutaLimpia

Rutas. Hojas de ruta por camion y paradas; el conductor consulta y registra el resultado de cada retiro.

## Requisitos
- Java 17
- Maven 3.9+
- MySQL 8 corriendo en `localhost:3306` (la BD `rutalimpia_rutas` se crea sola)

## Clonar y ejecutar
```bash
git clone <URL_DEL_REPOSITORIO>
cd ms-rutas

# (opcional) credenciales de BD; por defecto root / root
export DB_USER=root
export DB_PASSWORD=root

mvn clean install        # compila, ejecuta tests y empaqueta
mvn spring-boot:run      # levanta el servicio en http://localhost:8084
```

O ejecutando el JAR generado:
```bash
mvn clean package
java -jar target/ms-rutas-1.0.0.jar
```

MySQL rapido con Docker:
```bash
docker run -d --name mysql-rl -e MYSQL_ROOT_PASSWORD=root -p 3306:3306 mysql:8
```

## Documentacion
Swagger UI: http://localhost:8084/swagger-ui.html

## Endpoints
| Metodo | Ruta | Descripcion |
|---|---|---|
| POST | /rutas/paradas | Agregar parada a la hoja de ruta (uso interno) |
| GET | /rutas/camion/{camionId}?fecha=YYYY-MM-DD | Hoja de ruta del camion |
| GET | /rutas/paradas/{id} | Obtener parada |
| PUT | /rutas/paradas/{id}/resultado | Registrar REALIZADO / FALLIDO |
| DELETE | /rutas/paradas/{id} | Eliminar parada |

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
Este servicio se registra en Eureka como `ms-rutas`. Levanta primero `eureka-server` (http://localhost:8761).
Para ejecutarlo sin Eureka: `export EUREKA_ENABLED=false`.
En produccion se accede a traves del `api-gateway` (http://localhost:8080).
