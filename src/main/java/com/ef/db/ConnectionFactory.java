package com.ef.db;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactory {
 
  private static  Connection con;
  
  public Connection getConnection() throws SQLException, ClassNotFoundException {
    Properties props = new Properties();
    try {
		props.load(readFile());
		String user = props.getProperty("user").trim();
		String password = props.getProperty("password").trim();
		String className = props.getProperty("mysql.class").trim();
		String databaseURL = props.getProperty("databaseURL").trim();
	    Class.forName(className); 
	    con = DriverManager.getConnection(databaseURL,user, password);
	} catch (IOException e) {
		e.printStackTrace();
	}
    return con;
  }
  
  private  InputStream readFile() throws IOException {
	  InputStream path = this.getClass().getClassLoader().getResourceAsStream("db.properties");
	  return path;
  }
  

}
