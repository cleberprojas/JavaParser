package com.ef;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class LogDataRepository implements Repository {
  
  private static final String SELECT_ALL_LOGS = "SELECT acess_log.s_acess_date,\r\n" + 
                                                "    acess_log.s_acess_ip,\r\n" + 
                                                "    acess_log.s_acess_request,\r\n" + 
                                                "    acess_log.s_acess_status,\r\n" + 
                                                "    acess_log.s_acess_user_agent\r\n" + 
                                                "FROM acess_log_db.acess_log";
  
  private static final String INSERT_VALUES ="INSERT INTO acess_log_db.acess_log (" + 
                                              " s_acess_date, " + 
                                              " s_acess_ip, " + 
                                              " s_acess_request, " + 
                                              " s_acess_status," + 
                                              " s_acess_user_agent) " + 
                                              " VALUES ( ?, ?, ?, ?, ?) ";
  
  private Connection conn;
  private  PreparedStatement ps;
  
  @Override
  public int register(LogData log) {
    int rows = 0;
    Connection conn = null;
    try {
        conn = ConnectionFactory.getConnection();
        PreparedStatement ps = conn.prepareStatement(INSERT_VALUES);
        ps.setString(1,log.getLogDate().toString() );
        ps.setString(2, log.getIpNumber());
        ps.setString(3, log.getRequestMethod());
        ps.setString(4, log.getStatusCode());
        ps.setString(5, log.getSourceDescription());
        rows = ps.executeUpdate();
    } catch (ClassNotFoundException | SQLException e) {
        System.err.println(e.getMessage());
    }finally {
      try {
        conn.close();
      } catch (SQLException e) {
        System.err.println(e.getMessage());
      }
    }
    return rows;
  }

  @Override
  public List<LogData> findAll() {
    List<LogData> lstData =  new ArrayList<>();
    Connection conn = null;
    try {
      conn = ConnectionFactory.getConnection();
      Statement stmn = conn.createStatement();
      ResultSet resultSet = stmn.executeQuery(SELECT_ALL_LOGS);
      while(resultSet.next()) {
        lstData.add( new LogData(ParserUtils.stringAsDate(resultSet.getString(1)), 
                resultSet.getString(2), 
                resultSet.getString(3),
                resultSet.getString(4), 
                resultSet.getString(5)));
      }
    } catch (ClassNotFoundException | SQLException | ParseException e) {
      System.err.println(e.getMessage());
    }finally {
      try {
        conn.close();
      } catch (SQLException e) {
        System.err.println(e.getMessage());
      }
    }
    return lstData;
  }

  @Override
  public int saveAll(List<LogData> logs) {
    int rows = 0;
    try {
        conn = ConnectionFactory.getConnection();
        conn.setAutoCommit(false);
        ps = conn.prepareStatement(INSERT_VALUES);
        int i = 0;
        for(LogData log :logs) {
          ps.setString(1, ParserUtils.dateToString(log.getLogDate()));
          ps.setString(2, log.getIpNumber());
          ps.setString(3, log.getRequestMethod());
          ps.setString(4, log.getStatusCode());
          ps.setString(5, log.getSourceDescription());
          ps.addBatch();
          i++;
          if (i % 10000 == 0 || i == logs.size()) {
            ps.executeBatch(); // Execute every 1000 items.
            conn.commit();
          }
        }
    } catch (ClassNotFoundException | SQLException e) {
      System.err.println(e.getMessage());
      if (conn != null) {
          try {
              System.err.print("Transaction is being rolled back");
              conn.rollback();
          } catch(SQLException excep) {
            System.err.println(e.getMessage());
          }
      }
    }finally {
      try {
        conn.setAutoCommit(false);
        conn.close();
        ps.close();
      } catch (SQLException e) {
        System.err.println(e.getMessage());
      }
    }
    return rows;
  }
  

}
