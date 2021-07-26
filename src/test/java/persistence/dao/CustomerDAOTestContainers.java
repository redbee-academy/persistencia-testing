package persistence.dao;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Assertions;
import org.postgresql.ds.PGSimpleDataSource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import persistence.helper.DBHelper;
import persistence.helper.SQLReader;
import persistence.mock.CustomerMockFactory;

import javax.sql.DataSource;

@DisplayName("DAO tests using dockerized test-containers")
public class CustomerDAOTestContainers {

    private static final String PG_DOCKER_IMAGE = "postgres:11-alpine";
    private static final String DATABASE_NAME = "postgres";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "academy";

    private static final PostgreSQLContainer<?> CONTAINER =
            new PostgreSQLContainer<>(PG_DOCKER_IMAGE)
                    .withDatabaseName(DATABASE_NAME).withUsername(USERNAME).withPassword(PASSWORD)
                    .withExposedPorts(5432)
                    .waitingFor(
                            Wait.forLogMessage(".*(database system is ready to accept connections).*", 2)
                    );

    private static DataSource DS;
    private static DBHelper DB_HELPER;
    private static final String CUSTOMER_ID = "GUERR";

    @BeforeAll
    static void setUp() {
        CONTAINER.start();

        final var pgDs = new PGSimpleDataSource();
        pgDs.setServerNames(new String[]{CONTAINER.getHost()});
        pgDs.setPortNumbers(new int[]{CONTAINER.getFirstMappedPort()});
        pgDs.setDatabaseName(DATABASE_NAME);
        pgDs.setUser(USERNAME);
        pgDs.setPassword(PASSWORD);
        DS = pgDs;

        DB_HELPER = new DBHelper(DS, SQLReader.ofSemicolon());
    }

    @BeforeEach
    void resetTable() {
        DB_HELPER.executeScript("./src/test/resources/customer-table.sql");
    }

    @AfterAll
    static void cleanUp() {
        CONTAINER.stop();
    }

    @Test
    @DisplayName("When create is called it should insert the record and return the customer")
    void testSuccessfullyCreateCustomer() {
        // Given
        final var dao = new CustomerDAO(DS);
        final var expected = CustomerMockFactory.customerDTO(CUSTOMER_ID);

        // When
        final var actual = dao.create(expected);

        // Then
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("When create is called with a customer that already exists then it should throw an error")
    void testDuplicatedCustomerThrowsError() {
        // Given
        final var dao = new CustomerDAO(DS);
        final var customerId = CustomerMockFactory.EXISTING_CUSTOMER_ID;
        final var customerToCreate = CustomerMockFactory.customerDTO(customerId);

        // When
        final var actual = Assertions.assertThrows(RuntimeException.class, () -> dao.create(customerToCreate));

        // Then
        Assertions.assertEquals(org.postgresql.util.PSQLException.class, actual.getCause().getClass());
    }

    @Test
    @DisplayName("When getById is called with the ID of a user that does not exist then it should return an empty " +
            "Optional")
    void testGetByIdWithNonExistingUser() {
        // Given
        final var dao = new CustomerDAO(DS);

        // When
        final var oActual = dao.getById(CUSTOMER_ID);

        // Then
        Assertions.assertTrue(oActual.isEmpty());
    }

    @Test
    @DisplayName("When getById is called with an ID of an existing customer, then it should return an Optional " +
            "wrapping the retrieved customer")
    void testGetByIdWithExistingUser() {
        // Given
        final var dao = new CustomerDAO(DS);

        // When
        final var oActual = dao.getById(CustomerMockFactory.EXISTING_CUSTOMER_ID);

        // Then
        Assertions.assertTrue(oActual.isPresent());

        final var actual = oActual.get();
        Assertions.assertEquals(CustomerMockFactory.EXISTING_CUSTOMER_ID, actual.getId());
        Assertions.assertEquals("Alfreds Futterkiste", actual.getCompanyName());
        Assertions.assertEquals("Maria Anders", actual.getContactName());
        Assertions.assertEquals("Sales Representative", actual.getContactTitle());
        Assertions.assertEquals("Obere Str. 57", actual.getAddress());
        Assertions.assertEquals("Berlin", actual.getCity());
        Assertions.assertNull(actual.getRegion());
        Assertions.assertEquals("12209", actual.getPostalCode());
        Assertions.assertEquals("Germany", actual.getCountry());
        Assertions.assertEquals("030-0074321", actual.getPhone());
        Assertions.assertEquals("030-0076545", actual.getFax());
    }

}
