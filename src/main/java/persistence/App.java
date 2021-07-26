package persistence;

import org.postgresql.ds.PGSimpleDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import persistence.dao.CustomerDAO;
import persistence.service.CustomerService;

import javax.sql.DataSource;

public class App {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public static void main(String[] args) {
        new App().execute();
    }

    public void execute() {
        DataSource dataSource;
        try {
            dataSource = buildSource();
            final var customerDAO = new CustomerDAO(dataSource);
            final var customerService = new CustomerService(customerDAO);
            customerService.createIfNotExists();
        } catch (Throwable ex) {
            logger.error("Error running application", ex);
        }
    }

    private DataSource buildSource() {
        PGSimpleDataSource source = new PGSimpleDataSource();
        source.setDatabaseName("postgres");
        source.setUser("postgres");
        source.setPassword("academy");
        source.setApplicationName("persistence-testing");
        return source;
    }

}
