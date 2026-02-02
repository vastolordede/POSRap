package posrap.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL =
        "jdbc:sqlserver://localhost:1433;"
      + "databaseName=RapChieuPhim;"
      + "encrypt=false;"
      + "trustServerCertificate=true";

    private static final String USER = "sa";
    private static final String PASS = "123456";

    static {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            System.out.println("✅ SQL Server JDBC Driver loaded");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("❌ Không load được JDBC Driver", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
