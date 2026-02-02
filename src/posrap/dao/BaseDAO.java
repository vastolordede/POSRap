package posrap.dao;

import java.sql.Connection;
import java.sql.SQLException;
import posrap.util.DBConnection;

public class BaseDAO {

    // để các DAO kế thừa gọi được
    protected Connection getConnection() throws SQLException {
        return DBConnection.getConnection();
    }
}
