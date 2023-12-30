package salone.master.connectionprovider;

import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
public class Connectionprovider {
    private static Connection con;

    public static Connection getConnection(){

      try{
          if(con == null){
            Class.forName("org.postgresql.Driver");
            
            String url = "jdbc:postgresql://localhost:5432/demo";
            String username = "postgres";
            String password = "admin123";
            con = DriverManager.getConnection(url, username, password);
        }

      }catch(Exception e){
        e.printStackTrace();
      }
        return con;
    }
}
