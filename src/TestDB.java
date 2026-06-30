import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestDB {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/bankdb";
        String user = "root";
        String password = "9665811404@St";

        try {
            Connection con = DriverManager.getConnection(url, user, password);
            System.out.println("Database connected successfully");
            con.close();
        } catch (SQLException e) {
            System.out.println("Connection failed");
            System.out.println(e.getMessage());
        }
    }
}