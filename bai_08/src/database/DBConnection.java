
package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL =
        "jdbc:mysql://localhost:3306/quan_ly_sinh_vien";

    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void main(String[] args) {
        try (Connection conn = getConnection()) {
            System.out.println("Ket noi MySQL thanh cong!");
            System.out.println("Database: " + conn.getCatalog());
        } catch (SQLException e) {
            System.out.println("Ket noi MySQL that bai!");
            e.printStackTrace();
        }
    }
}
