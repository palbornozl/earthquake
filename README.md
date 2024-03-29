# Earthquake
Dada la siguiente API https://earthquake.usgs.gov/fdsnws/event/1/ 
la cual obtiene información sobre terremotos, 
se crea un servicio en Java utilizando Spring Boot 
con los siguientes endpoints, con contratos de entrada y salida formato json, 
donde cada endpoint deberá hacer uso la API mencionada para obtener la información
solicitada (llamar a API segun especificacion de la misma).

### Contratos entrada
- **/earthquake/authenticate**: Crear token según usuario y contraseña

*Contrato de entrada:*
```json
{
  "username": "admin",
  "password": "my5ecre7P@ssw0rd"
}
```
- **/earthquake/search/dates**: Consultar terremotos entre dos fechas y una magnitud mínima, debe agregar token creado como Bearer

*Contrato de entrada:*
```json
{
  "fechaInicio": "2020-01-01",
  "fechaFin": "2020-01-02",
  "magnitudeMinima": 6.5
}
```
- **/earthquake/search/magnitudes**: Consultar terremotos entre dos magnitudes, debe agregar token creado como Bearer

*Contrato de entrada*
```json
{
  "magnitudeMinima": 6.5,
  "magnitudeMaxima": 7.1
}
```
### Contrato salida
En ambos casos deberá retornar el siguiente contrato de salida:
```json
{
  "mag": 1.1,
  "place": "117km NW of Talkeetna, Alaska",
  "time": "Wednesday, January 1, 2014 11:47:26.501 PM",
  "updated": "Monday, May 20, 2019 10:45:30.681 PM",
  "alert": "green",
  "status": "reviewed",
  "tsunami": 0,
  "magType": "ml",
  "type": "earthquake",
  "title": "M 1.1 - 117km NW of Talkeetna, Alaska"
}
```
### Base de Datos
El resultado es almacenado en una base de datos relacional embebida (H2), cuya tabla debe ser:

*Schema*: earthquake
*Tabla*: earthquake

|nombre campo|observaciones|
|---|---|
|create_at|Llave primaria, fecha de insercion registro, valor por defecto|
|origen| POST / KAFKA clasificion de quien persiste|
|observacion|descripcion de el origen|
|fecha_inicio|Fecha inicio búsqueda|
|fecha_fin|Fecha fin búsqueda|
|magnitud_min|Magnitud minima busqueda|
|magnitud_max|Magnitud maxima busqueda|
|salida|Resultado busqueda (json)|

## Componentes
- SpringBoot
- H2 DB
- Kafka Producer/Consumer
- JWT Auth0

## Ejecución
Requisitos 
- jdk11
- maven 3.6.3
- docker / docker-compose
- Rest Client

#### Docker compose
Ejecutar comando:
`docker-compose -f docker-compose.yml up`

#### Rest Client
- Importar archivo restClientEarthquake.json
- Ejecutar token
- Para los otros rest se debe cambiar el token Bearer con el obtenido 

#### Acceso a BD
Vía browser http://localhost:8099/earthquake/h2-console
- JDBC URL: `jdbc:h2:mem:earthquake`
- User Name: `sa`
- Password: `password`

Para terminar se ejecuta:

`docker-compose -f docker-compose.yml stop earthquake kafka zookeeper`

