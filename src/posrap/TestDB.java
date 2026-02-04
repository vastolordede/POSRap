package posrap;

import posrap.util.DBConnection;
import java.sql.Connection;

public class TestDB {
    public static void main(String[] args) {
        try (Connection c = DBConnection.getConnection()) {
            System.out.println("CONNECTED NEON SUCCESS");
            System.out.println(c.getMetaData().getURL());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
