package posrap.dao;

import java.sql.Connection;
import java.sql.SQLException;
import posrap.util.DBConnection;

public class BaseDAO {

    public static Connection getConnection() throws SQLException {
        return DBConnection.getConnection();
    }
}
