package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.h2.tools.Server;

public class DatabaseManager {
    private static final String URL = "jdbc:h2:./data/db"; // File-based H2 db
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    // Init SQL tables from script schema.sql
    static {
        try (Connection conn = DriverManager.getConnection(
                URL + ";INIT=RUNSCRIPT FROM 'classpath:schema.sql'",
                USER, PASSWORD)) {
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Wrapper of DriverManager.getConnection() with credentials
     * 
     * @return Connection
     * @throws SQLException
     */
    static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /**
     * Runs web-based H2 Console
     */
    public static void runH2Console() {
        try {
            Server.main("-web", "-webAllowOthers", "-webPort", "8082");
            System.out.println("H2 Console started at: http://localhost:8082");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}