package persistence.helper;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.function.Function;

public class DBHelper {

    private final DataSource dataSource;
    private final SQLReader sqlReader;

    public DBHelper(DataSource dataSource, SQLReader sqlReader) {
        this.dataSource = dataSource;
        this.sqlReader = sqlReader;
    }

    public boolean execute(String sql) {
        return doWithConnection(connection -> {
            try (Statement stmt = connection.createStatement()) {
                return stmt.execute(sql);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    public boolean executeScript(String path) {
        return doWithConnection(connection -> {
            try (Statement stmt = connection.createStatement()) {
                var result = true;
                for (String s : sqlReader.readFile(path)) {
                    result = result && stmt.execute(s);
                }
                return result;
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    private <T> T doWithConnection(Function<Connection, T> fn) {
        try (final var conn = dataSource.getConnection()) {
            return fn.apply(conn);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

}
