// Database Manager

package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.h2.tools.Server;

/**
 * DBM is a database manager utility class for managing the
 * H2 database connection. It initializes the database, and
 * abstracts away JDBC boilerplate for downstream DAO classes
 */
public class DBM implements AutoCloseable {
    private final String URL;
    private final String USER;
    private final String PASSWORD;
    private Connection CONNECTION;

    public DBM() {
        this("sa", "", "jdbc:h2:./data/db");
    }

    public DBM(String user, String password, String url) {
        USER = user;
        PASSWORD = password;
        URL = url;
        // Initialize SQL tables from script "schema.sql"
        try {
            CONNECTION = DriverManager.getConnection(
                    URL + ";INIT=RUNSCRIPT FROM 'classpath:schema.sql'",
                    USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        this.runH2Console();
    }

    /**
     * Closes the database connection when the DBM instance is no longer needed.
     */
    @Override
    public void close() {
        try {
            if (CONNECTION != null && !CONNECTION.isClosed()) {
                CONNECTION.close();
            }
        } catch (Exception e) {
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
        try (PreparedStatement statement = CONNECTION.prepareStatement(query)) {
            preparer.prepare(statement);
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int executeInsert(String query, StatementPreparer preparer){
        try (PreparedStatement statement = CONNECTION.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            preparer.prepare(statement);
            if (statement.executeUpdate() > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    } else {
                        return -1;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -2;
        }
        return -1;
    }


    /**
     * Wrapper of PreparedStatement.executeQuery() abstracting away boilerplate,
     * and handling ResultSet using passed ResultSetHandler to return as type T.
     */
    public <T> T executeQuery(String query, StatementPreparer preparer, ResultSetHandler<T> handler) {
        try (PreparedStatement statement = CONNECTION.prepareStatement(query)) {
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