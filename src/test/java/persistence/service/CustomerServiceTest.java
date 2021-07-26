package persistence.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import persistence.dao.CustomerDAO;
import persistence.dto.CustomerDTO;
import persistence.mock.CustomerMockFactory;

import java.util.Optional;

public class CustomerServiceTest {

    private static final String CUSTOMER_ID = "GUERR";

    @Test
    @DisplayName("When the customer already exists then the service should do nothing")
    void testDoNothingIfCustomerExists() {
        // Given
        final var dao = Mockito.mock(CustomerDAO.class);
        Mockito.when(dao.getById(Mockito.eq(CUSTOMER_ID)))
                .thenReturn(Optional.of(CustomerMockFactory.customerDTO(CUSTOMER_ID)));

        final var service = new CustomerService(dao);

        // When
        service.createIfNotExists();

        // Then
        Mockito.verify(dao, Mockito.times(1)).getById(Mockito.eq(CUSTOMER_ID));
    }

    @Test
    @DisplayName("When the customer does not exist then the service should have it created")
    void testCreateCustomerIfItDoesNotExists() {
        // Given
        final var dao = Mockito.mock(CustomerDAO.class);
        Mockito.when(dao.getById(Mockito.eq(CustomerServiceTest.CUSTOMER_ID))).thenReturn(Optional.empty());
        Mockito.when(dao.create(Mockito.any(CustomerDTO.class)))
                .thenReturn(CustomerMockFactory.customerDTO(CustomerServiceTest.CUSTOMER_ID));

        final var service = new CustomerService(dao);

        // When
        service.createIfNotExists();

        // Then
        Mockito.verify(dao, Mockito.times(1)).getById(Mockito.eq(CUSTOMER_ID));
        Mockito.verify(dao, Mockito.times(1)).create(Mockito.any(CustomerDTO.class));
    }

}
