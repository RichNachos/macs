import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String USER = "metropolis_user";
    private static final String PASSWORD = "password";
    private static final String SERVER_NAME = "jdbc:mysql://localhost:3306/";
    private static final String DB_NAME = "metropolis_db";
    private Connection conn;

    public Connection getConnection() { return conn; }
    public void stopConnection() throws SQLException {
        conn.close();
    }
    public DBConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(SERVER_NAME + DB_NAME, USER, PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
