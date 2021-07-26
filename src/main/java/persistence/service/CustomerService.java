package persistence.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import persistence.dao.CustomerDAO;
import persistence.dto.CustomerDTO;

public class CustomerService {

    private final CustomerDAO customerDAO;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public CustomerService(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    public void createIfNotExists() {
        final var customerToCreate = new CustomerDTO(
                "GUERR",
                "Güerrín",
                "Juan Carlos Batman",
                "CEO",
                "Avenida Corrientes 1368",
                "Buenos Aires",
                null,
                "C1043",
                "Argentina",
                "+54 011 4371-8141",
                null
        );

        final var oCustomer = customerDAO.getById(customerToCreate.getId());
        if (oCustomer.isEmpty()) {
            logger.info("Customer not found. Creating it...");
            final var createdCustomer = customerDAO.create(customerToCreate);
            logger.info("Customer created: {}", createdCustomer);
            return;
        }

        logger.info("Customer {} already exists: {}", customerToCreate.getId(), oCustomer.get());
    }
}
