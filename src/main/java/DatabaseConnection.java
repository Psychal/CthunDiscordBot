import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//Work in progress.
class DatabaseConnection {
     static Connection getConnection() throws SQLException {
        String dbUrl = System.getenv("JDBC_DATABASE_URL");
        return DriverManager.getConnection(dbUrl);
    }
}
