package persistence.mock;

import persistence.dto.CustomerDTO;

public class CustomerMockFactory {

    public static final String EXISTING_CUSTOMER_ID = "ALFKI";

    public static CustomerDTO customerDTO(String customerId) {
        return new CustomerDTO(customerId, "Alfreds Futterkiste", "Maria Anders",
                "Sales Representative", "Obere Str. 57", "Berlin", "BV",
                "12209", "Germany", "030-0074321", "030-0076545");
    }

}
