package com.ef.db;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Logger;

public class ConnectionFactory {
 
  private static  Connection con;
  
  final static Logger logger = Logger.getLogger(ConnectionFactory.class);
  
  public Connection getConnection() {
    Properties props = new Properties();
    try {
		props.load(readFile());
		String user = props.getProperty("user").trim();
		String password = props.getProperty("password").trim();
		String className = props.getProperty("mysql.class").trim();
		String databaseURL = props.getProperty("databaseURL").trim();
	    Class.forName(className); 
	    con = DriverManager.getConnection(databaseURL,user, password);
	} catch (IOException | SQLException | ClassNotFoundException e) {
		logger.error(e.getMessage(),e.getCause());
	}
    return con;
  }
  
  private  InputStream readFile() throws IOException {
	  InputStream path = this.getClass().getClassLoader().getResourceAsStream("db.properties");
	  return path;
  }
  

}
