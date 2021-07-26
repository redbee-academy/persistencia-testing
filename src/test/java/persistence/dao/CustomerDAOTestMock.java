package persistence.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import persistence.mock.CustomerMockFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.PreparedStatement;

@DisplayName("Unit tests using Mockito framework")
public class CustomerDAOTestMock {

    private static final String CUSTOMER_ID = "someId";

    @Test
    @DisplayName("When getById is called with an ID of a non existing customer, then it should return an empty " +
            "Optional")
    void testGetByIdWithNonExistingCustomer() throws SQLException {
        // Given
        final var rs = Mockito.mock(ResultSet.class);
        Mockito.when(rs.next()).thenReturn(false);

        final var stmt = Mockito.mock(PreparedStatement.class);
        Mockito.doNothing().when(stmt).setString(Mockito.eq(1), Mockito.eq(CUSTOMER_ID));
        Mockito.when(stmt.executeQuery()).thenReturn(rs);

        final var conn = Mockito.mock(Connection.class);
        Mockito.when(conn.prepareStatement(Mockito.any(String.class))).thenReturn(stmt);

        final var ds = Mockito.mock(DataSource.class);
        Mockito.when(ds.getConnection()).thenReturn(conn);

        final var dao = new CustomerDAO(ds);

        // When
        final var oActual = dao.getById(CUSTOMER_ID);

        // Then
        Assertions.assertNotNull(oActual);
        Assertions.assertTrue(oActual.isEmpty());
    }

    @Test
    @DisplayName("When getById is called with an ID of an existing customer, then it should return an Optional " +
            "wrapping the retrieved customer")
    void testGetByIdWithExistingCustomer() throws SQLException {
        // Given
        final var expected = CustomerMockFactory.customerDTO(CUSTOMER_ID);

        final var rs = Mockito.mock(ResultSet.class);
        Mockito.when(rs.next()).thenReturn(true);
        Mockito.when(rs.getString(Mockito.eq(1))).thenReturn(CUSTOMER_ID);
        Mockito.when(rs.getString(Mockito.eq(2))).thenReturn(expected.getCompanyName());
        Mockito.when(rs.getString(Mockito.eq(3))).thenReturn(expected.getContactName());
        Mockito.when(rs.getString(Mockito.eq(4))).thenReturn(expected.getContactTitle());
        Mockito.when(rs.getString(Mockito.eq(5))).thenReturn(expected.getAddress());
        Mockito.when(rs.getString(Mockito.eq(6))).thenReturn(expected.getCity());
        Mockito.when(rs.getString(Mockito.eq(7))).thenReturn(expected.getRegion());
        Mockito.when(rs.getString(Mockito.eq(8))).thenReturn(expected.getPostalCode());
        Mockito.when(rs.getString(Mockito.eq(9))).thenReturn(expected.getCountry());
        Mockito.when(rs.getString(Mockito.eq(10))).thenReturn(expected.getPhone());
        Mockito.when(rs.getString(Mockito.eq(11))).thenReturn(expected.getFax());

        final var stmt = Mockito.mock(PreparedStatement.class);
        Mockito.doNothing().when(stmt).setString(Mockito.eq(1), Mockito.eq(CUSTOMER_ID));
        Mockito.when(stmt.executeQuery()).thenReturn(rs);

        final var conn = Mockito.mock(Connection.class);
        Mockito.when(conn.prepareStatement(Mockito.any(String.class))).thenReturn(stmt);

        final var ds = Mockito.mock(DataSource.class);
        Mockito.when(ds.getConnection()).thenReturn(conn);

        final var dao = new CustomerDAO(ds);

        // When
        final var oActual = dao.getById(CUSTOMER_ID);

        // Then
        Assertions.assertNotNull(oActual);
        Assertions.assertTrue(oActual.isPresent());

        final var actual = oActual.get();
        Assertions.assertEquals(expected.getId(), actual.getId());
        Assertions.assertEquals(expected.getCompanyName(), actual.getCompanyName());
        Assertions.assertEquals(expected.getContactName(), actual.getContactName());
        Assertions.assertEquals(expected.getContactTitle(), actual.getContactTitle());
        Assertions.assertEquals(expected.getAddress(), actual.getAddress());
        Assertions.assertEquals(expected.getCity(), actual.getCity());
        Assertions.assertEquals(expected.getRegion(), actual.getRegion());
        Assertions.assertEquals(expected.getPostalCode(), actual.getPostalCode());
        Assertions.assertEquals(expected.getCountry(), actual.getCountry());
        Assertions.assertEquals(expected.getPhone(), actual.getPhone());
        Assertions.assertEquals(expected.getFax(), actual.getFax());
    }

    @Test
    @DisplayName("When a SQLException happens executing getById, then it should be wrapped in a RuntimeException")
    void testGetByIdShouldWrapSQLExceptions() throws SQLException {
        // Given
        final var ds = Mockito.mock(DataSource.class);
        Mockito.when(ds.getConnection()).thenThrow(new SQLException("Some error"));
        final var dao = new CustomerDAO(ds);

        // When
        final var actual = Assertions.assertThrows(RuntimeException.class, () -> dao.getById(CUSTOMER_ID));

        // Then
        Assertions.assertEquals("java.sql.SQLException: Some error", actual.getMessage());
    }

    @Disabled("Implement it yourself!")
    @Test
    @DisplayName("When create is called it should insert the record and return the customer")
    void testSuccessfullyCreateCustomer() {
        // TODO: Implement it!
    }

}
