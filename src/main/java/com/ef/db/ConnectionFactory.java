package com.ef.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactory {

  public static Connection getConnection() throws SQLException, ClassNotFoundException {
    Properties props = new Properties();
    props.put("user", "root");
    props.put("password", "admin");
    String databaseURL = "jdbc:mysql://localhost:3306/acess_log_db?useTimezone=true&serverTimezone=UTC";
    Class.forName("com.mysql.cj.jdbc.Driver"); 
    return DriverManager.getConnection(databaseURL,props);
  }
  
  private ConnectionFactory() {
    throw new IllegalStateException("Utility class");
  }

}
