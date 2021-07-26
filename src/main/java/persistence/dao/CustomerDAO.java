package persistence.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import persistence.dto.CustomerDTO;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

public class CustomerDAO {

    private static final String GET_BY_ID_SQL = "select customer_id, company_name, contact_name, contact_title, " +
            "address, city, region, postal_code, country, phone, fax from customer where customer_id = ?;";

    private static final String INSERT_SQL = "insert into customer (customer_id, company_name, contact_name," +
            "contact_title, address, city, region, postal_code, country, phone, fax) VALUES( ?, ?, ?, ?, ?, ?, ?," +
            "?, ?, ?, ?);";

    private final DataSource dataSource;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public CustomerDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Optional<CustomerDTO> getById(String id) {
        try (
                final Connection conn = dataSource.getConnection();
                final var stmt = conn.prepareStatement(GET_BY_ID_SQL)
        ) {
            stmt.setString(1, id);
            final var rs = stmt.executeQuery();
            if (!rs.next()) return Optional.empty();
            return Optional.of(
                    new CustomerDTO(
                            rs.getString(1),
                            rs.getString(2),
                            rs.getString(3),
                            rs.getString(4),
                            rs.getString(5),
                            rs.getString(6),
                            rs.getString(7),
                            rs.getString(8),
                            rs.getString(9),
                            rs.getString(10),
                            rs.getString(11)
                    )
            );
        } catch (SQLException ex) {
            logger.error("Error getting customer by id {}", id, ex);
            throw new RuntimeException(ex);
        }
    }

    public CustomerDTO create(CustomerDTO customer) {
        try (
                final Connection conn = dataSource.getConnection();
                final var stmt = conn.prepareStatement(INSERT_SQL)
        ) {
            stmt.setString(1, customer.getId());
            stmt.setString(2, customer.getCompanyName());
            stmt.setString(3, customer.getContactName());
            stmt.setString(4, customer.getContactTitle());
            stmt.setString(5, customer.getAddress());
            stmt.setString(6, customer.getCity());
            stmt.setString(7, customer.getRegion());
            stmt.setString(8, customer.getPostalCode());
            stmt.setString(9, customer.getCountry());
            stmt.setString(10, customer.getPhone());
            stmt.setString(11, customer.getFax());
            final int insertedRows = stmt.executeUpdate();
            if (insertedRows != 1) throw new RuntimeException("Inconsistency creating customer");
            return customer;
        } catch (SQLException ex) {
            logger.error("Error creating customer {}", customer, ex);
            throw new RuntimeException(ex);
        }
    }

}
