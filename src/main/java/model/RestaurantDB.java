package model;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.h2.tools.Server;

public class RestaurantDB {
    private static final String DB_URL = "jdbc:h2:./data/db"; // Creates a file-based database
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "";

    /**
     * Executes a SQL statement (CREATE, INSERT, UPDATE, DELETE) and returns ResultSet if successful.
     * @param sql
     * @return
     */
    public static ResultSet executeSQL(String sql) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                Statement stmt = conn.createStatement()) {
            if (stmt.execute(sql)) 
                return stmt.getResultSet();
            else
                return null;
        }
    }

    public static void runH2Console() throws Exception {
        Server.main("-web", "-webAllowOthers", "-webPort", "8082");
        System.out.println("H2 Console started at: http://localhost:8082");
    }
}