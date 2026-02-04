package posrap.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL =
        "jdbc:postgresql://ep-icy-firefly-a14rm4re-pooler.ap-southeast-1.aws.neon.tech:5432/neondb"
      + "?sslmode=require";

    private static final String USER = "neondb_owner";
    private static final String PASS = "npg_OxQb4aepf5cr";

    static {
        try {
            Class.forName("org.postgresql.Driver");
            System.out.println("Driver loaded OK");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException  {
        System.out.println("TRY CONNECT NEON...");
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
