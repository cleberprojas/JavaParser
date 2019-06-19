package com.ef.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import com.ef.db.ConnectionFactory;
import com.ef.dto.LogDataDTO;
import com.ef.model.LogData;
import com.ef.utils.ParserUtils;

public class LogDataRepository implements Repository {
  
  private static final String CUSTOM_DATE_FORMAT = "yyyy-MM-dd.HH:mm:ss.SSS";
   
  private static final String INSERT_VALUES ="INSERT INTO acess_log_db.acess_log (" + 
                                              " s_acess_date, " + 
                                              " s_acess_ip, " + 
                                              " s_acess_request, " + 
                                              " s_acess_status," + 
                                              " s_acess_user_agent) " + 
                                              " VALUES ( ?, ?, ?, ?, ?) ";
  private static final String INSERT_BLOCKED_IP = "  INSERT INTO acess_log_db.acess_blocked_ip " + 
                                                  "  ( s_acess_ip, s_acess_blocked_reason)" + 
                                                  "  VALUES (?, ? )"; 
  
  private static final String SELECT_LOGS_BY_DATE_THRESHOLD = " SELECT A_LOG.S_ACESS_IP , COUNT(A_LOG.S_ACESS_IP) AS TIMES_REQUEST " + 
                                                              " FROM acess_log_db.acess_log AS A_LOG " + 
                                                              " WHERE A_LOG.s_acess_date >= ? " + 
                                                              "    AND A_LOG.s_acess_date <= ?  " + 
                                                              " GROUP BY A_LOG.S_ACESS_IP " + 
                                                              " HAVING TIMES_REQUEST > ? "; 
  private static final String SELECT_LOGS_BY_IP = " SELECT A_LOG.S_ACESS_IP, COUNT(A_LOG.S_ACESS_IP) AS TIMES_REQUEST " + 
                                                  "FROM acess_log_db.acess_log AS A_LOG " + 
                                                  "WHERE A_LOG.S_ACESS_IP = ? " + 
                                                  "GROUP BY A_LOG.S_ACESS_IP "; 
  
  private Connection conn;
  private  PreparedStatement ps;
  private ResultSet resultSet;
  
  @Override
  public int register(LogData log) {
    int rows = 0;
    try {
        conn = ConnectionFactory.getConnection();
        ps = conn.prepareStatement(INSERT_VALUES);
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
        if (conn != null) {
          ps.close();
          conn.close();
        }
      } catch (SQLException e) {
        System.err.println(e.getMessage());
      }
    }
    return rows;
  }
  
  @Override
  public int saveBlockedIp(String ipBlocked, String blockedMessage ) {
    int rows = 0;
    try {
        conn = ConnectionFactory.getConnection();
        ps = conn.prepareStatement(INSERT_BLOCKED_IP);
        ps.setString(1, ipBlocked);
        ps.setString(2, blockedMessage);
        rows = ps.executeUpdate();
    }catch (ClassNotFoundException | SQLException e) {
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
          if (conn != null) {
            ps.close();
            conn.close();
          }
        } catch (SQLException e) {
          System.err.println(e.getMessage());
        }
      }
    return rows;
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
          ps.setString(1, ParserUtils.dateToString(log.getLogDate(),CUSTOM_DATE_FORMAT));
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
        conn.setAutoCommit(true);
        conn.close();
        ps.close();
      } catch (SQLException e) {
        System.err.println(e.getMessage());
      }    }
    return rows;
  }

  public Optional<List<LogDataDTO>> findLogsByDateAndThreshold(Date start, Date end, int threshold) {
    List<LogDataDTO> lstData =  new ArrayList<>();
    try {
      conn = ConnectionFactory.getConnection();
      ps = conn.prepareStatement(SELECT_LOGS_BY_DATE_THRESHOLD);
      ps.setString(1, ParserUtils.dateToString(start,CUSTOM_DATE_FORMAT));
      ps.setString(2, ParserUtils.dateToString(end,CUSTOM_DATE_FORMAT));
      ps.setInt(3,  threshold);
      resultSet = ps.executeQuery();
      while(resultSet.next()) {
        lstData.add( new LogDataDTO( resultSet.getString(1), 
                resultSet.getLong(2)));
      }
    }catch (ClassNotFoundException | SQLException  e) {
        System.err.println(e.getMessage());
    }finally {
      try {
        if (conn != null) {
          resultSet.close();
          ps.close();
          conn.close();
        }
      } catch (SQLException e) {
        System.err.println(e.getMessage());
      }
    }
    if(lstData.isEmpty())
      return Optional.ofNullable(null);
    return Optional.of(lstData) ;
  }
  
  public Optional<List<LogDataDTO>> findRequestByIp(String ipToFind) {
    List<LogDataDTO> lstData =  new ArrayList<>();
    try {
      conn = ConnectionFactory.getConnection();
      ps = conn.prepareStatement(SELECT_LOGS_BY_IP);
      ps.setString(1, ipToFind);
      resultSet = ps.executeQuery();
      while(resultSet.next()) {
        lstData.add( new LogDataDTO( resultSet.getString(1), 
            resultSet.getLong(2)));
      }
    }catch (ClassNotFoundException | SQLException  e) {
      System.err.println(e.getMessage());
    }finally {
      try {
        if (conn != null) {
          resultSet.close();
          ps.close();
          conn.close();
        }
      } catch (SQLException e) {
        System.err.println(e.getMessage());
      }
    }
    if(lstData.isEmpty())
      return Optional.ofNullable(null);
    return Optional.of(lstData) ;
  }


}
