# Redbee Academy - Testing

Ejemplos de tests utilizando mocks, bases de datos en memoria (H2) y Testcontainers.

La aplicación es muy simple. Al ejecutarla (`./gradlew run`), se conecta a la base de datos y da de alta el cliente
`GUERR` si no existe.


## Comenzar

### Iniciar base de datos

```sh
docker-compose up -d
```

### Detener base de datos

```sh
docker-compose stop
```

### Eliminar base de datos

```sh
docker-compose down
```


## Ejercicio

Resta completar uno de los tests (`testSuccessfullyCreateCustomer`) en `CustomerDAOTestMock`. ¿Te animás a hacerlo?


## Referencias

- Tecnologías:
    - [Mockito](https://site.mockito.org/)
    - [H2 Database](https://www.h2database.com/html/features.html#in_memory_databases)
    - [Testcontainers](https://www.testcontainers.org/)
- Conceptos:
    - [Tests unitarios](https://martinfowler.com/bliki/UnitTest.html)
    - [XUnit](https://martinfowler.com/bliki/Xunit.html)
    - [Tests de integración](https://martinfowler.com/bliki/IntegrationTest.html)
    - [La pirámide del testing](https://martinfowler.com/bliki/TestPyramid.html)
    - [Mocks](https://martinfowler.com/articles/mocksArentStubs.html)
