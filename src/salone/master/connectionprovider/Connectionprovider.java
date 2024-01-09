package salone.master.connectionprovider;

import java.sql.*;

public class Connectionprovider {
    private static Connection con;

    public static Connection getConnection() {
        try {
            Class.forName("org.postgresql.Driver");

            String url = "jdbc:postgresql://localhost:5432/trevita_ms";
            String username = "postgres";
            String password = "admin123";
            con = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return con;
    }
}
