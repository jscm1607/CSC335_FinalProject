package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.h2.tools.Server;

/**
 * DBM is a database manager utility class for managing the
 * H2 database connection. It initializes the database, and
 * abstracts away JDBC boilerplate for downstream DAO classes
 */
public class DBM {
    private final String URL;
    private final String USER;
    private final String PASSWORD;

    public DBM(String user, String password, String url) {
        USER = user;
        PASSWORD = password;
        URL = url;
        // Initialize SQL tables from script "schema.sql"
        try (Connection conn = DriverManager.getConnection(
                URL + ";INIT=RUNSCRIPT FROM 'classpath:schema.sql'",
                USER, PASSWORD)) {
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * H2 Console (Web UI) for database management.
     */
    public void runH2Console() {
        try {
            Server.main("-web", "-webAllowOthers", "-webPort", "8082");
            System.out.println("H2 Console started at: http://localhost:8082");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Wrapper of PreparedStatement.executeUpdate() abstracting away boilerplate.
     */
    public void executeUpdate(String query, StatementPreparer preparer) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement statement = connection.prepareStatement(query)) {
            preparer.prepare(statement);
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Wrapper of PreparedStatement.executeQuery() abstracting away boilerplate, 
     * and handling ResultSet using passed ResultSetHandler to return as type T.
     */
    public <T> T executeQuery(String query, StatementPreparer preparer, ResultSetHandler<T> handler) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement statement = connection.prepareStatement(query)) {
            preparer.prepare(statement);
            try (ResultSet resultSet = statement.executeQuery()) {
                return handler.handle(resultSet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @FunctionalInterface
    public interface StatementPreparer {
        void prepare(PreparedStatement statement) throws Exception;
    }

    @FunctionalInterface
    public interface ResultSetHandler<T> {
        T handle(ResultSet resultSet) throws Exception;
    }
}