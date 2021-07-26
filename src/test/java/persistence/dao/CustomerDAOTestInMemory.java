package persistence.dao;

import org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Assertions;
import persistence.helper.DBHelper;
import persistence.helper.SQLReader;
import persistence.mock.CustomerMockFactory;

import javax.sql.DataSource;

@DisplayName("DAO tests using H2 in-memory Database")
public class CustomerDAOTestInMemory {

    private static final String DB_URL = "jdbc:h2:mem:public;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE;DB_CLOSE_DELAY=-1";
    private static DataSource DS;
    private static DBHelper DB_HELPER;
    private static final String CUSTOMER_ID = "GUERR";

    @BeforeAll
    static void setUp() {
        final var ds = new JdbcDataSource();
        ds.setUrl(DB_URL);
        DS = ds;
        DB_HELPER = new DBHelper(DS, SQLReader.ofSemicolon());
    }

    @BeforeEach
    void resetTable() {
        DB_HELPER.executeScript("./src/test/resources/customer-table.sql");
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
        Assertions.assertEquals(JdbcSQLIntegrityConstraintViolationException.class, actual.getCause().getClass());
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
