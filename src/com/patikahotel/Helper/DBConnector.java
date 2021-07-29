package com.patikahotel.Helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnector {

    public Connection connectDB()
    {
        Connection connection = null;
        try
        {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(Config.DB_URL, Config.DB_USERNAME, Config.DB_PW);
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }

        return connection;
    }

    public static Connection getConnection()
    {
        DBConnector db = new DBConnector();
        return db.connectDB();
    }
}
