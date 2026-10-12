# eureka-server - RutaLimpia

Servidor de descubrimiento de servicios (Netflix Eureka). Los microservicios y el API Gateway se registran aqui
y se encuentran por nombre (`ms-auth`, `ms-solicitudes`, `ms-flota`, `ms-rutas`).

## Requisitos
- Java 17, Maven 3.9+

## Clonar y ejecutar
```bash
git clone <URL_DEL_REPOSITORIO>
cd eureka-server
mvn clean install
mvn spring-boot:run        # http://localhost:8761
```
O con el JAR: `mvn clean package && java -jar target/eureka-server-1.0.0.jar`

**Debe levantarse primero**, antes que cualquier otro servicio.

## Dashboard
http://localhost:8761 muestra los servicios registrados (pueden tardar ~30 s en aparecer).
