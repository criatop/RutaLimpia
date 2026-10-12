# api-gateway - RutaLimpia

Puerta de entrada unica (Spring Cloud Gateway). Simula **Amazon API Gateway** en el entorno local:
los clientes llaman solo al puerto 8080 y el gateway enruta a cada microservicio por nombre, consultando a Eureka.

## Requisitos
- Java 17, Maven 3.9+
- `eureka-server` y los microservicios en ejecucion

## Clonar y ejecutar
```bash
git clone <URL_DEL_REPOSITORIO>
cd api-gateway
mvn clean install
mvn spring-boot:run        # http://localhost:8080
```
O con el JAR: `mvn clean package && java -jar target/api-gateway-1.0.0.jar`

## Rutas (src/main/resources/application.yml)
| Path | Servicio |
|---|---|
| /auth/**, /usuarios/** | ms-auth |
| /solicitudes/**, /tipos-residuo/** | ms-solicitudes |
| /camiones/**, /zonas/** | ms-flota |
| /rutas/** | ms-rutas |

Ejemplo: `POST http://localhost:8080/solicitudes` llega a `ms-solicitudes` sin conocer su puerto.
