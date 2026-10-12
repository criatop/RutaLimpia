# ms-auth - RutaLimpia

Autenticacion y usuarios. Registro, login con JWT y gestion de roles (VECINO / CONDUCTOR).

## Requisitos
- Java 17
- Maven 3.9+
- MySQL 8 corriendo en `localhost:3306` (la BD `rutalimpia_auth` se crea sola)

## Clonar y ejecutar
```bash
git clone <URL_DEL_REPOSITORIO>
cd ms-auth

# (opcional) credenciales de BD; por defecto root / root
export DB_USER=root
export DB_PASSWORD=root

mvn clean install        # compila, ejecuta tests y empaqueta
mvn spring-boot:run      # levanta el servicio en http://localhost:8081
```

O ejecutando el JAR generado:
```bash
mvn clean package
java -jar target/ms-auth-1.0.0.jar
```

MySQL rapido con Docker:
```bash
docker run -d --name mysql-rl -e MYSQL_ROOT_PASSWORD=root -p 3306:3306 mysql:8
```

## Documentacion
Swagger UI: http://localhost:8081/swagger-ui.html

## Endpoints
| Metodo | Ruta | Descripcion |
|---|---|---|
| POST | /auth/register | Registrar usuario (VECINO o CONDUCTOR) |
| POST | /auth/login | Login, devuelve JWT |
| GET | /auth/validate | Valida el token (header Authorization: Bearer) |
| GET | /usuarios | Listar usuarios |
| GET | /usuarios/{id} | Obtener usuario |
| PUT | /usuarios/{id} | Actualizar usuario |
| DELETE | /usuarios/{id} | Eliminar usuario |

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
Este servicio se registra en Eureka como `ms-auth`. Levanta primero `eureka-server` (http://localhost:8761).
Para ejecutarlo sin Eureka: `export EUREKA_ENABLED=false`.
En produccion se accede a traves del `api-gateway` (http://localhost:8080).
